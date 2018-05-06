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

import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import universum.studios.android.arkhitekton.view.ViewModel

/**
 * @author Martin Albedinsky
 */
interface ChallengeViewModel : ViewModel {

    companion object {

        const val DATA_SET_INDEX_ATTENTION = 0
        const val DATA_SET_INDEX_MEDITATION = 1
    }

    val deviceName: ObservableField<CharSequence>
    val timeStarted: ObservableField<CharSequence>
    val level: ObservableField<CharSequence>

    val attentionValueActual: ObservableInt
    val meditationValueActual: ObservableInt

    val attentionAchievementText: ObservableField<CharSequence>
    val meditationAchievementText: ObservableField<CharSequence>

    val chartDataAll: ObservableField<LineData>
    val chartDataLive: ObservableField<LineData>
    val chartDataLiveXAxisFormatter: IAxisValueFormatter

    val chartDataHistogram: ObservableField<BarData>
}