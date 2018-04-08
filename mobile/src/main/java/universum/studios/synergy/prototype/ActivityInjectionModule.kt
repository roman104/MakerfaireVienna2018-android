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

import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import universum.studios.synergy.prototype.challenge.view.ChallengeActivity
import universum.studios.synergy.prototype.challenge.view.ChallengeActivityModule
import universum.studios.synergy.prototype.welcome.view.WelcomeActivity
import universum.studios.synergy.prototype.welcome.view.WelcomeActivityModule

/**
 * @author Martin Albedinsky
 */
@Module(includes = [AndroidInjectionModule::class, AndroidSupportInjectionModule::class])
internal abstract class ActivityInjectionModule {

    @ContributesAndroidInjector(modules = [WelcomeActivityModule::class])
    internal abstract fun contributeWelcomeActivityInjector(): WelcomeActivity

    @ContributesAndroidInjector(modules = [ChallengeActivityModule::class])
    internal abstract fun contributeChallengeActivityInjector(): ChallengeActivity
}