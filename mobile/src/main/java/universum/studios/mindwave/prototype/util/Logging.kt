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
package universum.studios.mindwave.prototype.util

import android.util.Log
import universum.studios.android.logging.Logger
import universum.studios.android.logging.SimpleLogger
import universum.studios.mindwave.prototype.Config

/**
 * @author Martin Albedinsky
 */
class Logging private constructor() {

    companion object {

        val logger: Logger = SimpleLogger(Log.VERBOSE)

        fun configure() {
            logger.logLevel = if (Config.App.DEBUG) Log.VERBOSE else Log.ASSERT
        }

        fun currentThread(tag: String, message: String = "") = i(tag, message + " on thread => " + Thread.currentThread().name)
        fun d(tag: String, message: String, throwable: Throwable? = null) = logger.d(tag, message, throwable)
        fun i(tag: String, message: String, throwable: Throwable? = null) = logger.i(tag, message, throwable)
        fun w(tag: String, message: String, cause: Throwable? = null) = logger.w(tag, message, cause)
        fun e(tag: String, message: String, cause: Throwable? = null) = logger.e(tag, message, cause)
    }
}