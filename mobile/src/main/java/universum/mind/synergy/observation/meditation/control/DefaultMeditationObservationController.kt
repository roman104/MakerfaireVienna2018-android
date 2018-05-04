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
package universum.mind.synergy.observation.meditation.control

import io.reactivex.disposables.Disposable
import universum.mind.synergy.device.headset.Headset
import universum.mind.synergy.device.headset.data.HeadsetDataObservable
import universum.mind.synergy.observation.control.BaseObservationController
import universum.mind.synergy.observation.meditation.view.presentation.MeditationObservationPresenter
import universum.studios.android.arkhitekton.interaction.Interactor

/**
 * @author Martin Albedinsky
 */
class DefaultMeditationObservationController internal constructor(builder: Builder)
    : BaseObservationController<Interactor, MeditationObservationPresenter>(builder), MeditationObservationController {

    private var observationDisposable: Disposable? = null

    override fun onObservationStart(headset: Headset) {
        super.onObservationStart(headset)
        this.observationDisposable = HeadsetDataObservable.meditation(headset)
                .observeOn(presentationScheduler)
                .subscribeOn(interactionScheduler)
                .subscribe(getPresenter()::onObservationDataChanged)
    }

    override fun onObservationStop(headset: Headset) {
        super.onObservationStop(headset)
        this.observationDisposable?.dispose()
        this.observationDisposable = null
    }

    class Builder(
        interactor: Interactor,
        presenter: MeditationObservationPresenter
    ) : BaseObservationController.BaseBuilder<Builder, Interactor, MeditationObservationPresenter>(interactor, presenter) {
    
        override val self = this
        override fun build() = DefaultMeditationObservationController(this)
    }
}