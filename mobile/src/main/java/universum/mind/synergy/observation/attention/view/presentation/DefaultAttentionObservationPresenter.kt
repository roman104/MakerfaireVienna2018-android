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
package universum.mind.synergy.observation.attention.view.presentation

import android.arch.lifecycle.Lifecycle
import com.github.mikephil.charting.data.Entry
import universum.mind.synergy.device.headset.Headset.SignalQuality
import universum.mind.synergy.device.headset.data.AttentionData
import universum.mind.synergy.observation.attention.view.AttentionObservationViewModel
import universum.mind.synergy.observation.view.ObservationView
import universum.studios.android.arkhitekton.presentation.BasePresenter
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Martin Albedinsky
 */
class DefaultAttentionObservationPresenter(viewModel: AttentionObservationViewModel)
	: BasePresenter<ObservationView<AttentionObservationViewModel>, AttentionObservationViewModel>(viewModel), AttentionObservationPresenter {

    private val xAxisCounter = AtomicInteger()

    init {
        viewModel.deviceSignalQuality.set(SignalQuality.UNKNOWN.name)
    }

    override fun onHeadsetSignalQualityChanged(quality: SignalQuality) {}

    override fun onObservationDataChanged(data: AttentionData) {
        val viewModel = getViewModel()
        viewModel.valueActual.set(data.value)
        val chartData = viewModel.chartData.get()!!
        // todo: calculate x value based on time ...
        chartData.addEntry(Entry(xAxisCounter.incrementAndGet().toFloat(), data.value.toFloat()), 0)
        if (isViewAttached() && getViewLifecycleCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            getView().refreshChart()
        }
    }
}