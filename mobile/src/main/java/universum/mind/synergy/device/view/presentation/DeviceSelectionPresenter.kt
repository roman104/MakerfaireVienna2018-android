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

import universum.mind.synergy.device.Device
import universum.mind.synergy.device.view.DeviceSelectionView
import universum.mind.synergy.system.bluetooth.BluetoothError
import universum.mind.synergy.system.location.LocationError
import universum.studios.android.arkhitekton.presentation.Presenter

/**
 * @author Martin Albedinsky
 */
interface DeviceSelectionPresenter : Presenter<DeviceSelectionView> {

    fun onBluetoothError(error: BluetoothError)

    fun onLocationError(error: LocationError)

    fun onDevicesChanged(devices: List<Device>)
}