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
package universum.mind.synergy.device.headset

import universum.mind.synergy.R
import universum.mind.synergy.device.headset.data.AttentionData
import universum.mind.synergy.device.headset.data.MeditationData
import universum.mind.synergy.util.ListenersRegistry
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Martin Albedinsky
 */
abstract class Headset {

    enum class ConnectionState {
        IDLE, CONNECTING, CONNECTED, DISCONNECTED
    }

    enum class SignalQuality {
        UNKNOWN, GOOD, MEDIUM, POOR
    }

    enum class ObservationSubject constructor(val id: Long, val flag: Int, val nameRes: Int) {
        UNSPECIFIED(0L, 0, R.string.empty),
        ATTENTION(1L, 0x00000001, R.string.observation_subject_attention),
        MEDITATION(2L, 0x00000001 shl 1, R.string.observation_subject_meditation)
    }

    interface OnConnectionListener {

        fun onConnectionStateChanged(headset: Headset, state: ConnectionState)

        class Registry : ListenersRegistry<OnConnectionListener>() {

            fun notifyConnectionStateChange(headset: Headset, state: ConnectionState) {
                listeners.forEach { it.onConnectionStateChanged(headset, state) }
            }
        }
    }

    interface OnSignalQualityListener {

        fun onSignalQualityChanged(headset: Headset, quality: SignalQuality)

        class Registry : ListenersRegistry<OnSignalQualityListener>() {

            fun notifySignalQualityChange(headset: Headset, quality: SignalQuality) {
                listeners.forEach { it.onSignalQualityChanged(headset, quality) }
            }
        }
    }

    private val connectionListenersRegistry = OnConnectionListener.Registry()
    private val signalQualityListenersRegistry = OnSignalQualityListener.Registry()
    private val attentionListenerRegistry = OnAttentionListener.Registry()
    private val meditationListenerRegistry = OnMeditationListener.Registry()
    private val connected = AtomicBoolean()

    fun name(): String = javaClass.simpleName

    abstract fun getDeviceName(): String

    fun registerOnConnectionListener(listener: OnConnectionListener) {
        this.connectionListenersRegistry.registerListener(listener)
    }

    protected fun notifyConnectionStateChange(state: ConnectionState) {
        this.connectionListenersRegistry.notifyConnectionStateChange(this, state)
    }

    fun unregisterOnConnectionListener(listener: OnConnectionListener) {
        this.connectionListenersRegistry.unregisterListener(listener)
    }

    fun registerOnSignalQualityListener(listener: OnSignalQualityListener) {
        this.signalQualityListenersRegistry.registerListener(listener)
    }

    protected fun notifySignalQualityChange(quality: SignalQuality) {
        this.signalQualityListenersRegistry.notifySignalQualityChange(this, quality)
    }

    fun unregisterOnSignalQualityListener(listener: OnSignalQualityListener) {
        this.signalQualityListenersRegistry.unregisterListener(listener)
    }

    fun registerOnAttentionListener(listener: OnAttentionListener) {
        this.attentionListenerRegistry.registerListener(listener)
    }

    protected fun notifyAttentionChange(data: AttentionData) {
        this.attentionListenerRegistry.notifyChange(data)
    }

    fun unregisterOnAttentionListener(listener: OnAttentionListener) {
        this.attentionListenerRegistry.unregisterListener(listener)
    }

    fun registerOnMeditationListener(listener: OnMeditationListener) {
        this.meditationListenerRegistry.registerListener(listener)
    }

    protected fun notifyMeditationChange(data: MeditationData) {
        this.meditationListenerRegistry.notifyChange(data)
    }

    fun unregisterOnMeditationListener(listener: OnMeditationListener) {
        this.meditationListenerRegistry.unregisterListener(listener)
    }

    private fun hasRegisteredListeners(): Boolean {
        return attentionListenerRegistry.isNotEmpty() || meditationListenerRegistry.isNotEmpty()
    }

    fun connect() {
        if (connected.compareAndSet(false, true)) {
            onConnect()
        }
    }

    abstract fun onConnect()

    fun disconnect() {
        if (hasRegisteredListeners()) {
            return
        }
        if (connected.compareAndSet(true, false)) {
            onDisconnect()
        }
    }

    abstract fun onDisconnect()
}