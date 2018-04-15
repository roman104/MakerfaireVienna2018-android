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
package universum.studios.synergy.prototype.observation

import universum.studios.synergy.prototype.R

/**
 * @author Martin Albedinsky
 */
enum class ObservationSubject constructor(val id: Int, val flag: Int, val nameRes: Int) {
    ATTENTION(1, 0x00000001, R.string.observation_subject_attention),
    MEDITATION(2, 0x00000002, R.string.observation_subject_meditation)
}