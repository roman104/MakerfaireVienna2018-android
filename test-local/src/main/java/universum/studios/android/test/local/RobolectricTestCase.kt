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
package universum.studios.android.test.local

import android.app.Application
import android.support.annotation.CallSuper
import android.support.annotation.NonNull
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * Class that may be used to group **suite of Android tests** to be executed on a local *JVM*
 * with shadowed *Android environment* using [RobolectricTestRunner].
 *
 * @author Martin Albedinsky
 */
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
abstract class RobolectricTestCase : LocalTestCase() {

    /**
     * Application instance accessible via [RuntimeEnvironment.application].
     *
     * It is always valid between calls to [.beforeTest] and [.afterTest].
     */
    @NonNull protected lateinit var application: Application

    /*
     */
    @CallSuper @Throws(Exception::class) override fun beforeTest() {
        super.beforeTest()
        this.application = RuntimeEnvironment.application
    }

    /*
     */
    @CallSuper @Throws(Exception::class) override fun afterTest() = super.afterTest()
}