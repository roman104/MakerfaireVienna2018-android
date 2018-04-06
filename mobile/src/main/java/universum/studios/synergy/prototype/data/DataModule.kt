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
package universum.studios.synergy.prototype.data

import android.app.Application
import dagger.Module

/**
 * @author Martin Albedinsky
 */
@Module class DataModule(application: Application) {

    init {
        /*val sharedPreferences = PreferencesModule(application).providePrimaryPreferences()
        DataSources.apply {
            // todo: build repository here
            primaryParamsDataSource = PrimaryParamsRepository.get()
            // todo: build repository here
            secondaryParamsDataSource = SecondaryParamsRepository.get()
            suppliesDataSource = SuppliesRepository(sharedPreferences)
            clinicDataSource = ClinicRepository.Builder().apply {
                this.sharedPreferences = sharedPreferences
            }.build()
        }*/
    }

    /*@Deprecated("")
    @Provides fun provideClinicRepository(): ClinicRepositoryInterface = provideClinicDataSource() as ClinicRepositoryInterface

    @Provides fun provideClinicDataSource(): ClinicDataSource = DataSources.clinicDataSource

    @Provides fun providePrimaryParamsDataSource(): PrimaryParamsDataSource = DataSources.primaryParamsDataSource

    @Provides fun provideSecondaryParamsDataSource(): SecondaryParamsDataSource = DataSources.secondaryParamsDataSource

    @Provides fun provideSuppliesDataSource(): SuppliesDataSource = DataSources.suppliesDataSource*/
}