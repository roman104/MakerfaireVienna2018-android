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
package universum.mind.synergy.observation.meditation.view

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

/**
 * @author Martin Albedinsky
 */
class MeditationObservationViewModelImpl : ViewModel(), MeditationObservationViewModel {

    override val deviceSignalQuality = ObservableField<CharSequence>("")
    override val actualValue = ObservableInt()

	override val chartData: ObservableField<LineData> = ObservableField(LineData(LineDataSet(
            // Data set must contain at least one entry!
            arrayListOf(Entry(0f, 0f)),
            "Values")
    ))
}