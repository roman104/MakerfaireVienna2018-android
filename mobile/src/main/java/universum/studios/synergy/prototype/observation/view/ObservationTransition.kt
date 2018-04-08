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
package universum.studios.synergy.prototype.observation.view

import android.app.Activity
import android.support.v4.app.Fragment
import universum.studios.android.transition.NavigationalTransitionCompat
import universum.studios.android.transition.WindowTransitions

/**
 * @author Martin Albedinsky
 */
class ObservationTransition private constructor() : NavigationalTransitionCompat(ObservationActivity::class.java) {

    companion object {

        private val WINDOW_TRANSITION = WindowTransitions.SLIDE_TO_TOP_AND_SCALE_OUT

        fun get() = ObservationTransition()
    }

    override fun start(caller: Fragment) = start(caller.requireActivity())

    override fun onStart(caller: Activity) {
        caller.startActivity(createIntent(caller))
        WINDOW_TRANSITION.overrideStart(caller)
    }

    override fun onFinish(caller: Activity) {
        caller.finish()
        WINDOW_TRANSITION.overrideFinish(caller)
    }
}