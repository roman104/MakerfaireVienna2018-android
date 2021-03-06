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
package universum.mind.synergy.observation.attention.view

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.github.mikephil.charting.data.LineData
import universum.mind.synergy.observation.attention.data.AttentionChartData

/**
 * @author Martin Albedinsky
 */
class AttentionObservationViewModelImpl(application: Application) : AndroidViewModel(application), AttentionObservationViewModel {

    override val valueActual = ObservableInt(0)
    override val valueAverage = ObservableInt(0)
    override val valueHighest = ObservableInt(0)
    override val valueLowest = ObservableInt(0)

    override val chartData: ObservableField<LineData> = ObservableField(LineData(AttentionChartData.createInitialLineDataSet(application)))
}