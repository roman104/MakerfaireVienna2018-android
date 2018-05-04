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
import android.content.pm.PackageManager
import universum.mind.synergy.device.Device
import universum.mind.synergy.device.data.LiveBluetoothDevices
import universum.mind.synergy.device.view.DeviceSelectionView
import universum.mind.synergy.device.view.presentation.DeviceSelectionPresenter
import universum.mind.synergy.system.bluetooth.BluetoothError
import universum.mind.synergy.system.bluetooth.BluetoothUtils
import universum.mind.synergy.system.location.LocationError
import universum.mind.synergy.system.location.LocationUtils
import universum.mind.synergy.util.Logging
import universum.studios.android.arkhitekton.control.ReactiveController
import universum.studios.android.arkhitekton.interaction.Interactor
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Martin Albedinsky
 */
class DefaultDeviceSelectionController internal constructor(builder: Builder) : ReactiveController<Interactor, DeviceSelectionPresenter>(builder), DeviceSelectionController {

    companion object {

        private const val TAG = "DefaultDeviceSelectionController"
    }

    private val context = builder.context
    private val devicesObserver: Observer<List<BluetoothDevice>> = Observer { devices -> getPresenter().onDevicesChanged(devices!!.map { Device(it.name, it.address) }) }
    private val devicesLiveData = builder.devicesLiveData
    private val discoveryActive = AtomicBoolean()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            DeviceSelectionView.PERMISSION_REQUEST_BLUETOOTH -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDevicesDiscovery()
                } else {
                    Logging.d(TAG, "Bluetooth permission rejected.")
                }
            }
            DeviceSelectionView.PERMISSION_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDevicesDiscovery()
                } else {
                    Logging.d(TAG, "Location permission rejected.")
                }
            }
        }
    }

    override fun startDevicesDiscovery() {
        if (isActive() && discoveryActive.compareAndSet(false, true)) {
            var startDiscovery = true

            if (BluetoothUtils.isNotEnabled(context)) {
                startDiscovery = false
                getPresenter().onBluetoothError(BluetoothError.NOT_ENABLED)
                // todo: listen for bluetooth enabled state change ...
            }

            if (LocationUtils.hasNotPermission(context)) {
                startDiscovery = false
                getPresenter().onLocationError(LocationError.NOT_PERMITTED)
            }

            startDiscovery.let { devicesLiveData.observeForever(devicesObserver) }
        }

    }

    override fun restartDevicesDiscovery() {
        stopDevicesDiscovery()
        startDevicesDiscovery()
    }

    override fun stopDevicesDiscovery() {
        if (discoveryActive.compareAndSet(true, false)) {
            devicesLiveData.removeObserver(devicesObserver)
        }
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