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
package universum.studios.mindwave.prototype.welcome.control

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import universum.studios.android.arkhitekton.control.ReactiveController
import universum.studios.android.arkhitekton.interaction.Interactor
import universum.studios.mindwave.prototype.util.Logging
import universum.studios.mindwave.prototype.welcome.view.presentation.ParticipantsPresenter

/**
 * @author Martin Albedinsky
 */
class DefaultParticipantsController internal constructor(builder: Builder) : ReactiveController<Interactor, ParticipantsPresenter>(builder), ParticipantsController {

    companion object {

        @Suppress("unused") const val TAG = "DefaultParticipantsController"
    }

    private val context = builder.context
    private val bluetoothAdapter = builder.bluetoothAdapter
    private var devicesReceiver: DevicesReceiver? = null
    private val foundDevicesMap = HashMap<String, BluetoothDevice>()

    override fun startDevicesScan() {
        if (isActive() && bluetoothAdapter != null) {
            if (devicesReceiver == null) {
                this.devicesReceiver = DevicesReceiver()
                this.context.registerReceiver(devicesReceiver, devicesReceiver!!.createIntentFilter())
                this.bluetoothAdapter.startDiscovery()
            }
        }
    }

    override fun restartDevicesScan() {
        this.stopDevicesScan()
        this.foundDevicesMap.clear()
        this.dispatchDevicesChanged()
        this.startDevicesScan()
    }

    override fun stopDevicesScan() {
        if (devicesReceiver != null) {
            this.context.unregisterReceiver(devicesReceiver)
            this.devicesReceiver = null
            this.bluetoothAdapter!!.cancelDiscovery()
        }
    }

    override fun onDeactivated() {
        super.onDeactivated()
        stopDevicesScan()
    }

    internal fun onDeviceFound(device: BluetoothDevice) {
        foundDevicesMap[device.address] = device
        this.dispatchDevicesChanged()
    }

    internal fun onDeviceUpdated(device: BluetoothDevice) {
        foundDevicesMap[device.address] = device
        this.dispatchDevicesChanged()
    }

    private fun dispatchDevicesChanged() {
        getPresenter().onAvailableDevicesChanged(ArrayList(foundDevicesMap.values))
    }
    
    class Builder(
        interactor: Interactor,
        presenter: ParticipantsPresenter
    ) : ReactiveController.BaseBuilder<Builder, Interactor, ParticipantsPresenter>(interactor, presenter) {

        override val self = this
        lateinit var context: Context
        var bluetoothAdapter: BluetoothAdapter? = null

        override fun build() = DefaultParticipantsController(this)
    }

    private inner class DevicesReceiver : BroadcastReceiver() {

        fun createIntentFilter() = IntentFilter(BluetoothDevice.ACTION_FOUND).apply {
            addAction(BluetoothDevice.ACTION_NAME_CHANGED)
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        }

        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    Logging.d(TAG, "Found device(name: ${device.name}, address: ${device.address}).")
                    this@DefaultParticipantsController.onDeviceFound(device)
                }
                BluetoothDevice.ACTION_NAME_CHANGED -> {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    Logging.d(TAG, "Device changed(name: ${device.name}, address: ${device.address}).")
                    this@DefaultParticipantsController.onDeviceUpdated(device)
                }
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    Logging.d(TAG, "Device bond state changed(name: ${device.name}, address: ${device.address}).")
                }
                else -> {}
            }
        }
    }
}