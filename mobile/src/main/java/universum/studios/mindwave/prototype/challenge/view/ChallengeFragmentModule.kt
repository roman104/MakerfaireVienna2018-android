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
package universum.studios.mindwave.prototype.challenge.view

import android.bluetooth.BluetoothManager
import android.content.Context
import dagger.Module
import dagger.Provides
import universum.studios.android.arkhitekton.interaction.Interactor
import universum.studios.mindwave.prototype.ApplicationContext
import universum.studios.mindwave.prototype.challenge.control.ChallengeController
import universum.studios.mindwave.prototype.challenge.control.DefaultChallengeController
import universum.studios.mindwave.prototype.challenge.view.presentation.ChallengePresenter
import universum.studios.mindwave.prototype.challenge.view.presentation.DefaultChallengePresenter
import universum.studios.mindwave.prototype.view.BaseFragmentModule
import universum.studios.mindwave.prototype.view.EmptyFragmentModule

/**
 * @author Martin Albedinsky
 */
@Module(includes = [EmptyFragmentModule::class])
class ChallengeFragmentModule : BaseFragmentModule() {

	@Provides fun provideController(
			fragment: ChallengeFragment,
			interactor: Interactor,
			presenter: ChallengePresenter,
			@ApplicationContext context: Context
	): ChallengeController {
	    val holder = provideControllerHolder(fragment, ChallengeController.Holder::class.java)
		return if (holder.hasController()) holder.getController()
		else holder.attachController(DefaultChallengeController.Builder(interactor, presenter)
				.apply {
					this.session = fragment.arguments?.getParcelable(ChallengeFragment.ARGUMENT_SESSION) ?: throw IllegalArgumentException("No session found in fragment arguments!")
					this.context = context
					this.bluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
				}
				.build())
	}
	
	@Provides fun providePresenter(viewModel: ChallengeViewModel): ChallengePresenter = DefaultChallengePresenter(viewModel)
	@Provides fun provideViewModel(fragment: ChallengeFragment): ChallengeViewModel = provideViewModel(fragment, ChallengeViewModelImpl::class.java)
}