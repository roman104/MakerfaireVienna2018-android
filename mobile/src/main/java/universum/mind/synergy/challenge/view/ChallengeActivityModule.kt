/*
 * *************************************************************************************************
 *                                 Copyright 2018 Universum Studios
 * *************************************************************************************************
 *                  Licensed under the Apache License, Version 2.0 (the "License")
 * -------------------------------------------------------------------------------------------------
 * You may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 * 
 * See the License for the specific language governing permissions and limitations under the License.
 * *************************************************************************************************
 */
package universum.mind.synergy.challenge.view

import android.arch.lifecycle.ViewModelProviders
import android.bluetooth.BluetoothManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import universum.mind.synergy.device.headset.Headset
import universum.mind.synergy.device.headset.neurosky.NeuroSkyHeadset
import universum.mind.synergy.device.view.DeviceSelectionFragment
import universum.mind.synergy.device.view.DeviceSelectionFragmentModule
import universum.mind.synergy.observation.view.ObservationHeadsetHolder
import universum.studios.android.arkhitekton.util.Preconditions

/**
 * @author Martin Albedinsky
 */
@Module
@Suppress("unused")
abstract class ChallengeActivityModule {

    @Module companion object {

        @Provides @JvmStatic fun provideHeadset(activity: ChallengeActivity): Headset {
            val headsetHolder = ViewModelProviders.of(activity).get(ObservationHeadsetHolder::class.java)
            if (headsetHolder.hasHeadset()) {
                return headsetHolder.getHeadset()
            }
            val bluetoothAdapter = (activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
            Preconditions.checkNotNull(bluetoothAdapter, "No bluetooth adapter available!")
            val bluetoothDevice = bluetoothAdapter!!.getRemoteDevice(activity.selectedDevice.address)
            return headsetHolder.attachHeadset(NeuroSkyHeadset(activity, bluetoothDevice))
        }
    }

	@ContributesAndroidInjector(modules = [DeviceSelectionFragmentModule::class])
	abstract fun contributeDeviceSelectionFragmentInjector(): DeviceSelectionFragment

	@ContributesAndroidInjector(modules = [ChallengeFragmentModule::class])
	abstract fun contributeChallengeFragmentInjector(): ChallengeFragment
}