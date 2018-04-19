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
package universum.studios.synergy.prototype.device.headset

import universum.studios.synergy.prototype.R
import universum.studios.synergy.prototype.device.headset.data.AttentionData
import universum.studios.synergy.prototype.device.headset.data.MeditationData
import universum.studios.synergy.prototype.util.ListenersRegistry
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Martin Albedinsky
 */
abstract class Headset {

    enum class SignalQuality {
        UNKNOWN, GOOD, MEDIUM, POOR
    }

    enum class ObservationSubject constructor(val id: Long, val flag: Int, val nameRes: Int) {
        UNSPECIFIED(0L, 0, R.string.empty),
        ATTENTION(1L, 0x00000001, R.string.observation_subject_attention),
        MEDITATION(2L, 0x00000001 shl 1, R.string.observation_subject_meditation)
    }

    interface SignalQualityListener {

        fun onSignalQualityChanged(quality: SignalQuality)

        class Registry : ListenersRegistry<SignalQualityListener>() {

            fun notifySignalQualityChange(quality: SignalQuality) {
                listeners.forEach { it.onSignalQualityChanged(quality) }
            }
        }
    }

    private val signalQualityListeners = SignalQualityListener.Registry()
    private val attentionListenerRegistry = AttentionListener.Registry()
    private val meditationListenerRegistry = MeditationListener.Registry()
    private val connected = AtomicBoolean()

    fun name(): String = javaClass.simpleName

    fun registerSignalQualityListener(listener: SignalQualityListener) {
        this.signalQualityListeners.registerListener(listener)
    }

    protected fun notifySignalQualityChange(quality: SignalQuality) {
        this.signalQualityListeners.notifySignalQualityChange(quality)
    }

    fun unregisterSignalQualityListener(listener: SignalQualityListener) {
        this.signalQualityListeners.unregisterListener(listener)
    }

    fun registerAttentionListener(listener: AttentionListener) {
        this.attentionListenerRegistry.registerListener(listener)
    }

    protected fun notifyAttentionChange(data: AttentionData) {
        this.attentionListenerRegistry.notifyAttentionChanged(data)
    }

    fun unregisterAttentionListener(listener: AttentionListener) {
        this.attentionListenerRegistry.unregisterListener(listener)
    }

    fun registerMeditationListener(listener: MeditationListener) {
        this.meditationListenerRegistry.registerListener(listener)
    }

    protected fun notifyMeditationChange(data: MeditationData) {
        this.meditationListenerRegistry.notifyMeditationChanged(data)
    }

    fun unregisterMeditationListener(listener: MeditationListener) {
        this.meditationListenerRegistry.unregisterListener(listener)
    }

    private fun hasRegisteredListeners(): Boolean {
        return attentionListenerRegistry.isNotEmpty() || meditationListenerRegistry.isNotEmpty()
    }

    fun connect() {
        if (connected.get()) {
            return
        }
        onConnect()
        this.connected.set(true)
    }

    abstract fun onConnect()

    fun disconnect() {
        if (hasRegisteredListeners()) {
            return
        }
        if (connected.get()) {
            onDisconnect()
            this.connected.set(false)
        }
    }

    abstract fun onDisconnect()
}