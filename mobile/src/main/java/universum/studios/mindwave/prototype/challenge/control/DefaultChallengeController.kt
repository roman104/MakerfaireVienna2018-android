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
package universum.studios.mindwave.prototype.challenge.control

import android.bluetooth.BluetoothAdapter
import android.content.Context
import universum.studios.android.arkhitekton.control.ReactiveController
import universum.studios.android.arkhitekton.interaction.Interactor
import universum.studios.mindwave.prototype.challenge.view.presentation.ChallengePresenter

/**
 * @author Martin Albedinsky
 */
class DefaultChallengeController internal constructor(builder: Builder) : ReactiveController<Interactor, ChallengePresenter>(builder), ChallengeController {

    companion object {
    
        @Suppress("unused") const val TAG = "DefaultChallengeController"
    }

    override fun startChallenge() {
        // todo:
    }

    override fun stopChallenge() {
        // todo:
    }
    
    class Builder(
        interactor: Interactor,
        presenter: ChallengePresenter
    ) : ReactiveController.BaseBuilder<Builder, Interactor, ChallengePresenter>(interactor, presenter) {
    
        override val self = this
        lateinit var context: Context
        var bluetoothAdapter: BluetoothAdapter? = null

        override fun build() = DefaultChallengeController(this)
    }
}