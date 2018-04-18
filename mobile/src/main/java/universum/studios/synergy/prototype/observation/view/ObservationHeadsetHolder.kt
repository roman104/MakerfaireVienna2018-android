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
package universum.studios.synergy.prototype.observation.view

import android.arch.lifecycle.ViewModel
import universum.studios.android.arkhitekton.util.Preconditions
import universum.studios.synergy.prototype.device.headset.Headset

/**
 * @author Martin Albedinsky
 */
class ObservationHeadsetHolder : ViewModel() {

    private var headset: Headset? = null

    fun attachHeadset(headset: Headset): Headset {
        this.headset = headset
        return getHeadset()
    }

    fun hasHeadset() = headset != null

    fun getHeadset(): Headset = Preconditions.checkNotNull(headset, "No headset attached!")

    fun detachHeadset() {
        this.headset = null
    }

    override fun onCleared() {
        super.onCleared()
        detachHeadset()
    }
}