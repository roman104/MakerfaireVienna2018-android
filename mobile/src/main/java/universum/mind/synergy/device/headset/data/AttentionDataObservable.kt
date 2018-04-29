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
package universum.mind.synergy.device.headset.data

import android.util.Log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import universum.mind.synergy.device.headset.AttentionListener
import universum.mind.synergy.device.headset.Headset
import universum.studios.android.logging.SimpleLogger
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Martin Albedinsky
 */
internal class AttentionDataObservable(headset: Headset) : HeadsetDataObservable<AttentionData>(headset) {

    companion object {

        const val TAG = "AttentionDataObservable"
        val LOGGER = SimpleLogger(Log.VERBOSE)
    }

    override fun subscribeActual(observer: Observer<in AttentionData>) {
        val headsetDisposable = AttentionDataDisposable(headset, observer)
        observer.onSubscribe(headsetDisposable)
        headsetDisposable.startListening()
    }

    private class AttentionDataDisposable(val headset: Headset, val observer: Observer<in AttentionData>) : AttentionListener, Disposable {

        private val disposed = AtomicBoolean()

        fun startListening() {
            this.headset.registerAttentionListener(this)
            this.headset.connect()
            LOGGER.i(TAG, "Started listening ...")
        }

        override fun onAttentionChanged(data: AttentionData) {
            if (isDisposed) {
                return
            }
            this.observer.onNext(data)
        }

        fun stopListening() {
            this.headset.unregisterAttentionListener(this)
            this.headset.disconnect()
            LOGGER.i(TAG, "Stopped listening ...")
        }

        override fun dispose() {
            if (disposed.compareAndSet(false, true)) {
                stopListening()
            }
        }

        override fun isDisposed(): Boolean = disposed.get()
    }
}