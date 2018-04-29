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
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import universum.mind.synergy.device.headset.Headset
import universum.studios.android.logging.SimpleLogger
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Martin Albedinsky
 */
abstract class HeadsetDataObservable<D : HeadsetData> internal constructor(internal val headset: Headset) : Observable<D>() {

    companion object {

        internal val LOGGER = SimpleLogger(Log.VERBOSE)

        fun attention(headset: Headset): HeadsetDataObservable<AttentionData> = AttentionDataObservable(headset)

        fun meditation(headset: Headset): HeadsetDataObservable<MeditationData> = MeditationDataObservable(headset)
    }

    override fun subscribeActual(observer: Observer<in D>) {
        val headsetDisposable = onCreateDataDisposable(headset, observer)
        observer.onSubscribe(headsetDisposable)
        headsetDisposable.startListening()
    }

    protected abstract fun onCreateDataDisposable(headset: Headset, observer: Observer<in D>): DataDisposable<D>

    protected abstract class DataDisposable<D : HeadsetData> (
            private val observableName: String,
            private val headset: Headset,
            private val observer: Observer<in D>
    ) : Disposable {

        private val disposed = AtomicBoolean()

        protected fun notifyNext(data: D) {
            this.observer.onNext(data)
        }

        internal fun startListening() {
            onRegisterDataListener(headset)
            this.headset.connect()
            LOGGER.i(observableName, "Started listening ...")
        }

        protected abstract fun onRegisterDataListener(headset: Headset)

        private fun stopListening() {
            onUnregisterDataListener(headset)
            this.headset.disconnect()
            LOGGER.i(observableName, "Stopped listening ...")
        }

        protected abstract fun onUnregisterDataListener(headset: Headset)

        override fun dispose() {
            if (disposed.compareAndSet(false, true)) {
                stopListening()
            }
        }

        override fun isDisposed(): Boolean = disposed.get()
    }
}