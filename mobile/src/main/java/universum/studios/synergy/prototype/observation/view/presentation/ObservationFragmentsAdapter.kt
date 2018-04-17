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
import android.support.v4.util.LongSparseArray
import android.support.v4.view.PagerAdapter
import android.view.ViewGroup
import universum.studios.android.support.pager.adapter.FragmentPagerAdapter
import universum.studios.synergy.prototype.observation.ObservationSubject
import universum.studios.synergy.prototype.observation.attention.view.AttentionObservationFragment
import universum.studios.synergy.prototype.observation.meditation.view.MeditationObservationFragment
import universum.studios.synergy.prototype.observation.view.ObservationFragment

/**
 * @author Martin Albedinsky
 */
class ObservationFragmentsAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    companion object {

        private const val NO_ID = -1L
    }

    private val items: MutableList<ObservationSubject> = ArrayList()
    private val fragments = LongSparseArray<ObservationFragment>()

    fun addObservationSubject(subject: ObservationSubject) {
        items.add(subject)
        notifyDataSetChanged()
    }

    fun removeObservationSubject(subject: ObservationSubject) {
        items.remove(subject)
        notifyDataSetChanged()
    }

    override fun getCount(): Int = items.size

    override fun getItemId(position: Int): Long {
        return if (position >= 0 && position < items.size) items[position].id else NO_ID
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = super.instantiateItem(container, position)
        this.fragments.put(getItemId(position), item as ObservationFragment)
        return item
    }

    override fun getItem(position: Int): Fragment {
        return when (items[position]) {
            ObservationSubject.ATTENTION -> AttentionObservationFragment.newInstance()
            ObservationSubject.MEDITATION -> MeditationObservationFragment.newInstance()
            else -> throw IllegalStateException("Cannot get item for UNSPECIFIED observation subject!")
        }
    }

    fun getFragmentAt(position: Int): ObservationFragment? = fragments.get(getItemId(position))

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        super.destroyItem(container, position, item)
        this.fragments.remove(getItemId(position))
    }
}