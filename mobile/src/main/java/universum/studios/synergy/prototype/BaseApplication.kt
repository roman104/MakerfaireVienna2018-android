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
package universum.studios.synergy.prototype

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import universum.studios.android.arkhitekton.util.Preconditions
import universum.studios.synergy.prototype.analytics.Analytics
import universum.studios.synergy.prototype.data.DataModule
import javax.inject.Inject

/**
 * @author Martin Albedinsky
 */
abstract class BaseApplication : Application(), HasActivityInjector {

    companion object {

        @Analytics.Environment internal var analyticsEnvironment = Analytics.ENVIRONMENT_PRODUCTION
    }

    private var component: ApplicationComponent? = null
    private lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        // NOTE: Apply configuration after MultiDex has been installed so all required classes for
        // NOTE: configuration may be properly looked up.
        Config.apply()
    }

    override fun onCreate() {
        super.onCreate()
        onInitialize()
        setComponent(DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this, Analytics.buildForEnvironment(this, analyticsEnvironment)))
                //.preferencesModule(PreferencesModule(this))
                .dataModule(DataModule(this))
                .build()
        )
    }

    protected open fun onInitialize() {}

    fun setComponent(component: ApplicationComponent) {
        this.component = component
        this.component!!.inject(this)
    }

    fun getComponent(): ApplicationComponent = Preconditions.checkNotNull(component, "No component attached!")

    @Inject fun attachActivityInjector(injector: DispatchingAndroidInjector<Activity>) {
        this.activityInjector = injector
    }

    override fun activityInjector(): AndroidInjector<Activity> = Preconditions.checkNotNull(activityInjector, "No activity injector attached!")
}