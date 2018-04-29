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
package universum.mind.synergy.device.control

import android.arch.lifecycle.Observer
import android.bluetooth.BluetoothDevice
import android.content.Context
import universum.mind.synergy.device.Device
import universum.mind.synergy.device.data.LiveBluetoothDevices
import universum.mind.synergy.device.view.presentation.DeviceSelectionPresenter
import universum.mind.synergy.util.BluetoothUtils
import universum.studios.android.arkhitekton.control.ReactiveController
import universum.studios.android.arkhitekton.interaction.Interactor

/**
 * @author Martin Albedinsky
 */
class DefaultDeviceSelectionController internal constructor(builder: Builder) : ReactiveController<Interactor, DeviceSelectionPresenter>(builder), DeviceSelectionController {

    private val context = builder.context
    private val devicesObserver: Observer<List<BluetoothDevice>> = Observer { devices -> getPresenter().onDevicesChanged(devices!!.map { Device(it.name, it.address) }) }
    private val devicesLiveData = builder.devicesLiveData

    override fun startDevicesDiscovery() {
        isActive().let {
            if (BluetoothUtils.isEnalbed(context)) {
                devicesLiveData.observeForever(devicesObserver)
            } else {
                getPresenter().onBluetoothNotEnabled()
            }
        }
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
        lateinit var context: Context
        lateinit var devicesLiveData: LiveBluetoothDevices

        override fun build() = DefaultDeviceSelectionController(this)
    }
}