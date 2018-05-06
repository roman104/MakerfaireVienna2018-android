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
package universum.mind.synergy

import universum.mind.synergy.challenge.view.ChooseChallengeLevelDialog
import universum.studios.android.support.dialog.manage.DialogInflater
import universum.studios.android.support.fragment.annotation.FragmentAnnotations

/**
 * @author Martin Albedinsky
 */
class Config {

    class App private constructor() {

        @Suppress("unused") companion object {

            const val PRODUCTION_ID = BuildConfig.PRODUCTION_APPLICATION_ID
            const val PRODUCTION_VERSION_NAME = BuildConfig.PRODUCTION_VERSION_NAME

            const val FLAVOR_ID = BuildConfig.APPLICATION_ID
            const val FLAVOR_VERSION_NAME = BuildConfig.VERSION_NAME

            val DEBUG = BuildConfig.DEBUG
        }
    }

    companion object {

        fun apply() {
            FragmentAnnotations.setEnabled(true)
            DialogInflater.registerDialog(ChooseChallengeLevelDialog::class.java)
        }
    }
}