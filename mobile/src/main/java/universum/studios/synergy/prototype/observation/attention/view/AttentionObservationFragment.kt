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
package universum.studios.synergy.prototype.observation.attention.view

import android.content.Context
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_observation_attention.*
import universum.studios.android.support.fragment.annotation.ContentView
import universum.studios.synergy.prototype.R
import universum.studios.synergy.prototype.databinding.FragmentObservationAttentionBinding
import universum.studios.synergy.prototype.device.headset.Headset
import universum.studios.synergy.prototype.observation.attention.control.AttentionObservationController
import universum.studios.synergy.prototype.observation.view.BaseObservationFragment

/**
 * @author Martin Albedinsky
 */
@ContentView(R.layout.fragment_observation_attention)
class AttentionObservationFragment : BaseObservationFragment<AttentionObservationViewModel, AttentionObservationController>() {

    companion object {

        fun newInstance() = AttentionObservationFragment().apply {
            arguments = BaseObservationFragment.createArguments(Headset.ObservationSubject.ATTENTION)
        }
    }

    private lateinit var binding: FragmentObservationAttentionBinding

    override fun onAttach(context: Context) {
        requestFeature(FEATURE_INJECTION_BASIC)
        super.onAttach(context)
    }

    override fun onBindViews(rootView: View, savedInstanceState: Bundle?) {
        super.onBindViews(rootView, savedInstanceState)
        this.binding = FragmentObservationAttentionBinding.bind(rootView).apply {
            viewModel = super.getViewModel()
        }
        this.chart_view.data = getViewModel().chartData.get()
    }
}