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
package universum.studios.synergy.prototype.device.headset.neurosky

import android.bluetooth.BluetoothDevice
import android.content.Context
import com.neurosky.AlgoSdk.NskAlgoDataType
import com.neurosky.AlgoSdk.NskAlgoSdk
import com.neurosky.AlgoSdk.NskAlgoType
import com.neurosky.connection.DataType.MindDataType
import com.neurosky.connection.TgStreamHandler
import com.neurosky.connection.TgStreamReader
import universum.studios.synergy.prototype.device.headset.Headset
import universum.studios.synergy.prototype.device.headset.data.AttentionData
import universum.studios.synergy.prototype.util.Logging

/**
 * @author Martin Albedinsky
 */
class NeuroSkyHeadset(private val context: Context, private val bluetoothDevice: BluetoothDevice) : Headset() {

    private val nskAlgoSdk: NskAlgoSdk by lazy { NskAlgoSdk() }
    private var deviceStreamReader: TgStreamReader? = null
    private val deviceStreamHandler: TgStreamHandler = object : TgStreamHandler {

        override fun onStatesChanged(p0: Int) {}

        override fun onChecksumFail(p0: ByteArray?, p1: Int, p2: Int) {}

        override fun onDataReceived(datatype: Int, data: Int, payload: Any?) {
            when (datatype) {
                MindDataType.CODE_ATTENTION -> {
                    val attValue = shortArrayOf(data.toShort())
                    NskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ATT.value, attValue, 1)
                }
                else -> {}
            }
        }

        override fun onRecordFail(p0: Int) {}

    }

    init {
        nskAlgoSdk.apply {
            setOnSignalQualityListener { level -> /* todo: handle signal quality change ... */ }
            setOnStateChangeListener { state, reason -> /* todo: handle state change ... */  }
            setOnAttAlgoIndexListener { attention ->
                Logging.i(name(), "Attention changed to: $attention")
                notifyAttentionChange(AttentionData(attention))
            }
        }
    }

    override fun connect() {
        this.deviceStreamReader = TgStreamReader(bluetoothDevice, deviceStreamHandler)
        this.deviceStreamReader?.startLog()
        this.deviceStreamReader?.connectAndStart()
        NskAlgoSdk.NskAlgoInit(NskAlgoType.NSK_ALGO_TYPE_ATT.value, context.filesDir.absolutePath, "NeuroSky_Release_To_GeneralFreeLicense_Use_Only_Nov 25 2016")
        NskAlgoSdk.NskAlgoStart(false)
    }

    override fun disconnect() {
        this.deviceStreamReader?.stop()
        this.deviceStreamReader?.close()
        this.deviceStreamReader = null
    }
}