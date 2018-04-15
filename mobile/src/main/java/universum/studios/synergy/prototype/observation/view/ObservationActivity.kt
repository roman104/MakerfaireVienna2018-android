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
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_observation.*
import universum.studios.android.support.dialog.AdapterDialog
import universum.studios.android.support.dialog.BaseAdapterDialog.AdapterProvider
import universum.studios.android.support.dialog.Dialog
import universum.studios.android.support.dialog.Dialog.OnDialogListener
import universum.studios.android.support.fragment.annotation.ContentView
import universum.studios.android.support.fragment.manage.FragmentRequest
import universum.studios.android.support.fragment.transition.FragmentTransitions
import universum.studios.synergy.prototype.R
import universum.studios.synergy.prototype.device.Device
import universum.studios.synergy.prototype.device.view.DeviceSelectionFragment
import universum.studios.synergy.prototype.observation.view.presentation.ObservationFragmentsAdapter
import universum.studios.synergy.prototype.observation.view.presentation.ObservationSubjectsDialogAdapter
import universum.studios.synergy.prototype.view.BaseActivity

/**
 * @author Martin Albedinsky
 */
@ContentView(R.layout.activity_observation)
class ObservationActivity : BaseActivity(), DeviceSelectionFragment.OnDeviceSelectionListener,
        AdapterProvider,
        OnDialogListener,
        AdapterDialog.OnItemClickListener {

    private lateinit var pagerAdapter: ObservationFragmentsAdapter
    private var subjectsDialogAdapter: ObservationSubjectsDialogAdapter? = null
    private var selectedSubjectFlags = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        requestFeature(FEATURE_INJECTION_BASIC)
        super.onCreate(savedInstanceState)
        apply {
            navigationalTransition = ObservationTransition.get()
        }
        setDialogXmlFactory(R.xml.dialogs_observation)
        if (savedInstanceState == null) {
            val deviceSelectionFragment = DeviceSelectionFragment()
            deviceSelectionFragment.setOnDeviceSelectionListener(this)
            fragmentController.newRequest(deviceSelectionFragment)
                    .immediate(true)
                    .execute()
        }
    }

    override fun onContentChanged() {
        super.onContentChanged()
        this.fab.setOnClickListener { showDialogWithId(R.id.dialog_observation_subjects) }
    }

    override fun onDeviceSelected(device: Device) {
        fragmentController.newRequest(findCurrentFragment()!!)
                .transaction(FragmentRequest.REMOVE)
                .transition(FragmentTransitions.CROSS_FADE)
                .execute()
        this.pagerAdapter = ObservationFragmentsAdapter(supportFragmentManager, device)
        this.pager.adapter = pagerAdapter
        this.fab.show()
    }

    override fun provideDialogAdapter(dialog: Dialog): Any? {
        return when(dialog.dialogId) {
            R.id.dialog_observation_subjects -> {
                if (subjectsDialogAdapter == null) {
                    this.subjectsDialogAdapter = ObservationSubjectsDialogAdapter(this, dialog.dialogLayoutInflater!!)
                }
                this.subjectsDialogAdapter?.setSelectedSubjectFlags(selectedSubjectFlags)
                return subjectsDialogAdapter
            }
            else -> null
        }
    }

    override fun onDialogItemClick(dialog: AdapterDialog<out AdapterView<*>>, adapterView: AdapterView<*>, view: View, position: Int, id: Long): Boolean {
        return when (dialog.dialogId) {
            R.id.dialog_observation_subjects -> {
                val selectedSubject = subjectsDialogAdapter!!.getItem(position)
                if ((selectedSubjectFlags and selectedSubject.flag) == 0) {
                    this.selectedSubjectFlags != selectedSubject.flag
                    this.pagerAdapter.addObservationSubject(selectedSubject)
                }
                dialog.dismiss()
                true
            }
            else -> false
        }
    }

    override fun onDialogButtonClick(dialog: Dialog, button: Int): Boolean {
        return when (dialog.dialogId) {
            R.id.dialog_observation_exit -> {
                if (button == Dialog.BUTTON_POSITIVE) {
                    finishWithNavigationalTransition()
                }
                true
            }
            else -> false
        }
    }

    override fun onBackPress(): Boolean {
        if (findCurrentFragment() == null) {
            showDialogWithId(R.id.dialog_observation_exit)
            return true
        }
        return false
    }
}