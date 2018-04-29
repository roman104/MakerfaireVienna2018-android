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
package universum.mind.synergy.device.view.presentation

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import universum.mind.synergy.R
import universum.mind.synergy.device.Device
import universum.mind.synergy.device.view.DeviceSelectionViewModel
import universum.mind.synergy.view.ScreenView
import universum.mind.synergy.view.dialog.DialogsView
import universum.studios.android.arkhitekton.presentation.BasePresenter

/**
 * @author Martin Albedinsky
 */
class DefaultDeviceSelectionPresenter(viewModel: DeviceSelectionViewModel) :
        BasePresenter<ScreenView<DeviceSelectionViewModel>,DeviceSelectionViewModel>(viewModel),
        DeviceSelectionPresenter,
        LifecycleObserver {

    override fun onBluetoothNotEnabled() {
        performViewLifecycleAction(object : LifecycleAction() {

            override fun run() {
                getView().showDialogWithId(R.id.dialog_bluetooth_disabled, DialogsView.NO_DIALOG_OPTIONS)
            }
        }.withStateCondition(Lifecycle.State.RESUMED))
    }
    
	override fun onDevicesChanged(devices: List<Device>) {
        getViewModel().updateDevices(devices)
	}
}