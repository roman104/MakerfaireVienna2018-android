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
package universum.studios.mindwave.prototype.welcome.view

import android.bluetooth.BluetoothManager
import android.content.Context
import dagger.Module
import dagger.Provides
import universum.studios.android.arkhitekton.interaction.Interactor
import universum.studios.mindwave.prototype.ApplicationContext
import universum.studios.mindwave.prototype.view.BaseFragmentModule
import universum.studios.mindwave.prototype.view.EmptyFragmentModule
import universum.studios.mindwave.prototype.welcome.control.DefaultParticipantsController
import universum.studios.mindwave.prototype.welcome.control.ParticipantsController
import universum.studios.mindwave.prototype.welcome.view.presentation.DefaultParticipantsPresenter
import universum.studios.mindwave.prototype.welcome.view.presentation.ParticipantsPresenter

/**
 * @author Martin Albedinsky
 */
@Module(includes = [EmptyFragmentModule::class])
class ParticipantsFragmentModule : BaseFragmentModule() {

	@Provides fun provideController(
			fragment: ParticipantsFragment,
			interactor: Interactor,
			presenter: ParticipantsPresenter,
            @ApplicationContext context: Context
	): ParticipantsController {
	    val holder = provideControllerHolder(fragment, ParticipantsController.Holder::class.java)
		return if (holder.hasController()) holder.getController()
		else holder.attachController(DefaultParticipantsController.Builder(interactor, presenter)
				.apply {
					this.context = context
					this.bluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
				}
				.build())
	}
	
	@Provides fun providePresenter(viewModel: ParticipantsViewModel): ParticipantsPresenter = DefaultParticipantsPresenter(viewModel)
	@Provides fun provideViewModel(fragment: ParticipantsFragment): ParticipantsViewModel = provideViewModel(fragment, ParticipantsViewModelImpl::class.java)
}