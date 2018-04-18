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
package universum.studios.synergy.prototype.device.view.presentation

import android.content.Context
import android.view.View
import android.view.ViewGroup
import universum.studios.android.widget.adapter.SimpleRecyclerAdapter
import universum.studios.android.widget.adapter.holder.RecyclerViewHolder
import universum.studios.synergy.prototype.R
import universum.studios.synergy.prototype.databinding.ItemDeviceListBinding
import universum.studios.synergy.prototype.device.Device

/**
 * @author Martin Albedinsky
 */
class DevicesSelectionAdapter(context: Context) : SimpleRecyclerAdapter<
        DevicesSelectionAdapter,
        DevicesSelectionAdapter.ItemHolder,
        Device>(context) {

    companion object {

        const val ACTION_CLICK = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder = ItemHolder(inflateView(R.layout.item_device_list, parent))

    override fun onBindViewHolder(viewHolder: ItemHolder, position: Int) {
        viewHolder.binding.device = getItem(position)
        viewHolder.binding.executePendingBindings()
    }

    inner class ItemHolder(itemView: View) : RecyclerViewHolder(itemView), View.OnClickListener {

        internal val binding = ItemDeviceListBinding.bind(itemView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            notifyDataSetActionSelected(ACTION_CLICK, adapterPosition, null)
        }
    }
}