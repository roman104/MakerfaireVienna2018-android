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
package universum.mind.synergy.welcome.view

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_welcome.*
import universum.mind.synergy.R
import universum.mind.synergy.observation.view.ObservationTransition
import universum.mind.synergy.view.BaseFragment
import universum.studios.android.arkhitekton.control.Controller
import universum.studios.android.arkhitekton.view.ViewModel
import universum.studios.android.support.fragment.annotation.ContentView

/**
 * @author Martin Albedinsky
 */
@ContentView(R.layout.fragment_welcome)
class WelcomeFragment : BaseFragment<ViewModel, Controller<*>>() {

    override fun onBindViews(rootView: View, savedInstanceState: Bundle?) {
        super.onBindViews(rootView, savedInstanceState)
        // todo: choose either single or combined observation ...
        this.observation.setOnClickListener { ObservationTransition.get().start(this) }
    }
}