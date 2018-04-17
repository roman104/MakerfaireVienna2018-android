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
package universum.studios.synergy.prototype.observation.attention.control

import universum.studios.android.arkhitekton.interaction.Interactor
import universum.studios.synergy.prototype.device.headset.AttentionListener
import universum.studios.synergy.prototype.device.headset.Headset
import universum.studios.synergy.prototype.device.headset.data.AttentionData
import universum.studios.synergy.prototype.observation.attention.view.presentation.AttentionObservationPresenter
import universum.studios.synergy.prototype.observation.control.BaseObservationController

/**
 * @author Martin Albedinsky
 */
class DefaultAttentionObservationController internal constructor(builder: Builder)
    : BaseObservationController<Interactor, AttentionObservationPresenter>(builder), AttentionObservationController {

    private val subjectListener = object : AttentionListener {

        override fun onAttentionChanged(data: AttentionData) = getPresenter().onObservationDataChanged(data)
    }

    override fun onObservationStart(headset: Headset) {
        super.onObservationStart(headset)
        headset.registerAttentionListener(subjectListener)
    }

    override fun onObservationStop(headset: Headset) {
        super.onObservationStop(headset)
        headset.unregisterAttentionListener(subjectListener)
    }
    
    class Builder(
        interactor: Interactor,
        presenter: AttentionObservationPresenter
    ) : BaseObservationController.BaseBuilder<Builder, Interactor, AttentionObservationPresenter>(interactor, presenter) {
    
        override val self = this
        override fun build() = DefaultAttentionObservationController(this)
    }
}