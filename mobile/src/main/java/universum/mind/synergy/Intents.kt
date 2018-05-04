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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

/**
 * @author Martin Albedinsky
 */
class Intents private constructor() {

    companion object {
    
    }

    class System private constructor() {

        companion object {

            @JvmStatic fun navigateToBluetoothSettings(context: Context) {
                context.startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
            }

            @JvmStatic fun navigateToApplicationDetailsSettings(context: Context) {
                context.startActivity(Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:${context.packageName}")
                ))
            }
        }
    }
}