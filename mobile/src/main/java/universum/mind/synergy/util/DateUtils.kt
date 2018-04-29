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
package universum.mind.synergy.util

/**
 * @author Martin Albedinsky
 */
class DateUtils private constructor() {

    companion object {
    
        // todo: implement class
    }

    class Formatter private constructor() {

        companion object {

            fun formatElapsedTime(timeInMillis: Long): String {
                var formatted = ""
                var hours = 0L
                var minutes = 0L
                var seconds: Long

                if (timeInMillis >= 1000 * 60 * 60) {
                    hours = timeInMillis / (1000 * 60 * 60)
                    formatted = "${hours}h"
                }
                if (timeInMillis >= 1000 * 60) {
                    minutes = timeInMillis / (1000 * 60)
                    minutes -= hours * 60
                    formatted += if (hours > 0) " ${minutes}m" else "${minutes}m"
                }
                if (timeInMillis >= 1000) {
                    seconds = timeInMillis / 1000
                    seconds -= hours * 60 * 60 + minutes * 60
                    formatted += if (hours > 0 || minutes > 0) " ${seconds}s" else "${seconds}s"
                }

                return formatted
            }
        }
    }
}