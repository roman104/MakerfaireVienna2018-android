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
package universum.mind.synergy.observation.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.view.MenuItem
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
import universum.studios.android.support.pager.adapter.FragmentPagerAdapter
import universum.mind.synergy.R
import universum.mind.synergy.device.Device
import universum.mind.synergy.device.view.DeviceSelectionFragment
import universum.mind.synergy.observation.view.presentation.ObservationFragmentsAdapter
import universum.mind.synergy.observation.view.presentation.ObservationSubjectsDialogAdapter
import universum.mind.synergy.view.BaseActivity

/**
 * @author Martin Albedinsky
 */
@ContentView(R.layout.activity_observation)
class ObservationActivity : BaseActivity(), DeviceSelectionFragment.OnDeviceSelectionListener,
        OnPageChangeListener,
        ObservationFragment.OnOptionsItemSelectedListener,
        AdapterProvider,
        OnDialogListener,
        AdapterDialog.OnItemClickListener {

    internal lateinit var selectedDevice: Device
    private lateinit var pagerAdapter: ObservationFragmentsAdapter
    private val fragmentLifecycleCallbacks = object : FragmentLifecycleCallbacks() {

        override fun onFragmentCreated(fragmentManager: FragmentManager, fragment: Fragment, savedInstanceState: Bundle?) {
            if (fragment is ObservationFragment) {
                fragment.setOnOptionsItemSelectedListener(this@ObservationActivity)
            }
        }

        override fun onFragmentDestroyed(fragmentManager: FragmentManager, fragment: Fragment) {
            if (fragment is ObservationFragment) {
                fragment.setOnOptionsItemSelectedListener(null)
            }
        }
    }
    private var subjectsDialogAdapter: ObservationSubjectsDialogAdapter? = null
    private var selectedSubjectFlags = 0
    private var selectedPagePosition = FragmentPagerAdapter.NO_POSITION

    override fun onCreate(savedInstanceState: Bundle?) {
        requestFeature(FEATURE_INJECTION_BASIC)
        super.onCreate(savedInstanceState)
        apply {
            navigationalTransition = ObservationTransition.get()
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, false)
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
        this.selectedDevice = device
        fragmentController.newRequest(findCurrentFragment()!!)
                .transaction(FragmentRequest.REMOVE)
                .transition(FragmentTransitions.CROSS_FADE)
                .execute()
        this.pagerAdapter = ObservationFragmentsAdapter(supportFragmentManager)
        this.pager.adapter = pagerAdapter
        this.pager.addOnPageChangeListener(this)
        this.fab.show()
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (positionOffset == 0f) onPageSelected(position)
    }

    override fun onPageSelected(position: Int) {
        if (selectedPagePosition != position) {
            this.selectedPagePosition = position
        }
    }

    override fun onObservationOptionsItemSelected(fragment: ObservationFragment, item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_remove -> {
                val subject = fragment.getSubject()
                this.selectedSubjectFlags = selectedSubjectFlags and subject.flag.inv()
                this.pagerAdapter.removeObservationSubject(subject)
                true
            }
            else -> false
        }
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
                    this.selectedSubjectFlags = selectedSubjectFlags or selectedSubject.flag
                    this.pagerAdapter.addObservationSubject(selectedSubject)
                    this.pager.setCurrentItem(pagerAdapter.count - 1, true)
                    dialog.dismiss()
                }
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

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentLifecycleCallbacks)
        this.pager.adapter = null
    }
}