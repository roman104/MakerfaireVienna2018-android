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
package universum.studios.mindwave.prototype.analytics

import android.annotation.SuppressLint
import android.app.Application
import android.support.annotation.IntDef
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.core.CrashlyticsCore
import universum.studios.android.analytics.AnalyticsFacade
import universum.studios.android.analytics.ConsoleDestination
import universum.studios.android.analytics.FabricDestination
import universum.studios.android.analytics.screen.ScreenAnalytics
import universum.studios.mindwave.prototype.BuildConfig
import universum.studios.mindwave.prototype.util.Logging

/**
 * @author Martin Albedinsky
 */
abstract class Analytics protected constructor(builder: AnalyticsFacade.FacadeBuilder<*>) : AnalyticsFacade(builder) {

    companion object {

        const val ENVIRONMENT_PRODUCTION = 0
        const val ENVIRONMENT_TEST = 1

        val EMPTY = EmptyAnaltytics.INSTANCE
        private const val SCREEN_ANALYTICS_DESTINATION = FabricDestination.NAME

        @SuppressLint("SwitchIntDef")
        fun buildForEnvironment(application: Application, @Environment environment: Int): Analytics {
            return when(environment) {
                ENVIRONMENT_PRODUCTION -> {
                    val answers = Answers()
                    val crashlytics = Crashlytics.Builder()
                            .core(CrashlyticsCore.Builder().disabled(!Analytics.Fabric.ENABLED).build())
                            .answers(answers)
                            .build()
                    io.fabric.sdk.android.Fabric.with(application, crashlytics, answers)
                    val analytics = AnalyticsImpl.Builder()
                            .addDestination(FabricDestination.create(crashlytics))
                            .addDestination(ConsoleDestination.create(Logging.logger))
                            .build()
                    ScreenAnalytics.Builder().analytics(analytics).destinations(SCREEN_ANALYTICS_DESTINATION).build().install(application)
                    analytics
                }
                ENVIRONMENT_TEST -> AnalyticsImpl.Builder().addDestination(ConsoleDestination.create(Logging.logger)).build()
                else -> AnalyticsImpl.Builder().build()
            }
        }
    }

    @IntDef(ENVIRONMENT_PRODUCTION, ENVIRONMENT_TEST)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Environment

    object Fabric {

        internal const val ENABLED = BuildConfig.BUILD_TYPE != "debug"
    }

    private class EmptyAnaltytics internal constructor(builder: Builder) : Analytics(builder) {

        companion object {

            internal val INSTANCE: Analytics = Builder().build()
        }

        private class Builder : AnalyticsFacade.FacadeBuilder<Builder>() {

            protected override val self = this
            override fun build() = EmptyAnaltytics(this)
        }
    }
}