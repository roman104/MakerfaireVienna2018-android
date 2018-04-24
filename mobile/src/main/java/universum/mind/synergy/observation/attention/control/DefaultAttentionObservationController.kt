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
package universum.mind.synergy.observation.attention.control

import io.reactivex.disposables.Disposable
import universum.studios.android.arkhitekton.interaction.Interactor
import universum.mind.synergy.device.headset.Headset
import universum.mind.synergy.device.headset.data.HeadsetDataObservable
import universum.mind.synergy.observation.attention.view.presentation.AttentionObservationPresenter
import universum.mind.synergy.observation.control.BaseObservationController

/**
 * @author Martin Albedinsky
 */
class DefaultAttentionObservationController internal constructor(builder: Builder)
    : BaseObservationController<Interactor, AttentionObservationPresenter>(builder), AttentionObservationController {

    private var attentionDisposable: Disposable? = null

    override fun onObservationStart(headset: Headset) {
        super.onObservationStart(headset)
        this.attentionDisposable = HeadsetDataObservable.attention(headset)
                .observeOn(presentationScheduler)
                .subscribeOn(interactionScheduler)
                .subscribe(getPresenter()::onObservationDataChanged)
    }

    override fun onObservationStop(headset: Headset) {
        super.onObservationStop(headset)
        this.attentionDisposable?.dispose()
        this.attentionDisposable = null
    }
    
    class Builder(
        interactor: Interactor,
        presenter: AttentionObservationPresenter
    ) : BaseObservationController.BaseBuilder<Builder, Interactor, AttentionObservationPresenter>(interactor, presenter) {
    
        override val self = this
        override fun build() = DefaultAttentionObservationController(this)
    }
}