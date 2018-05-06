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
package universum.mind.synergy.challenge.control

import io.reactivex.disposables.CompositeDisposable
import universum.mind.synergy.challenge.ChallengeAchievement
import universum.mind.synergy.challenge.view.presentation.ChallengePresenter
import universum.mind.synergy.device.headset.Headset
import universum.mind.synergy.device.headset.data.HeadsetDataObservable
import universum.mind.synergy.util.DatePolices
import universum.studios.android.arkhitekton.control.ReactiveController
import universum.studios.android.arkhitekton.interaction.Interactor
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Martin Albedinsky
 */
class DefaultChallengeController internal constructor(builder: Builder) : ReactiveController<Interactor, ChallengePresenter>(builder), ChallengeController {

    private val headset = builder.headset
    private val challengeLevel = builder.level
    private val challengeRunning = AtomicBoolean()
    private val compositeDisposable = CompositeDisposable()
    internal var attentionChallengeAchievementStart = DatePolices.NO_TIME
    internal var attentionLatestChallengeAchievementDuration = DatePolices.NO_TIME
    internal var meditationChallengeAchievementStart = DatePolices.NO_TIME
    internal var meditationLatestChallengeAchievementDuration = DatePolices.NO_TIME

    override fun startChallenge() {
        if (challengeRunning.compareAndSet(false, true)) {
            getPresenter().onChallengeStarted(headset.getDeviceName(), System.currentTimeMillis(), challengeLevel)
            this.compositeDisposable.addAll(
                    HeadsetDataObservable.attention(headset)
                            .observeOn(presentationScheduler)
                            .subscribeOn(interactionScheduler)
                            .subscribe { data ->
                                val presenter = getPresenter()
                                presenter.onAttentionChanged(data)
                                if (data.value >= challengeLevel) {
                                    if (attentionChallengeAchievementStart == DatePolices.NO_TIME) {
                                        attentionChallengeAchievementStart = System.currentTimeMillis()
                                    }
                                } else if (attentionChallengeAchievementStart != DatePolices.NO_TIME) {
                                    val challengeDuration = System.currentTimeMillis() - attentionChallengeAchievementStart
                                    if (challengeDuration > attentionLatestChallengeAchievementDuration) {
                                        attentionLatestChallengeAchievementDuration = challengeDuration
                                        presenter.onAttentionAchievement(ChallengeAchievement(challengeDuration))
                                    }
                                    attentionChallengeAchievementStart = DatePolices.NO_TIME
                                }
                            },
                    HeadsetDataObservable.meditation(headset)
                            .observeOn(presentationScheduler)
                            .subscribeOn(interactionScheduler)
                            .subscribe { data ->
                                val presenter = getPresenter()
                                presenter.onMeditationChanged(data)
                                if (data.value >= challengeLevel) {
                                    if (meditationChallengeAchievementStart == DatePolices.NO_TIME) {
                                        meditationChallengeAchievementStart = System.currentTimeMillis()
                                    }
                                } else if (meditationChallengeAchievementStart != DatePolices.NO_TIME) {
                                    val challengeDuration = System.currentTimeMillis() - meditationChallengeAchievementStart
                                    if (challengeDuration > meditationLatestChallengeAchievementDuration) {
                                        meditationLatestChallengeAchievementDuration = challengeDuration
                                        presenter.onMeditationAchievement(ChallengeAchievement(challengeDuration))
                                    }
                                    meditationChallengeAchievementStart = DatePolices.NO_TIME
                                }
                            }
            )
        }
    }

    override fun isChallengeRunning() = challengeRunning.get()

    override fun stopChallenge() {
        if (challengeRunning.compareAndSet(true, false)) {
            this.compositeDisposable.clear()
        }
    }

    override fun onDeactivated() {
        super.onDeactivated()
        stopChallenge()
    }
    
    class Builder(interactor: Interactor, presenter: ChallengePresenter)
        : ReactiveController.BaseBuilder<Builder, Interactor, ChallengePresenter>(interactor, presenter) {
    
        override val self = this
        lateinit var headset: Headset
        var level = 50

        override fun build() = DefaultChallengeController(this)
    }
}