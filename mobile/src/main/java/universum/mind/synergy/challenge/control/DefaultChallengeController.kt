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
import universum.mind.synergy.challenge.view.presentation.ChallengePresenter
import universum.mind.synergy.device.headset.Headset
import universum.mind.synergy.device.headset.data.HeadsetDataObservable
import universum.studios.android.arkhitekton.control.ReactiveController
import universum.studios.android.arkhitekton.interaction.Interactor
import universum.studios.android.arkhitekton.util.Preconditions
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Martin Albedinsky
 */
class DefaultChallengeController internal constructor(builder: Builder) : ReactiveController<Interactor, ChallengePresenter>(builder), ChallengeController {

    private val headset = Preconditions.checkNotNull(builder.headset)
    private val challengeRunning = AtomicBoolean()
    private val compositeDisposable = CompositeDisposable()

    override fun startChallenge() {
        if (challengeRunning.compareAndSet(false, true)) {
            getPresenter().onChallengeStarted()
            this.compositeDisposable.addAll(
                    HeadsetDataObservable.attention(headset)
                            .observeOn(presentationScheduler)
                            .subscribeOn(interactionScheduler)
                            .subscribe(getPresenter()::onAttentionChanged),
                    HeadsetDataObservable.meditation(headset)
                            .observeOn(presentationScheduler)
                            .subscribeOn(interactionScheduler)
                            .subscribe(getPresenter()::onMeditationChanged)
            )
        }
    }

    override fun stopChallenge() {
        if (challengeRunning.compareAndSet(true, false)) {
            this.compositeDisposable.clear()
        }
    }

    override fun onDeactivated() {
        super.onDeactivated()
        stopChallenge()
    }
    
    class Builder(
        interactor: Interactor,
        presenter: ChallengePresenter
    ) : ReactiveController.BaseBuilder<Builder, Interactor, ChallengePresenter>(interactor, presenter) {
    
        override val self = this
        lateinit var headset: Headset

        override fun build() = DefaultChallengeController(this)
    }
}