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
package universum.mind.synergy.device.data

import android.arch.lifecycle.LiveData
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import universum.mind.synergy.util.Logging

/**
 * @author Martin Albedinsky
 */
class LiveBluetoothDevices private constructor(private val context: Context) : LiveData<List<BluetoothDevice>>() {

    companion object {

        internal const val TAG = "LiveBluetoothDevices"

        fun create(context: Context): LiveBluetoothDevices = LiveBluetoothDevices(context.applicationContext)
    }

    private var bluetoothAdapter: BluetoothAdapter? = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    private var devicesReceiver: DevicesReceiver? = null
    private val foundDevicesMap = HashMap<String, BluetoothDevice>()

    override fun onActive() {
        bluetoothAdapter?.let {
            if (devicesReceiver == null) {
                this.devicesReceiver = DevicesReceiver()
                this.context.registerReceiver(devicesReceiver, devicesReceiver!!.createIntentFilter())
                this.bluetoothAdapter?.startDiscovery()
            }
        }
    }

    internal fun onDeviceFound(device: BluetoothDevice) {
        foundDevicesMap[device.address] = device
        this.notifyDevicesChanged()
    }

    internal fun onDeviceUpdated(device: BluetoothDevice) {
        foundDevicesMap[device.address] = device
        this.notifyDevicesChanged()
    }

    private fun notifyDevicesChanged() = postValue(ArrayList(foundDevicesMap.values))

    override fun onInactive() {
        if (devicesReceiver != null) {
            this.context.unregisterReceiver(devicesReceiver)
            this.devicesReceiver = null
            this.bluetoothAdapter?.cancelDiscovery()
        }
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
                    this@LiveBluetoothDevices.onDeviceFound(device)
                }
                BluetoothDevice.ACTION_NAME_CHANGED -> {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    Logging.d(TAG, "Device changed(name: ${device.name}, address: ${device.address}).")
                    this@LiveBluetoothDevices.onDeviceUpdated(device)
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