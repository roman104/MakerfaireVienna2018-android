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

import dagger.Module
import dagger.Provides
import universum.studios.android.arkhitekton.interaction.Interactor
import universum.mind.synergy.device.headset.Headset
import universum.mind.synergy.observation.attention.control.AttentionObservationController
import universum.mind.synergy.observation.attention.control.DefaultAttentionObservationController
import universum.mind.synergy.observation.attention.view.presentation.AttentionObservationPresenter
import universum.mind.synergy.observation.attention.view.presentation.DefaultAttentionObservationPresenter
import universum.mind.synergy.view.BaseFragmentModule
import universum.mind.synergy.view.EmptyFragmentModule

/**
 * @author Martin Albedinsky
 */
@Module(includes = [EmptyFragmentModule::class])
class AttentionObservationFragmentModule : BaseFragmentModule() {

	@Provides fun provideController(
			fragment: AttentionObservationFragment,
			interactor: Interactor,
			presenter: AttentionObservationPresenter,
			headset: Headset
	): AttentionObservationController {
	    val holder = provideControllerHolder(fragment, AttentionObservationController.Holder::class.java)
		return if (holder.hasController()) holder.getController()
		else holder.attachController(DefaultAttentionObservationController.Builder(interactor, presenter)
                .apply { this.headset = headset }
                .build())
	}
	
	@Provides fun providePresenter(viewModel: AttentionObservationViewModel): AttentionObservationPresenter = DefaultAttentionObservationPresenter(viewModel)
	@Provides fun provideViewModel(fragment: AttentionObservationFragment): AttentionObservationViewModel = provideViewModel(fragment, AttentionObservationViewModelImpl::class.java)
}