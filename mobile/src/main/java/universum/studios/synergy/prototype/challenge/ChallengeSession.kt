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
package universum.studios.synergy.prototype.challenge

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

/**
 * @author Martin Albedinsky
 */
class ChallengeSession private constructor() : Parcelable {

    companion object  CREATOR : Creator<ChallengeSession>  {

        fun create(): ChallengeSession = ChallengeSession()

        override fun createFromParcel(source: Parcel) = ChallengeSession(source)

        override fun newArray(size: Int): Array<ChallengeSession?> = arrayOfNulls(size)
    }

    private var firstParticipantAddress: String = ""
    private var secondParticipantAddress: String = ""

    internal constructor(source: Parcel) : this() {
        this.firstParticipantAddress = source.readString()
        this.secondParticipantAddress = source.readString()
    }

    override fun writeToParcel(destination: Parcel, flags: Int) {
        destination.writeString(firstParticipantAddress)
        destination.writeString(secondParticipantAddress)
    }

    override fun describeContents(): Int = 0

    fun setFirstParticipantAddress(address: String) {
        this.firstParticipantAddress = address
    }

    fun setSecondParticipantDeviceAddress(address: String) {
        this.secondParticipantAddress = address
    }

    fun getFirstParticipantDeviceAddress(): String = firstParticipantAddress
    fun getSecondParticipantDeviceAddress(): String = secondParticipantAddress
}