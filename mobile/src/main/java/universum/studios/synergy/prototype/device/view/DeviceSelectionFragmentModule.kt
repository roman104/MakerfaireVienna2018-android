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
package universum.studios.synergy.prototype.device.view

import dagger.Module
import dagger.Provides
import universum.studios.android.arkhitekton.interaction.Interactor
import universum.studios.synergy.prototype.device.control.DefaultDeviceSelectionController
import universum.studios.synergy.prototype.device.control.DeviceSelectionController
import universum.studios.synergy.prototype.device.data.LiveBluetoothDevices
import universum.studios.synergy.prototype.device.view.presentation.DefaultDeviceSelectionPresenter
import universum.studios.synergy.prototype.device.view.presentation.DeviceSelectionPresenter
import universum.studios.synergy.prototype.view.BaseFragmentModule
import universum.studios.synergy.prototype.view.EmptyFragmentModule

/**
 * @author Martin Albedinsky
 */
@Module(includes = [EmptyFragmentModule::class])
class DeviceSelectionFragmentModule : BaseFragmentModule() {

	@Provides fun provideController(
			fragment: DeviceSelectionFragment,
			interactor: Interactor,
			presenter: DeviceSelectionPresenter
	): DeviceSelectionController {
	    val holder = provideControllerHolder(fragment, DeviceSelectionController.Holder::class.java)
		return if (holder.hasController()) holder.getController()
		else holder.attachController(DefaultDeviceSelectionController.Builder(interactor, presenter)
				.apply { devicesLiveData = LiveBluetoothDevices.create(fragment.requireContext()) }
				.build())
	}
	
	@Provides fun providePresenter(viewModel: DeviceSelectionViewModel): DeviceSelectionPresenter = DefaultDeviceSelectionPresenter(viewModel)
	@Provides fun provideViewModel(fragment: DeviceSelectionFragment): DeviceSelectionViewModel = provideViewModel(fragment, DeviceSelectionViewModelImpl::class.java)
}