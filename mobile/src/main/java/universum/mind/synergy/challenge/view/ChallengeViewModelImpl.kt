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
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import universum.mind.synergy.R

/**
 * @author Martin Albedinsky
 */
class ChallengeViewModelImpl(application: Application) : AndroidViewModel(application), ChallengeViewModel {

    override val attentionValueActual = ObservableInt(0)
    override val meditationValueActual = ObservableInt(0)

    override val chartData: ObservableField<LineData> = ObservableField(LineData(
            // ATTENTION:
            LineDataSet(
                    // Data set must contain at least one entry!
                    arrayListOf(Entry(0f, 0f)),
                    application.getString(R.string.observation_subject_attention)
            ).apply {
                color = application.getColor(R.color.observation_subject_attention)
                setCircleColor(application.getColor(R.color.observation_subject_attention))
            },
            // MEDITATION:
            LineDataSet(
                    // Data set must contain at least one entry!
                    arrayListOf(Entry(0f, 0f)),
                    application.getString(R.string.observation_subject_meditation)
            ).apply {
                color = application.getColor(R.color.observation_subject_meditation)
                setCircleColor(application.getColor(R.color.observation_subject_meditation))
            }
    ))

    override val chartDataXAxisFormatter: IAxisValueFormatter = IAxisValueFormatter { value, _ ->
        when {
            value <= 1000 * 60 -> "${(value / 1000).toLong()}s"
            value <= 1000 * 60 * 60 -> "${(value / (1000 * 60)).toLong()}m ${(value / 1000).toLong()}s"
            else -> "${(value / (1000 * 60 * 60)).toLong()}h ${(value / (1000 * 60)).toLong()}m ${(value / 1000).toLong()}s"
        }
    }
}