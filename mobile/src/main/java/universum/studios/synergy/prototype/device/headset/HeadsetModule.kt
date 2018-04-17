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
package universum.studios.synergy.prototype.device.headset

import android.bluetooth.BluetoothManager
import android.content.Context
import dagger.Module
import dagger.Provides
import universum.studios.android.arkhitekton.util.Preconditions
import universum.studios.synergy.prototype.device.Device
import universum.studios.synergy.prototype.device.headset.neurosky.NeuroSkyHeadset

/**
 * @author Martin Albedinsky
 */
@Module class HeadsetModule {

    @Provides fun provideHeadset(context: Context, device: Device): Headset {
        val bluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        Preconditions.checkNotNull(bluetoothAdapter, "No bluetooth adapter available!")
        val bluetoothDevice = bluetoothAdapter!!.getRemoteDevice(device.address)
        return NeuroSkyHeadset(context, bluetoothDevice)
    }
}