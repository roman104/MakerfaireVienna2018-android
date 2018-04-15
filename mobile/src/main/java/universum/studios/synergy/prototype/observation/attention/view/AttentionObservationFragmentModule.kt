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
package universum.studios.synergy.prototype.observation.attention.view

import android.bluetooth.BluetoothManager
import android.content.Context
import dagger.Module
import dagger.Provides
import universum.studios.android.arkhitekton.interaction.Interactor
import universum.studios.synergy.prototype.device.Device
import universum.studios.synergy.prototype.observation.attention.control.AttentionObservationController
import universum.studios.synergy.prototype.observation.attention.control.DefaultAttentionObservationController
import universum.studios.synergy.prototype.observation.attention.view.presentation.AttentionObservationPresenter
import universum.studios.synergy.prototype.observation.attention.view.presentation.DefaultAttentionObservationPresenter
import universum.studios.synergy.prototype.view.BaseFragmentModule
import universum.studios.synergy.prototype.view.EmptyFragmentModule

/**
 * @author Martin Albedinsky
 */
@Module(includes = [EmptyFragmentModule::class])
class AttentionObservationFragmentModule : BaseFragmentModule() {

	@Provides fun provideController(
			fragment: AttentionObservationFragment,
			interactor: Interactor,
			presenter: AttentionObservationPresenter
	): AttentionObservationController {
	    val holder = provideControllerHolder(fragment, AttentionObservationController.Holder::class.java)
		return if (holder.hasController()) holder.getController()
		else holder.attachController(DefaultAttentionObservationController.Builder(interactor, presenter)
                .apply {
                    bluetoothAdapter = (fragment.requireContext().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
                    device = fragment.arguments?.getParcelable(AttentionObservationFragment.ARGUMENT_DEVICE) as Device
                }
                .build())
	}
	
	@Provides fun providePresenter(viewModel: AttentionObservationViewModel): AttentionObservationPresenter = DefaultAttentionObservationPresenter(viewModel)
	@Provides fun provideViewModel(fragment: AttentionObservationFragment): AttentionObservationViewModel = provideViewModel(fragment, AttentionObservationViewModelImpl::class.java)
}