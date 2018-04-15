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
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_observation_attention.*
import universum.studios.android.support.fragment.annotation.ContentView
import universum.studios.android.util.BundleKey
import universum.studios.synergy.prototype.R
import universum.studios.synergy.prototype.device.Device
import universum.studios.synergy.prototype.observation.attention.control.AttentionObservationController
import universum.studios.synergy.prototype.view.BaseFragment

/**
 * @author Martin Albedinsky
 */
@ContentView(R.layout.fragment_observation_attention)
class AttentionObservationFragment : BaseFragment<AttentionObservationViewModel, AttentionObservationController>() {

    companion object {

        val ARGUMENT_DEVICE = BundleKey.argument(AttentionObservationFragment::class.java, "Device")

        fun newInstance(device: Device) = AttentionObservationFragment().apply {
            arguments = Bundle().apply { putParcelable(ARGUMENT_DEVICE, device) }
        }
    }

    override fun onAttach(context: Context) {
        requestFeature(FEATURE_INJECTION_BASIC)
        super.onAttach(context)
    }

    override fun onBindViews(rootView: View, savedInstanceState: Bundle?) {
        super.onBindViews(rootView, savedInstanceState)
        val entries = ArrayList<Entry>()
        entries.add(Entry(0f, 100f))
        entries.add(Entry(50f, 200f))
        entries.add(Entry(100f, 150f))
        val dataSet = LineDataSet(entries, "Values")
        this.chart_view.data = LineData(dataSet)
    }

    override fun onStart() {
        super.onStart()
        getController().startObservation()
    }

    override fun onStop() {
        super.onStop()
        getController().stopObservation()
    }
}