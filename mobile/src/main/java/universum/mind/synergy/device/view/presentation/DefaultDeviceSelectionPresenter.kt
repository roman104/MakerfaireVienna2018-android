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

import android.Manifest
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import universum.mind.synergy.R
import universum.mind.synergy.device.Device
import universum.mind.synergy.device.view.DeviceSelectionView
import universum.mind.synergy.device.view.DeviceSelectionViewModel
import universum.mind.synergy.system.bluetooth.BluetoothError
import universum.mind.synergy.system.location.LocationError
import universum.mind.synergy.view.DialogsView
import universum.studios.android.arkhitekton.presentation.BasePresenter

/**
 * @author Martin Albedinsky
 */
class DefaultDeviceSelectionPresenter(viewModel: DeviceSelectionViewModel) :
        BasePresenter<DeviceSelectionView, DeviceSelectionViewModel>(viewModel),
        DeviceSelectionPresenter,
        LifecycleObserver {

    companion object {

        private val BLUETOOTH_PERMISSIONS = arrayOf(Manifest.permission.BLUETOOTH)
        private val LOCATION_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun onBluetoothError(error: BluetoothError) {
        performViewLifecycleAction(object : LifecycleAction() {

            override fun run() {
                val view = getView()
                when (error) {
                    BluetoothError.NOT_AVAILABLE -> view.showDialogWithId(R.id.dialog_bluetooth_unavailable, DialogsView.NO_DIALOG_OPTIONS)
                    BluetoothError.NOT_ENABLED -> view.showDialogWithId(R.id.dialog_bluetooth_disabled, DialogsView.NO_DIALOG_OPTIONS)
                    BluetoothError.NOT_PERMITTED -> {
                        if (view.shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH)) {
                            view.showDialogWithId(R.id.dialog_bluetooth_permission_rationale, DialogsView.NO_DIALOG_OPTIONS)
                        } else {
                            view.requestPermissions(BLUETOOTH_PERMISSIONS, DeviceSelectionView.PERMISSION_REQUEST_BLUETOOTH)
                        }
                    }
                }
            }
        }.withStateCondition(Lifecycle.State.RESUMED))
    }

    override fun onLocationError(error: LocationError) {
        performViewLifecycleAction(object : LifecycleAction() {

            override fun run() {
                val view = getView()
                when (error) {
                    LocationError.NOT_AVAILABLE -> view.showDialogWithId(R.id.dialog_location_unavailable, DialogsView.NO_DIALOG_OPTIONS)
                    LocationError.NOT_ENABLED -> view.showDialogWithId(R.id.dialog_location_disabled, DialogsView.NO_DIALOG_OPTIONS)
                    LocationError.NOT_PERMITTED -> {
                        if (view.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            view.showDialogWithId(R.id.dialog_location_permission_rationale, DialogsView.NO_DIALOG_OPTIONS)
                        } else {
                            view.requestPermissions(LOCATION_PERMISSIONS, DeviceSelectionView.PERMISSION_REQUEST_LOCATION)
                        }
                    }
                }
            }
        }.withStateCondition(Lifecycle.State.RESUMED))
    }
    
	override fun onDevicesChanged(devices: List<Device>) {
        getViewModel().updateDevices(devices)
	}
}