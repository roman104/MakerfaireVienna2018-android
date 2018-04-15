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
package universum.studios.synergy.prototype.observation.attention.view

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

/**
 * @author Martin Albedinsky
 */
class AttentionObservationViewModelImpl : ViewModel(), AttentionObservationViewModel {
    
	override val inputError = ObservableField<CharSequence>()

    override fun setInputError(error: CharSequence?) = this.inputError.set(error)

    override fun clearInputErrors() {
        inputError.set(null)
    }
}