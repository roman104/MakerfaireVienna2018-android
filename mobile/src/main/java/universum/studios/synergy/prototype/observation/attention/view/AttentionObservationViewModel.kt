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
package universum.studios.synergy.prototype.observation.attention.view

import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.github.mikephil.charting.data.LineData
import universum.studios.android.arkhitekton.view.ViewModel

/**
 * @author Martin Albedinsky
 */
interface AttentionObservationViewModel : ViewModel {

    val deviceSignalQuality: ObservableField<CharSequence>
    val actualValue: ObservableInt
    val chartData: ObservableField<LineData>
}