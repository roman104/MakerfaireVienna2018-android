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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import universum.studios.android.widget.adapter.SimpleListAdapter
import universum.studios.android.widget.adapter.holder.ViewHolder
import universum.studios.synergy.prototype.R
import universum.studios.synergy.prototype.databinding.ItemObservationSubjectListBinding
import universum.studios.synergy.prototype.observation.ObservationSubject

/**
 * @author Martin Albedinsky
 */
class ObservationSubjectsDialogAdapter(context: Context, private val layoutInflater: LayoutInflater)
    : SimpleListAdapter<ObservationSubjectsDialogAdapter, ObservationSubjectsDialogAdapter.ItemHolder, ObservationSubject>(
        context,
        ObservationSubject.values().filter { it != ObservationSubject.UNSPECIFIED }) {

    private var selectedSubjectFlags = 0

    fun setSelectedSubjectFlags(flags: Int) {
        if (selectedSubjectFlags != flags) {
            this.selectedSubjectFlags = flags
            notifyDataSetChanged()
        }
    }

    override fun inflateView(resource: Int, parent: ViewGroup?): View = layoutInflater.inflate(resource, parent, false)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(inflateView(R.layout.item_observation_subject_list, parent))
    }

    override fun onBindViewHolder(viewHolder: ItemHolder, position: Int) {
        val subject = getItem(position)
        viewHolder.binding.observationSubject = subject
        viewHolder.binding.selectable = (selectedSubjectFlags and subject.flag) == 0
        viewHolder.binding.executePendingBindings()
    }

    inner class ItemHolder(itemView: View) : ViewHolder(itemView) {

        internal val binding = ItemObservationSubjectListBinding.bind(itemView)
    }
}