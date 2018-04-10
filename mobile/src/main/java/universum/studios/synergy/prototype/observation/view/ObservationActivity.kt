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

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_observation.*
import universum.studios.android.support.fragment.annotation.ContentView
import universum.studios.android.support.fragment.manage.FragmentRequest
import universum.studios.android.support.fragment.transition.FragmentTransitions
import universum.studios.synergy.prototype.R
import universum.studios.synergy.prototype.device.Device
import universum.studios.synergy.prototype.device.view.DeviceSelectionFragment
import universum.studios.synergy.prototype.util.Logging
import universum.studios.synergy.prototype.view.BaseActivity

/**
 * @author Martin Albedinsky
 */
@ContentView(R.layout.activity_observation)
class ObservationActivity : BaseActivity(), DeviceSelectionFragment.OnDeviceSelectionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestFeature(FEATURE_INJECTION_BASIC)
        super.onCreate(savedInstanceState)
        apply { navigationalTransition = ObservationTransition.get() }
        if (savedInstanceState == null) {
            val deviceSelectionFragment = DeviceSelectionFragment()
            deviceSelectionFragment.setOnDeviceSelectionListener(this)
            fragmentController.newRequest(deviceSelectionFragment)
                    .immediate(true)
                    .execute()
        }
    }

    override fun onDeviceSelected(device: Device) {
        Logging.d(name(), "onDeviceSelected($device)")
        fragmentController.newRequest(findCurrentFragment()!!)
                .transaction(FragmentRequest.REMOVE)
                .transition(FragmentTransitions.CROSS_FADE)
                .execute()
        this.fab.show()
    }
}