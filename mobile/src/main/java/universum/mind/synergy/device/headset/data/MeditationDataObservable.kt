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

import io.reactivex.Observer
import universum.mind.synergy.device.headset.Headset
import universum.mind.synergy.device.headset.OnMeditationListener

/**
 * @author Martin Albedinsky
 */
internal class MeditationDataObservable(headset: Headset) : HeadsetDataObservable<MeditationData>(headset) {

    companion object {

        const val TAG = "MeditationDataObservable"
    }

    override fun onCreateDataDisposable(headset: Headset, observer: Observer<in MeditationData>): DataDisposable<MeditationData> = MeditationDataDisposable(headset, observer)

    private class MeditationDataDisposable(headset: Headset, observer: Observer<in MeditationData>)
        : HeadsetDataObservable.DataDisposable<MeditationData>(TAG, headset, observer), OnMeditationListener {

        override fun onRegisterDataListener(headset: Headset) = headset.registerOnMeditationListener(this)

        override fun onMeditationChanged(data: MeditationData) {
            if (isDisposed) {
                return
            }
            notifyNext(data)
        }

        override fun onUnregisterDataListener(headset: Headset) = headset.unregisterOnMeditationListener(this)
    }
}