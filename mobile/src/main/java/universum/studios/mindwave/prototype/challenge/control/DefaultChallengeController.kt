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
package universum.studios.mindwave.prototype.challenge.control

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.neurosky.AlgoSdk.NskAlgoDataType
import com.neurosky.AlgoSdk.NskAlgoSdk
import com.neurosky.AlgoSdk.NskAlgoType
import com.neurosky.connection.DataType.MindDataType
import com.neurosky.connection.TgStreamHandler
import com.neurosky.connection.TgStreamReader
import universum.studios.android.arkhitekton.control.ReactiveController
import universum.studios.android.arkhitekton.interaction.Interactor
import universum.studios.mindwave.prototype.challenge.ChallengeSession
import universum.studios.mindwave.prototype.challenge.view.presentation.ChallengePresenter
import universum.studios.mindwave.prototype.util.Logging
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Martin Albedinsky
 */
class DefaultChallengeController internal constructor(builder: Builder) : ReactiveController<Interactor, ChallengePresenter>(builder), ChallengeController {

    companion object {
    
        @Suppress("unused") const val TAG = "DefaultChallengeController"
    }

    private val session = builder.session
    private val context = builder.context
    private val bluetoothAdapter = builder.bluetoothAdapter
    private val challengeRunning = AtomicBoolean()
    private val nskAlgoSdk: NskAlgoSdk by lazy { NskAlgoSdk().apply {
        setOnSignalQualityListener { level -> /* todo: handle signal quality change ... */ }
        setOnStateChangeListener { state, reason -> /* todo: handle state change ... */  }
        setOnAttAlgoIndexListener { attention ->
            val participantDataChange = participantsDataChanges.poll()
            if (participantDataChange != null) {
                if (participantDataChange.firstParticipant) {
                    Logging.i(TAG, "Attention for FIRST participant changed to: $attention")
                    getPresenter().onFirstParticipantAttentionChanged(attention)
                } else {
                    Logging.i(TAG, "Attention for SECOND participant changed to: $attention")

                }
            } else {
                Logging.i(TAG, "Attention for UNKNOWN participant changed to: $attention")
            }
        }
    }}
    private val participantsDataChanges: Queue<ParticipantDataChange> = LinkedBlockingQueue()
    private var firstParticipantStreamReader: TgStreamReader? = null
    private val firstParticipantStreamHandler: TgStreamHandler = object : TgStreamHandler {

        override fun onStatesChanged(p0: Int) {}

        override fun onChecksumFail(p0: ByteArray?, p1: Int, p2: Int) {}

        override fun onDataReceived(datatype: Int, data: Int, payload: Any?) {
            when (datatype) {
                MindDataType.CODE_ATTENTION -> {
                    participantsDataChanges.add(ParticipantDataChange(true, data))
                    val attValue = shortArrayOf(data.toShort())
                    NskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ATT.value, attValue, 1)
                }
                else -> {}
            }
        }

        override fun onRecordFail(p0: Int) {}

    }
    private var secondParticipantStreamReader: TgStreamReader? = null
    private val secondParticipantStreamHandler: TgStreamHandler = object : TgStreamHandler {

        override fun onStatesChanged(p0: Int) {}

        override fun onChecksumFail(p0: ByteArray?, p1: Int, p2: Int) {}

        override fun onDataReceived(datatype: Int, data: Int, payload: Any?) {
            when (datatype) {
                MindDataType.CODE_ATTENTION -> {
                    participantsDataChanges.add(ParticipantDataChange(false, data))
                    val attValue = shortArrayOf(data.toShort())
                    NskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ATT.value, attValue, 1)
                }
                else -> {}
            }
        }

        override fun onRecordFail(p0: Int) {}
    }

    override fun onActivated() {
        super.onActivated()
        nskAlgoSdk.hashCode()
    }

    override fun startChallenge() {
        if (challengeRunning.get() || bluetoothAdapter == null) {
            return
        }
        val firstParticipantDevice = bluetoothAdapter.getRemoteDevice(session.getFirstParticipantDeviceAddress()) ?: return
        val secondParticipantDevice = bluetoothAdapter.getRemoteDevice(session.getSecondParticipantDeviceAddress()) ?: return

        this.firstParticipantStreamReader = TgStreamReader(firstParticipantDevice, firstParticipantStreamHandler)
        this.firstParticipantStreamReader?.startLog()
        this.firstParticipantStreamReader?.connectAndStart()

        this.secondParticipantStreamReader = TgStreamReader(secondParticipantDevice, secondParticipantStreamHandler)
        this.secondParticipantStreamReader?.startLog()
        this.secondParticipantStreamReader?.connectAndStart()

        NskAlgoSdk.NskAlgoInit(NskAlgoType.NSK_ALGO_TYPE_ATT.value, context.filesDir.absolutePath, "NeuroSky_Release_To_GeneralFreeLicense_Use_Only_Nov 25 2016")
        NskAlgoSdk.NskAlgoStart(false)
        this.challengeRunning.set(true)
    }

    override fun stopChallenge() {
        if (challengeRunning.get()) {
            NskAlgoSdk.NskAlgoStop()
            this.firstParticipantStreamReader?.stop()
            this.firstParticipantStreamReader?.close()
            this.firstParticipantStreamReader = null
            this.secondParticipantStreamReader?.stop()
            this.secondParticipantStreamReader?.close()
            this.secondParticipantStreamReader = null
            this.challengeRunning.set(false)
        }
    }

    override fun onDeactivated() {
        super.onDeactivated()
        stopChallenge()
    }
    
    class Builder(
        interactor: Interactor,
        presenter: ChallengePresenter
    ) : ReactiveController.BaseBuilder<Builder, Interactor, ChallengePresenter>(interactor, presenter) {
    
        override val self = this
        lateinit var session: ChallengeSession
        lateinit var context: Context
        var bluetoothAdapter: BluetoothAdapter? = null

        override fun build() = DefaultChallengeController(this)
    }

    private class ParticipantDataChange(val firstParticipant: Boolean, val data: Int)
}