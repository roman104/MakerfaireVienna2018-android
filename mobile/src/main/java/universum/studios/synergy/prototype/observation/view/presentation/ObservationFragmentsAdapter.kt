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
package universum.studios.synergy.prototype.observation.view.presentation

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.PagerAdapter
import universum.studios.android.support.pager.adapter.FragmentPagerAdapter
import universum.studios.synergy.prototype.device.Device
import universum.studios.synergy.prototype.observation.ObservationSubject
import universum.studios.synergy.prototype.observation.attention.view.AttentionObservationFragment
import universum.studios.synergy.prototype.observation.meditation.view.MeditationObservationFragment

/**
 * @author Martin Albedinsky
 */
class ObservationFragmentsAdapter(fragmentManager: FragmentManager, private val device: Device) : FragmentPagerAdapter(fragmentManager) {

    private val items: MutableList<ObservationSubject> = ArrayList()

    fun addObservationSubject(subject: ObservationSubject) {
        items.add(subject)
        notifyDataSetChanged()
    }

    fun removeObservationSubject(subject: ObservationSubject) {
        items.remove(subject)
        notifyDataSetChanged()
    }

    override fun getCount(): Int = items.size

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getItem(position: Int): Fragment {
        return when (items[position]) {
            ObservationSubject.ATTENTION -> AttentionObservationFragment.newInstance(device)
            ObservationSubject.MEDITATION -> MeditationObservationFragment.newInstance(device)
        }
    }
}