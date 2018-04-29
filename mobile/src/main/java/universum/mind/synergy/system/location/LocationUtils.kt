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
package universum.mind.synergy.system.location

import android.Manifest
import android.content.Context
import universum.studios.android.util.Permissions

/**
 * @author Martin Albedinsky
 */
class LocationUtils private constructor() {

    companion object {

        fun hasNotPermission(context: Context) = !hasPermission(context)

        fun hasPermission(context: Context) = Permissions.has(context, Manifest.permission.ACCESS_COARSE_LOCATION)

        fun isEnalbed(context: Context): Boolean {
            // todo:
            return false
        }
    }
}