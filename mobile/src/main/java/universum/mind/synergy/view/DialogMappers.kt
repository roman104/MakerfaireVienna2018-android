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
package universum.mind.synergy.view

import universum.studios.android.arkhitekton.data.model.ModelMapper
import universum.studios.android.arkhitekton.util.Description
import universum.studios.android.support.dialog.DialogOptions

/**
 * @author Martin Albedinsky
 */
class DialogMappers private constructor() {

    companion object {

        val DESCRIPTION_TO_OPTIONS: ModelMapper<Description, DialogOptions<*>> = DescriptionToOptions()

        private class DescriptionToOptions : ModelMapper<Description, DialogOptions<*>> {

            override fun map(description: Description): DialogOptions<*> = DialogOptions<DialogOptions<*>>()
                    .title(description.getTitle())
                    .content(description.getSummary())
        }
    }
}
