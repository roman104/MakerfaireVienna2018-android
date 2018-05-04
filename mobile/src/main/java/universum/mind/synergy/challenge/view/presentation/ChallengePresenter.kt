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

import universum.mind.synergy.challenge.ChallengeAchievement
import universum.mind.synergy.challenge.view.ChallengeView
import universum.mind.synergy.device.headset.data.AttentionData
import universum.mind.synergy.device.headset.data.MeditationData
import universum.studios.android.arkhitekton.presentation.Presenter

/**
 * @author Martin Albedinsky
 */
interface ChallengePresenter : Presenter<ChallengeView> {

    companion object {

        const val CHART_MAX_VISIBLE_ENTRIES = 20
    }

    fun onChallengeStarted()

    fun onAttentionChanged(data: AttentionData)

    fun onAttentionAchievement(achievement: ChallengeAchievement)

    fun onMeditationChanged(data: MeditationData)

    fun onMeditationAchievement(achievement: ChallengeAchievement)
}