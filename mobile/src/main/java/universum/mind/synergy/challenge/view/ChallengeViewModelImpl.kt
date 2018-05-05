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
package universum.mind.synergy.challenge.view

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import universum.mind.synergy.R
import universum.mind.synergy.observation.attention.data.AttentionChartData
import universum.mind.synergy.observation.meditation.data.MeditationChartData
import universum.mind.synergy.util.DateUtils

/**
 * @author Martin Albedinsky
 */
class ChallengeViewModelImpl(application: Application) : AndroidViewModel(application), ChallengeViewModel {

    override val attentionValueActual = ObservableInt(0)
    override val meditationValueActual = ObservableInt(0)

    override val attentionAchievementText = ObservableField<CharSequence>("")
    override val meditationAchievementText = ObservableField<CharSequence>("")

    override val chartDataAll: ObservableField<LineData> = ObservableField(LineData(
            AttentionChartData.createInitialLineDataSet(application),
            MeditationChartData.createInitialLineDataSet(application)
    ))
    override val chartDataLive: ObservableField<LineData> = ObservableField(LineData(
            AttentionChartData.createInitialLineDataSet(application),
            MeditationChartData.createInitialLineDataSet(application)
    ))
    override val chartDataLiveXAxisFormatter: IAxisValueFormatter = IAxisValueFormatter { value, _ -> DateUtils.Formatter.formatElapsedTime(value.toLong())}

    override val chartDataHistogram = ObservableField(BarData(
            BarDataSet(
                    listOf(
                            BarEntry(0f, 0f),
                            BarEntry(10f, 0f),
                            BarEntry(20f, 0f),
                            BarEntry(30f, 0f),
                            BarEntry(40f, 0f),
                            BarEntry(50f, 0f),
                            BarEntry(60f, 0f),
                            BarEntry(70f, 0f),
                            BarEntry(80f, 0f),
                            BarEntry(90f, 0f),
                            BarEntry(100f, 0f)
                    ),
                    application.getString(R.string.observation_subject_attention)
            ).apply {
                this.color = application.resources.getColor(R.color.observation_subject_attention)
            },
            BarDataSet(
                    listOf(
                            BarEntry(0f, 0f),
                            BarEntry(10f, 0f),
                            BarEntry(20f, 0f),
                            BarEntry(30f, 0f),
                            BarEntry(40f, 0f),
                            BarEntry(50f, 0f),
                            BarEntry(60f, 0f),
                            BarEntry(70f, 0f),
                            BarEntry(80f, 0f),
                            BarEntry(90f, 0f),
                            BarEntry(100f, 0f)
                    ),
                    application.getString(R.string.observation_subject_meditation)
            ).apply {
                this.color = application.resources.getColor(R.color.observation_subject_meditation)
            }
    ).apply { barWidth = 4.5f })
}