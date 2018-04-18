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
package universum.studios.synergy.prototype.device.control

import android.arch.lifecycle.Observer
import android.bluetooth.BluetoothDevice
import universum.studios.android.arkhitekton.control.ReactiveController
import universum.studios.android.arkhitekton.interaction.Interactor
import universum.studios.synergy.prototype.device.Device
import universum.studios.synergy.prototype.device.data.LiveBluetoothDevices
import universum.studios.synergy.prototype.device.view.presentation.DeviceSelectionPresenter

/**
 * @author Martin Albedinsky
 */
class DefaultDeviceSelectionController internal constructor(builder: Builder) : ReactiveController<Interactor, DeviceSelectionPresenter>(builder), DeviceSelectionController {

    companion object {
    
        @Suppress("unused") const val TAG = "DefaultDeviceSelectionController"
    }

    private val devicesObserver: Observer<List<BluetoothDevice>> = Observer { devices -> getPresenter().onDevicesChanged(devices!!.map { Device(it.name, it.address) }) }
    private val devicesLiveData = builder.devicesLiveData

    override fun startDevicesDiscovery() {
        isActive().let { devicesLiveData.observeForever(devicesObserver) }
    }

    override fun restartDevicesDiscovery() {
        stopDevicesDiscovery()
        startDevicesDiscovery()
    }

    override fun stopDevicesDiscovery() {
        devicesLiveData.removeObserver(devicesObserver)
    }

    override fun onDeactivated() {
        super.onDeactivated()
        stopDevicesDiscovery()
    }
    
    class Builder(
        interactor: Interactor,
        presenter: DeviceSelectionPresenter
    ) : ReactiveController.BaseBuilder<Builder, Interactor, DeviceSelectionPresenter>(interactor, presenter) {
    
        override val self = this
        lateinit var devicesLiveData: LiveBluetoothDevices

        override fun build() = DefaultDeviceSelectionController(this)
    }
}