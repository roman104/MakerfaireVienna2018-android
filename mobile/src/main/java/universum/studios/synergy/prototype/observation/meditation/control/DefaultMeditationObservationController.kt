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
package universum.studios.synergy.prototype.observation.meditation.control

import universum.studios.android.arkhitekton.interaction.Interactor
import universum.studios.synergy.prototype.device.headset.Headset
import universum.studios.synergy.prototype.device.headset.MeditationListener
import universum.studios.synergy.prototype.device.headset.data.MeditationData
import universum.studios.synergy.prototype.observation.control.BaseObservationController
import universum.studios.synergy.prototype.observation.meditation.view.presentation.MeditationObservationPresenter

/**
 * @author Martin Albedinsky
 */
class DefaultMeditationObservationController internal constructor(builder: Builder)
    : BaseObservationController<Interactor, MeditationObservationPresenter>(builder), MeditationObservationController {

    private val subjectListener = object : MeditationListener {

        override fun onMeditationChanged(data: MeditationData) = getPresenter().onObservationDataChanged(data)
    }

    override fun onObservationStart(headset: Headset) {
        super.onObservationStart(headset)
        headset.registerMeditationListener(subjectListener)
    }

    override fun onObservationStop(headset: Headset) {
        super.onObservationStop(headset)
        headset.unregisterMeditationListener(subjectListener)
    }

    class Builder(
        interactor: Interactor,
        presenter: MeditationObservationPresenter
    ) : BaseObservationController.BaseBuilder<Builder, Interactor, MeditationObservationPresenter>(interactor, presenter) {
    
        override val self = this
        override fun build() = DefaultMeditationObservationController(this)
    }
}