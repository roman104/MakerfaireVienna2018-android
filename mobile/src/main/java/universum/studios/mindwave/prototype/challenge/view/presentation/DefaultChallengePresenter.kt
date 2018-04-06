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
package universum.studios.mindwave.prototype.challenge.view.presentation

import universum.studios.android.arkhitekton.presentation.BasePresenter
import universum.studios.mindwave.prototype.R
import universum.studios.mindwave.prototype.challenge.view.ChallengeView
import universum.studios.mindwave.prototype.challenge.view.ChallengeViewModel

/**
 * @author Martin Albedinsky
 */
class DefaultChallengePresenter(viewModel: ChallengeViewModel) : BasePresenter<ChallengeView, ChallengeViewModel>(viewModel), ChallengePresenter {

	override fun onFirstParticipantAttentionChanged(attention: Int) {
		val resources = getView().getResources()
		getViewModel().setFirstParticipantAttentionState(resources.getString(R.string.challenge_participant_attention_value_format, attention))
	}

	override fun onSecondParticipantAttentionChanged(attention: Int) {
        val resources = getView().getResources()
		getViewModel().setSecondParticipantAttentionState(resources.getString(R.string.challenge_participant_attention_value_format, attention))
	}
}