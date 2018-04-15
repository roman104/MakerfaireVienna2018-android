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
package universum.studios.synergy.prototype.observation.view

import dagger.Module
import dagger.android.ContributesAndroidInjector
import universum.studios.synergy.prototype.device.view.DeviceSelectionFragment
import universum.studios.synergy.prototype.device.view.DeviceSelectionFragmentModule
import universum.studios.synergy.prototype.observation.attention.view.AttentionObservationFragment
import universum.studios.synergy.prototype.observation.attention.view.AttentionObservationFragmentModule
import universum.studios.synergy.prototype.observation.meditation.view.MeditationObservationFragment
import universum.studios.synergy.prototype.observation.meditation.view.MeditationObservationFragmentModule

/**
 * @author Martin Albedinsky
 */
@Module
@Suppress("UNUSED")
abstract class ObservationActivityModule {

	@ContributesAndroidInjector(modules = [DeviceSelectionFragmentModule::class])
	abstract fun contributeDeviceSelectionFragmentInjector(): DeviceSelectionFragment

	@ContributesAndroidInjector(modules = [AttentionObservationFragmentModule::class])
	abstract fun contributeAttentionObservationFragmentInjector(): AttentionObservationFragment

	@ContributesAndroidInjector(modules = [MeditationObservationFragmentModule::class])
	abstract fun contributeMeditationObservationFragmentInjector(): MeditationObservationFragment
}