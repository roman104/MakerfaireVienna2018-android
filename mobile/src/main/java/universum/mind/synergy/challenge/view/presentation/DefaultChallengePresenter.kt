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
package universum.mind.synergy.challenge.view.presentation

import android.arch.lifecycle.Lifecycle
import com.github.mikephil.charting.data.Entry
import universum.mind.synergy.challenge.view.ChallengeView
import universum.mind.synergy.challenge.view.ChallengeViewModel
import universum.mind.synergy.device.headset.data.AttentionData
import universum.mind.synergy.device.headset.data.MeditationData
import universum.studios.android.arkhitekton.presentation.BasePresenter

/**
 * @author Martin Albedinsky
 */
class DefaultChallengePresenter(viewModel: ChallengeViewModel) : BasePresenter<ChallengeView, ChallengeViewModel>(viewModel), ChallengePresenter {

    private var timeStarted = 0L

    override fun onChallengeStarted() {
        this.timeStarted = System.currentTimeMillis()
    }

    override fun onAttentionChanged(data: AttentionData) {
        val viewModel = getViewModel()
        viewModel.attentionValueActual.set(data.value)
        val chartData = viewModel.chartData.get()!!
        chartData.addEntry(
                Entry((data.timeObserved - timeStarted).toFloat(), data.value.toFloat()),
                ChallengeViewModel.DATA_SET_INDEX_ATTENTION
        )
        if (chartData.entryCount > ChallengePresenter.CHART_MAX_VISIBLE_ENTRIES) {
            chartData.getDataSetByIndex(ChallengeViewModel.DATA_SET_INDEX_ATTENTION).removeFirst()
            chartData.notifyDataChanged()
        }
        this.refreshChartView()
    }

    override fun onMeditationChanged(data: MeditationData) {
        val viewModel = getViewModel()
        viewModel.meditationValueActual.set(data.value)
        val chartData = viewModel.chartData.get()!!
        chartData.addEntry(
                Entry((data.timeObserved - timeStarted).toFloat(), data.value.toFloat()),
                ChallengeViewModel.DATA_SET_INDEX_MEDITATION
        )
        if (chartData.entryCount > ChallengePresenter.CHART_MAX_VISIBLE_ENTRIES) {
            chartData.getDataSetByIndex(ChallengeViewModel.DATA_SET_INDEX_MEDITATION).removeFirst()
            chartData.notifyDataChanged()
        }
        this.refreshChartView()
    }

    private fun refreshChartView() {
        if (isViewAttached() && getViewLifecycleCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            getView().refreshChart()
        }
    }
}