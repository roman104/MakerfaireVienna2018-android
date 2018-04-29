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

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test
import universum.studios.android.test.local.LocalTestCase

/**
 * @author Martin Albedinsky
 */
class DateUtilsTest : LocalTestCase() {

    @Test fun testFormatterFormatElapsedTime() {
        assertThat(DateUtils.Formatter.formatElapsedTime(0), `is`(""))
        assertThat(DateUtils.Formatter.formatElapsedTime(1000), `is`("1s"))
        assertThat(DateUtils.Formatter.formatElapsedTime(5000), `is`("5s"))
        assertThat(DateUtils.Formatter.formatElapsedTime(1000 * 60), `is`("1m 0s"))
        assertThat(DateUtils.Formatter.formatElapsedTime(1000 * 60 + 5000), `is`("1m 5s"))
        assertThat(DateUtils.Formatter.formatElapsedTime(1000 * 60 * 60), `is`("1h 0m 0s"))
        assertThat(DateUtils.Formatter.formatElapsedTime(1000 * 60 * 60 + 5000), `is`("1h 0m 5s"))
    }
}