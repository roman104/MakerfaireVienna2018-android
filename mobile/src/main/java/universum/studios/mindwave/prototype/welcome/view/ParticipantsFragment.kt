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
package universum.studios.mindwave.prototype.welcome.view

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.fragment_participants.*
import universum.studios.android.support.fragment.annotation.ContentView
import universum.studios.android.support.fragment.annotation.MenuOptions
import universum.studios.mindwave.prototype.R
import universum.studios.mindwave.prototype.challenge.ChallengeSession
import universum.studios.mindwave.prototype.challenge.view.ChallengeTransition
import universum.studios.mindwave.prototype.view.BaseFragment
import universum.studios.mindwave.prototype.welcome.control.ParticipantsController
import universum.studios.mindwave.prototype.welcome.view.presentation.BluetoothDevicesSpinnerAdapter

/**
 * @author Martin Albedinsky
 */
@MenuOptions(R.menu.participants)
@ContentView(R.layout.fragment_participants)
class ParticipantsFragment : BaseFragment<ParticipantsViewModel, ParticipantsController>() {

    internal val firstParticipantDevicesAdapter: BluetoothDevicesSpinnerAdapter by lazy { BluetoothDevicesSpinnerAdapter(requireContext()) }
    internal val secondParticipantDevicesAdapter: BluetoothDevicesSpinnerAdapter by lazy { BluetoothDevicesSpinnerAdapter(requireContext()) }

    override fun onAttach(context: Context?) {
        requestFeature(FEATURE_INJECTION_BASIC)
        super.onAttach(context)
        requireActivity().setTitle(R.string.participants_title)
    }

    override fun onBindViews(rootView: View, savedInstanceState: Bundle?) {
        super.onBindViews(rootView, savedInstanceState)
        this.first_participant_devices_spinner.adapter = firstParticipantDevicesAdapter
        this.second_participant_devices_spinner.adapter = secondParticipantDevicesAdapter
        this.start.setOnClickListener {
            val session = ChallengeSession.create()
            val firstParticipantDevice = firstParticipantDevicesAdapter.selectedItem
            val secondParticipantDevice = secondParticipantDevicesAdapter.selectedItem
            session.setFirstParticipantAddress(firstParticipantDevice!!.address)
            session.setSecondParticipantDeviceAddress(secondParticipantDevice!!.address)
            ChallengeTransition.get().session(session).start(this)
        }
        this.start.isEnabled = false
        getViewModel().getAvailableDevices().observe(this, Observer { devices ->
            firstParticipantDevicesAdapter.changeItems(devices)
            secondParticipantDevicesAdapter.changeItems(devices)
            start.isEnabled = devices!!.count() > 1
        })
    }

    override fun onStart() {
        super.onStart()
        getController().startDevicesScan()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_item_refresh -> {
                getController().restartDevicesScan()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        super.onStop()
        getController().stopDevicesScan()
    }
}