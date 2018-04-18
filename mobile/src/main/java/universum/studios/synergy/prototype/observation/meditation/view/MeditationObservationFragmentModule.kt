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
package universum.studios.synergy.prototype.observation.meditation.view

import dagger.Module
import dagger.Provides
import universum.studios.android.arkhitekton.interaction.Interactor
import universum.studios.synergy.prototype.device.headset.Headset
import universum.studios.synergy.prototype.observation.meditation.control.DefaultMeditationObservationController
import universum.studios.synergy.prototype.observation.meditation.control.MeditationObservationController
import universum.studios.synergy.prototype.observation.meditation.view.presentation.DefaultMeditationObservationPresenter
import universum.studios.synergy.prototype.observation.meditation.view.presentation.MeditationObservationPresenter
import universum.studios.synergy.prototype.view.BaseFragmentModule
import universum.studios.synergy.prototype.view.EmptyFragmentModule

/**
 * @author Martin Albedinsky
 */
@Module(includes = [EmptyFragmentModule::class])
class MeditationObservationFragmentModule : BaseFragmentModule() {

	@Provides fun provideController(
			fragment: MeditationObservationFragment,
			interactor: Interactor,
			presenter: MeditationObservationPresenter,
			headset: Headset
	): MeditationObservationController {
	    val holder = provideControllerHolder(fragment, MeditationObservationController.Holder::class.java)
		return if (holder.hasController()) holder.getController()
		else holder.attachController(DefaultMeditationObservationController.Builder(interactor, presenter)
                .apply { this.headset = headset }
                .build())
	}
	
	@Provides fun providePresenter(viewModel: MeditationObservationViewModel): MeditationObservationPresenter = DefaultMeditationObservationPresenter(viewModel)
	@Provides fun provideViewModel(fragment: MeditationObservationFragment): MeditationObservationViewModel = provideViewModel(fragment, MeditationObservationViewModelImpl::class.java)
}