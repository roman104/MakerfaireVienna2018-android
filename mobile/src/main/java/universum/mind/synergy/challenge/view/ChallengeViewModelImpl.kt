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
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import universum.mind.synergy.observation.attention.data.AttentionChartData
import universum.mind.synergy.observation.meditation.data.MeditationChartData
import universum.mind.synergy.util.DateUtils

/**
 * @author Martin Albedinsky
 */
class ChallengeViewModelImpl(application: Application) : AndroidViewModel(application), ChallengeViewModel {

    override val attentionValueActual = ObservableInt(0)
    override val meditationValueActual = ObservableInt(0)

    override val chartData: ObservableField<LineData> = ObservableField(LineData(
            AttentionChartData.createInitialLineDataSet(application),
            MeditationChartData.createInitialLineDataSet(application)
    ))

    override val chartDataXAxisFormatter: IAxisValueFormatter = IAxisValueFormatter { value, _ -> DateUtils.Formatter.formatElapsedTime(value.toLong())
    }
}