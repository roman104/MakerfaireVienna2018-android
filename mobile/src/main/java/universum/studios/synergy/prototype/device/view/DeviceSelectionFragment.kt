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
package universum.studios.synergy.prototype.device.view

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.fragment_device_selection.*
import universum.studios.android.support.fragment.annotation.ContentView
import universum.studios.synergy.prototype.R
import universum.studios.synergy.prototype.databinding.FragmentDeviceSelectionBinding
import universum.studios.synergy.prototype.device.Device
import universum.studios.synergy.prototype.device.control.DeviceSelectionController
import universum.studios.synergy.prototype.device.view.presentation.DevicesSelectionAdapter
import universum.studios.synergy.prototype.view.BaseFragment

/**
 * @author Martin Albedinsky
 */
@ContentView(R.layout.fragment_device_selection)
class DeviceSelectionFragment : BaseFragment<DeviceSelectionViewModel, DeviceSelectionController>() {

    interface OnDeviceSelectionListener {

        fun onDeviceSelected(device: Device)
    }

    private var selectionListener: OnDeviceSelectionListener? = null
    private lateinit var binding: FragmentDeviceSelectionBinding
    private val devicesAdapter by lazy { DevicesSelectionAdapter(requireContext()) }

    fun setOnDeviceSelectionListener(listener: OnDeviceSelectionListener?) {
        this.selectionListener = listener
    }

    internal fun notifyDeviceSelected(device: Device) = selectionListener?.onDeviceSelected(device)

    override fun onAttach(context: Context) {
        requestFeature(FEATURE_INJECTION_BASIC)
        super.onAttach(context)
    }

    override fun onBindViews(rootView: View, savedInstanceState: Bundle?) {
        super.onBindViews(rootView, savedInstanceState)
        this.binding = FragmentDeviceSelectionBinding.bind(rootView).apply {
            viewModel = super.getViewModel()
        }
        this.ui_child_toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        this.ui_child_toolbar.inflateMenu(R.menu.device_selection)
        this.ui_child_toolbar.setOnMenuItemClickListener { onOptionsItemSelected(it) }
        this.list.apply {
            layoutManager = LinearLayoutManager(rootView.context, LinearLayoutManager.VERTICAL, false)
            adapter = devicesAdapter
        }
        this.devicesAdapter.registerOnDataSetActionListener { _, position, _, _ ->
            notifyDeviceSelected(devicesAdapter.getItem(position))
            true
        }
    }

    override fun onStart() {
        super.onStart()
        getViewModel().getDevicesData().observe(this, Observer { devices ->
            devicesAdapter.changeItems(devices)
        })
        getController().startDevicesDiscovery()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_refresh -> {
                getController().restartDevicesDiscovery()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        super.onStop()
        getController().stopDevicesDiscovery()
    }
}