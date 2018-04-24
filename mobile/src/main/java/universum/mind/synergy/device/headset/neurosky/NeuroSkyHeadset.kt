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
package universum.mind.synergy.device.headset.neurosky

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import com.neurosky.AlgoSdk.NskAlgoDataType
import com.neurosky.AlgoSdk.NskAlgoSdk
import com.neurosky.AlgoSdk.NskAlgoSignalQuality
import com.neurosky.AlgoSdk.NskAlgoType
import com.neurosky.connection.DataType.MindDataType
import com.neurosky.connection.TgStreamHandler
import com.neurosky.connection.TgStreamReader
import universum.studios.android.logging.SimpleLogger
import universum.mind.synergy.device.headset.Headset
import universum.mind.synergy.device.headset.data.AttentionData
import universum.mind.synergy.device.headset.data.MeditationData

/**
 * @author Martin Albedinsky
 */
class NeuroSkyHeadset(private val context: Context, private val bluetoothDevice: BluetoothDevice) : Headset() {

    companion object {

        private val logger = SimpleLogger(Log.ASSERT)
    }

    private val nskAlgoSdk: NskAlgoSdk by lazy { NskAlgoSdk() }
    private var deviceStreamReader: TgStreamReader? = null
    private val deviceStreamHandler: TgStreamHandler = object : TgStreamHandler {

        override fun onStatesChanged(p0: Int) {}

        override fun onChecksumFail(p0: ByteArray?, p1: Int, p2: Int) {}

        override fun onDataReceived(datatype: Int, data: Int, payload: Any?) {
            when (datatype) {
                MindDataType.CODE_ATTENTION -> {
                    NskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ATT.value, shortArrayOf(data.toShort()), 1)
                }
                MindDataType.CODE_MEDITATION -> {
                    NskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_MED.value, shortArrayOf(data.toShort()), 1)
                }
                else -> {}
            }
        }

        override fun onRecordFail(p0: Int) {}

    }

    init {
        nskAlgoSdk.apply {
            setOnSignalQualityListener { level ->
                when (NskAlgoSignalQuality.values()[level]) {
                    NskAlgoSignalQuality.NSK_ALGO_SQ_GOOD -> notifySignalQualityChange(SignalQuality.GOOD)
                    NskAlgoSignalQuality.NSK_ALGO_SQ_MEDIUM-> notifySignalQualityChange(SignalQuality.MEDIUM)
                    NskAlgoSignalQuality.NSK_ALGO_SQ_POOR-> notifySignalQualityChange(SignalQuality.POOR)
                    NskAlgoSignalQuality.NSK_ALGO_SQ_NOT_DETECTED-> notifySignalQualityChange(SignalQuality.UNKNOWN)
                }
            }
            setOnStateChangeListener { state, reason -> /* todo: handle state change ... */  }
            setOnAttAlgoIndexListener { attention ->
                logger.i(name(), "Attention changed to: $attention")
                notifyAttentionChange(AttentionData.Builder().apply {
                    deviceAddress = bluetoothDevice.address
                    subject = ObservationSubject.ATTENTION
                    value = attention
                }.build())
            }
            setOnMedAlgoIndexListener{ meditation ->
                logger.i(name(), "Meditation changed to: $meditation")
                notifyMeditationChange(MeditationData.Builder().apply {
                    deviceAddress = bluetoothDevice.address
                    subject = ObservationSubject.MEDITATION
                    value = meditation
                }.build())
            }
        }
    }

    override fun onConnect() {
        NskAlgoSdk.NskAlgoInit(
                NskAlgoType.NSK_ALGO_TYPE_F.value + NskAlgoType.NSK_ALGO_TYPE_ATT.value + NskAlgoType.NSK_ALGO_TYPE_MED.value,
                context.filesDir.absolutePath,
                "NeuroSky_Release_To_GeneralFreeLicense_Use_Only_Nov 25 2016"
        )
        NskAlgoSdk.NskAlgoStart(false)
        this.deviceStreamReader = TgStreamReader(bluetoothDevice, deviceStreamHandler)
        this.deviceStreamReader?.startLog()
        this.deviceStreamReader?.connectAndStart()
    }

    override fun onDisconnect() {
        this.deviceStreamReader?.stop()
        this.deviceStreamReader?.close()
        this.deviceStreamReader = null
    }
}