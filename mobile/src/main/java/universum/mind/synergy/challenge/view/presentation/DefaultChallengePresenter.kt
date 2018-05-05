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
import universum.mind.synergy.challenge.ChallengeAchievement
import universum.mind.synergy.challenge.view.ChallengeView
import universum.mind.synergy.challenge.view.ChallengeViewModel
import universum.mind.synergy.device.headset.data.AttentionData
import universum.mind.synergy.device.headset.data.MeditationData
import universum.mind.synergy.util.DateUtils
import universum.studios.android.arkhitekton.presentation.BasePresenter

/**
 * @author Martin Albedinsky
 */
class DefaultChallengePresenter(viewModel: ChallengeViewModel) : BasePresenter<ChallengeView, ChallengeViewModel>(viewModel), ChallengePresenter {

    companion object {

        private val ACHIEVEMENT_DURATION_FORMAT = DateUtils.Formatter.createFormat("mm:ss")
    }

    private var timeStarted = 0L

    override fun onChallengeStarted(deviceName: String, timeStarted: Long) {
        this.timeStarted = timeStarted
        val viewModel = getViewModel()
        viewModel.deviceName.set(deviceName)
        viewModel.timeStarted.set("(${DateUtils.Formatter.format(timeStarted, DateUtils.Pattern.TIME_DETAILED)})")
    }

    override fun onAttentionChanged(data: AttentionData) {
        val viewModel = getViewModel()
        viewModel.attentionValueActual.set(data.value)
        // Update chart presenting actual data:
        val actualChartData = viewModel.chartDataLive.get()!!
        viewModel.chartDataAll.get()!!.addEntry(
                Entry((data.timeObserved - timeStarted).toFloat(), data.value.toFloat()),
                ChallengeViewModel.DATA_SET_INDEX_ATTENTION
        )
        actualChartData.addEntry(
                Entry((data.timeObserved - timeStarted).toFloat(), data.value.toFloat()),
                ChallengeViewModel.DATA_SET_INDEX_ATTENTION
        )
        if (actualChartData.entryCount > ChallengePresenter.CHART_MAX_VISIBLE_ENTRIES) {
            actualChartData.getDataSetByIndex(ChallengeViewModel.DATA_SET_INDEX_ATTENTION).removeFirst()
            actualChartData.notifyDataChanged()
        }
        // Update chart presenting histogram data:
        val histogramChartData = viewModel.chartDataHistogram.get()!!
        val histogramDataSet = histogramChartData.getDataSetByIndex(ChallengeViewModel.DATA_SET_INDEX_ATTENTION)
        var i = 0
        while (i < histogramDataSet.entryCount) {
            val entry = histogramDataSet.getEntryForIndex(i)
            if (data.value <= entry.x + 10f) {
                entry.y = entry.y + 1f
                histogramChartData.notifyDataChanged()
                break
            }
            i++
        }
        this.refreshCharts()
    }

    override fun onAttentionAchievement(achievement: ChallengeAchievement) {
        getViewModel().attentionAchievementText.set(DateUtils.Formatter.format(achievement.duration, ACHIEVEMENT_DURATION_FORMAT))
    }

    override fun onMeditationChanged(data: MeditationData) {
        val viewModel = getViewModel()
        viewModel.meditationValueActual.set(data.value)
        // Update chart presenting actual data:
        viewModel.chartDataAll.get()!!.addEntry(
                Entry((data.timeObserved - timeStarted).toFloat(), data.value.toFloat()),
                ChallengeViewModel.DATA_SET_INDEX_MEDITATION
        )
        val actualChartData = viewModel.chartDataLive.get()!!
        actualChartData.addEntry(
                Entry((data.timeObserved - timeStarted).toFloat(), data.value.toFloat()),
                ChallengeViewModel.DATA_SET_INDEX_MEDITATION
        )
        if (actualChartData.entryCount > ChallengePresenter.CHART_MAX_VISIBLE_ENTRIES) {
            actualChartData.getDataSetByIndex(ChallengeViewModel.DATA_SET_INDEX_MEDITATION).removeFirst()
            actualChartData.notifyDataChanged()
        }
        // Update chart presenting histogram data:
        val histogramChartData = viewModel.chartDataHistogram.get()!!
        val histogramDataSet = histogramChartData.getDataSetByIndex(ChallengeViewModel.DATA_SET_INDEX_MEDITATION)
        var i = 0
        while (i < histogramDataSet.entryCount) {
            val entry = histogramDataSet.getEntryForIndex(i)
            if (data.value <= entry.x + 10f) {
                entry.y = entry.y + 1f
                histogramChartData.notifyDataChanged()
                break
            }
            i++
        }
        this.refreshCharts()
    }

    override fun onMeditationAchievement(achievement: ChallengeAchievement) {
        getViewModel().meditationAchievementText.set(DateUtils.Formatter.format(achievement.duration, ACHIEVEMENT_DURATION_FORMAT))
    }

    private fun refreshCharts() {
        if (isViewAttached() && getViewLifecycleCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            getView().refreshCharts()
        }
    }
}