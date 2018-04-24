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
package universum.mind.synergy.device.view

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import universum.mind.synergy.device.Device

/**
 * @author Martin Albedinsky
 */
class DeviceSelectionViewModelImpl : ViewModel(), DeviceSelectionViewModel {

    private val hasDevices = ObservableBoolean()
    private val devicesData = MutableLiveData<List<Device>>()

    override fun hasDevices(): ObservableBoolean = hasDevices

    override fun updateDevices(devices: List<Device>) {
        this.devicesData.value = devices
        this.hasDevices.set(devices.isNotEmpty())
    }

    override fun getDevicesData(): LiveData<List<Device>> = devicesData
}