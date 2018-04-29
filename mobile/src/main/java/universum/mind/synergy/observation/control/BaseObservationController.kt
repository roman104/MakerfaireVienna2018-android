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
package universum.mind.synergy.observation.control

import universum.mind.synergy.device.headset.Headset
import universum.mind.synergy.observation.view.presentation.ObservationPresenter
import universum.studios.android.arkhitekton.control.ReactiveController
import universum.studios.android.arkhitekton.interaction.Interactor
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Martin Albedinsky
 */
abstract class BaseObservationController<out I : Interactor, out P : ObservationPresenter<*, *>> protected constructor(builder: BaseBuilder<*, I, P>)
    : ReactiveController<I, P>(builder), ObservationController<P> {

    private val headset = builder.headset
    private val observing = AtomicBoolean()

    override fun startObservation() {
        if (observing.compareAndSet(false, true)) {
            onObservationStart(headset)
        }
    }

    protected open fun onObservationStart(headset: Headset) {}

    override fun stopObservation() {
        if (observing.compareAndSet(true, false)) {
            onObservationStop(headset)
        }
    }

    protected open fun onObservationStop(headset: Headset) {}

    override fun onDeactivated() {
        super.onDeactivated()
        stopObservation()
    }

    abstract class BaseBuilder<B : BaseBuilder<B, I, P>, I : Interactor, P : ObservationPresenter<*, *>>
    protected constructor(interactor: I, presenter: P) : ReactiveController.BaseBuilder<B, I, P>(interactor, presenter) {

        lateinit var headset: Headset
    }
}