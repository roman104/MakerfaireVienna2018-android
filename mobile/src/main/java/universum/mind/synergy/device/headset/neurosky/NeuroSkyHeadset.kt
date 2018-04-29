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
import com.neurosky.AlgoSdk.NskAlgoState
import com.neurosky.AlgoSdk.NskAlgoType
import com.neurosky.connection.ConnectionStates
import com.neurosky.connection.DataType.MindDataType
import com.neurosky.connection.TgStreamHandler
import com.neurosky.connection.TgStreamReader
import universum.mind.synergy.device.headset.Headset
import universum.mind.synergy.device.headset.data.AttentionData
import universum.mind.synergy.device.headset.data.MeditationData
import universum.studios.android.logging.SimpleLogger

/**
 * @author Martin Albedinsky
 */
class NeuroSkyHeadset(private val context: Context, private val bluetoothDevice: BluetoothDevice) : Headset() {

    companion object {

        val LOGGER = SimpleLogger(Log.VERBOSE)
    }

    private val nskAlgoSdk: NskAlgoSdk by lazy { NskAlgoSdk() }
    private var deviceStreamReader: TgStreamReader? = null
    private val deviceStreamHandler: TgStreamHandler = object : TgStreamHandler {

        override fun onStatesChanged(state: Int) {
            when (state) {
                ConnectionStates.STATE_INIT -> LOGGER.d(name(), "Initiated")
                ConnectionStates.STATE_CONNECTING -> {
                    LOGGER.d(name(), "Connecting")
                    notifyConnectionStateChange(ConnectionState.CONNECTING)
                }
                ConnectionStates.STATE_CONNECTED -> {
                    LOGGER.d(name(), "Connected")
                    notifyConnectionStateChange(ConnectionState.CONNECTED)
                }
                ConnectionStates.STATE_WORKING -> LOGGER.d(name(), "Working")
                ConnectionStates.STATE_STOPPED -> LOGGER.d(name(), "Stopped")
                ConnectionStates.STATE_DISCONNECTED -> {
                    LOGGER.d(name(), "Disconnected")
                    notifyConnectionStateChange(ConnectionState.DISCONNECTED)
                }
                ConnectionStates.STATE_COMPLETE -> LOGGER.d(name(), "Completed")
                ConnectionStates.STATE_RECORDING_START -> LOGGER.d(name(), "Recording started")
                ConnectionStates.STATE_RECORDING_END -> LOGGER.d(name(), "Recording stopped")
                ConnectionStates.STATE_GET_DATA_TIME_OUT -> LOGGER.d(name(), "Data retrieval time out")
                ConnectionStates.STATE_FAILED,
                ConnectionStates.STATE_ERROR -> {
                    LOGGER.d(name(), "Failed")
                    notifyConnectionStateChange(ConnectionState.DISCONNECTED)
                }
            }
        }

        override fun onChecksumFail(p0: ByteArray?, p1: Int, p2: Int) {
            LOGGER.d(name(), "Checksum fail: ???")
        }

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
            setOnStateChangeListener { state, _ ->
                LOGGER.d(name(), "State changed to: $state")
                when (state) {
                    NskAlgoState.NSK_ALGO_STATE_INITED.value -> {}
                }
            }
            setOnAttAlgoIndexListener { attention ->
                LOGGER.i(name(), "Attention changed to: $attention")
                notifyAttentionChange(AttentionData.Builder().apply {
                    deviceAddress = bluetoothDevice.address
                    subject = ObservationSubject.ATTENTION
                    value = attention
                }.build())
            }
            setOnMedAlgoIndexListener{ meditation ->
                LOGGER.i(name(), "Meditation changed to: $meditation")
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