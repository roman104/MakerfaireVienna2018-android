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
package universum.studios.synergy.prototype.observation.attention.view.presentation

import android.arch.lifecycle.Lifecycle
import android.os.Handler
import com.github.mikephil.charting.data.Entry
import universum.studios.android.arkhitekton.presentation.BasePresenter
import universum.studios.synergy.prototype.R
import universum.studios.synergy.prototype.device.headset.Headset.SignalQuality
import universum.studios.synergy.prototype.device.headset.data.AttentionData
import universum.studios.synergy.prototype.observation.attention.view.AttentionObservationViewModel
import universum.studios.synergy.prototype.observation.view.ObservationView
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Martin Albedinsky
 */
class DefaultAttentionObservationPresenter(viewModel: AttentionObservationViewModel)
	: BasePresenter<ObservationView<AttentionObservationViewModel>, AttentionObservationViewModel>(viewModel), AttentionObservationPresenter {

    private val handler = Handler()
    private val xAxisCounter = AtomicInteger()

    init {
        viewModel.deviceSignalQuality.set(SignalQuality.UNKNOWN.name)
    }

    override fun onHeadsetSignalQualityChanged(quality: SignalQuality) {
        getViewModel().deviceSignalQuality.set(getView().getResources().getString(R.string.observation_device_signal_quality_format, quality.name))
    }

    override fun onObservationDataChanged(data: AttentionData) {
        val viewModel = getViewModel()
        viewModel.actualValue.set(data.value)
        val chartData = viewModel.chartData.get()!!
        // todo: calculate x value based on time ...
        chartData.addEntry(Entry(xAxisCounter.incrementAndGet().toFloat(), data.value.toFloat()), 0)
        if (isViewAttached() && getViewLifecycleCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            handler.post { getView().refreshChart() }
        }
    }
}