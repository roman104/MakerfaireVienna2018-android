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
package universum.mind.synergy.observation.meditation.view

import android.content.Context
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_observation_attention.*
import universum.studios.android.support.fragment.annotation.ContentView
import universum.mind.synergy.R
import universum.mind.synergy.databinding.FragmentObservationMeditationBinding
import universum.mind.synergy.device.headset.Headset
import universum.mind.synergy.observation.meditation.control.MeditationObservationController
import universum.mind.synergy.observation.view.BaseObservationFragment

/**
 * @author Martin Albedinsky
 */
@ContentView(R.layout.fragment_observation_meditation)
class MeditationObservationFragment : BaseObservationFragment<MeditationObservationViewModel, MeditationObservationController>() {

    companion object {

        fun newInstance() = MeditationObservationFragment().apply {
            arguments = BaseObservationFragment.createArguments(Headset.ObservationSubject.MEDITATION)
        }
    }

    private lateinit var binding: FragmentObservationMeditationBinding

    override fun onAttach(context: Context) {
        requestFeature(FEATURE_INJECTION_BASIC)
        super.onAttach(context)
    }

    override fun onBindViews(rootView: View, savedInstanceState: Bundle?) {
        super.onBindViews(rootView, savedInstanceState)
        this.binding = FragmentObservationMeditationBinding.bind(rootView).apply {
            viewModel = super.getViewModel()
        }
        this.chart_view.data = getViewModel().chartData.get()
    }
}