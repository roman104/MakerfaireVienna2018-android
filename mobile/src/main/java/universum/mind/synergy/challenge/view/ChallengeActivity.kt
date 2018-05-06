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
package universum.mind.synergy.challenge.view

import android.os.Bundle
import universum.mind.synergy.R
import universum.mind.synergy.device.Device
import universum.mind.synergy.device.view.DeviceSelectionFragment
import universum.mind.synergy.view.BaseActivity
import universum.studios.android.support.dialog.Dialog
import universum.studios.android.support.fragment.annotation.ContentView
import universum.studios.android.support.fragment.transition.FragmentTransitions

/**
 * @author Martin Albedinsky
 */
@ContentView(R.layout.activity_container)
class ChallengeActivity : BaseActivity(), DeviceSelectionFragment.OnDeviceSelectionListener, Dialog.OnDialogListener {

    internal lateinit var selectedDevice: Device

    override fun onCreate(savedInstanceState: Bundle?) {
        requestFeature(FEATURE_INJECTION_BASIC)
        super.onCreate(savedInstanceState)
        navigationalTransition = ChallengeTransition.get()
        setDialogXmlFactory(R.xml.dialogs_challenge_startup)
        if (savedInstanceState == null) {
            val fragment = DeviceSelectionFragment.newInstance()
            fragment.setOnDeviceSelectionListener(this)
            fragmentController.newRequest(fragment).immediate(true).execute()
        }
    }

    override fun onDeviceSelected(device: Device) {
        this.selectedDevice = device
        showDialogWithId(R.id.dialog_challenge_startup_choose_level)
    }

    override fun onDialogButtonClick(dialog: Dialog, button: Int): Boolean {
        return when (dialog.dialogId) {
            R.id.dialog_challenge_startup_choose_level -> {
                val level = (dialog as ChooseChallengeLevelDialog).getSelectedLevel()
                dialog.dismiss()
                fragmentController.newRequest(ChallengeFragment.newInstance())
                        .arguments(Bundle().apply { putInt(ChallengeFragment.ARGUMENT_LEVEL, level) })
                        .replaceSame(true)
                        .transition(FragmentTransitions.CROSS_FADE)
                        .execute()
                true
            }
            else -> false
        }
    }
}