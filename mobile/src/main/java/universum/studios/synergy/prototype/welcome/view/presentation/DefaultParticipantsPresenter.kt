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
package universum.studios.synergy.prototype.welcome.view.presentation

import android.bluetooth.BluetoothDevice
import universum.studios.android.arkhitekton.presentation.BasePresenter
import universum.studios.synergy.prototype.welcome.view.ParticipantsView
import universum.studios.synergy.prototype.welcome.view.ParticipantsViewModel

/**
 * @author Martin Albedinsky
 */
class DefaultParticipantsPresenter(viewModel: ParticipantsViewModel) : BasePresenter<ParticipantsView, ParticipantsViewModel>(viewModel), ParticipantsPresenter {

	override fun onAvailableDevicesChanged(devices: List<BluetoothDevice>) {
		getViewModel().updateAvailableDevices(devices)
	}
}