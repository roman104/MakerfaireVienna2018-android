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

import universum.studios.android.arkhitekton.presentation.BasePresenter
import universum.studios.android.arkhitekton.view.View
import universum.studios.synergy.prototype.device.headset.data.AttentionData
import universum.studios.synergy.prototype.observation.attention.view.AttentionObservationViewModel

/**
 * @author Martin Albedinsky
 */
class DefaultAttentionObservationPresenter(viewModel: AttentionObservationViewModel)
	: BasePresenter<View<AttentionObservationViewModel>, AttentionObservationViewModel>(viewModel), AttentionObservationPresenter {

    companion object {
    
    }

    override fun onAttentionChanged(data: AttentionData) {
    }
}