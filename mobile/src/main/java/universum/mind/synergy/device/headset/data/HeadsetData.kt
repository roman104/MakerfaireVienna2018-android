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
package universum.mind.synergy.device.headset.data

import universum.studios.android.arkhitekton.util.Preconditions
import universum.mind.synergy.device.headset.Headset

/**
 * @author Martin Albedinsky
 */
open class HeadsetData protected constructor(builder: DataBuilder<*>){

    val deviceAddress = Preconditions.checkNotNull(builder.deviceAddress, "No device address specified!")
    val subject = Preconditions.checkNotNull(builder.subject, "No observation subject specified!")
    val timeObserved = builder.timeObserved

    abstract class DataBuilder<out D : HeadsetData> {

        lateinit var deviceAddress: String
        lateinit var subject: Headset.ObservationSubject
        var timeObserved: Long = System.currentTimeMillis()

        abstract fun build(): D
    }
}