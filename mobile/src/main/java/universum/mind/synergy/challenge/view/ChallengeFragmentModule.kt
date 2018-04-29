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

import dagger.Module
import dagger.Provides
import universum.mind.synergy.challenge.control.ChallengeController
import universum.mind.synergy.challenge.control.DefaultChallengeController
import universum.mind.synergy.challenge.view.presentation.ChallengePresenter
import universum.mind.synergy.challenge.view.presentation.DefaultChallengePresenter
import universum.mind.synergy.view.BaseFragmentModule
import universum.mind.synergy.view.EmptyFragmentModule
import universum.studios.android.arkhitekton.interaction.Interactor

/**
 * @author Martin Albedinsky
 */
@Module(includes = [EmptyFragmentModule::class])
class ChallengeFragmentModule : BaseFragmentModule() {

	@Provides fun provideController(
            fragment: ChallengeFragment,
            interactor: Interactor,
            presenter: ChallengePresenter
	): ChallengeController {
	    val holder = provideControllerHolder(fragment, ChallengeController.Holder::class.java)
		return if (holder.hasController()) holder.getController()
		else holder.attachController(DefaultChallengeController.Builder(interactor, presenter)
				.apply {
				}
				.build())
	}
	
	@Provides fun providePresenter(viewModel: ChallengeViewModel): ChallengePresenter = DefaultChallengePresenter(viewModel)
	@Provides fun provideViewModel(fragment: ChallengeFragment): ChallengeViewModel = provideViewModel(fragment, ChallengeViewModelImpl::class.java)
}