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

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.utils.Utils
import universum.studios.android.arkhitekton.view.ViewModel
import universum.studios.android.util.BundleKey
import universum.mind.synergy.R
import universum.mind.synergy.device.headset.Headset
import universum.mind.synergy.observation.control.ObservationController
import universum.mind.synergy.util.Logging
import universum.mind.synergy.view.BaseFragment
import universum.mind.synergy.view.PageFragment

/**
 * @author Martin Albedinsky
 */
abstract class BaseObservationFragment<VM : ViewModel, C : ObservationController<*>> : BaseFragment<VM, C>(), ObservationFragment, ObservationView<VM> {

    companion object {

        val ARGUMENT_OBSERVATION_SUBJECT = BundleKey.argument(ObservationFragment::class.java, "ObservationSubject")

        fun createArguments(subject: Headset.ObservationSubject) = Bundle().apply { putString(ARGUMENT_OBSERVATION_SUBJECT, subject.name) }
    }

    private var position = PageFragment.NO_POSITION
    private var primary = false
    private lateinit var subject: Headset.ObservationSubject
    private var optionsItemSelectedListener: ObservationFragment.OnOptionsItemSelectedListener? = null
    private lateinit var menu: Menu
    private var chartView: Chart<*>? = null

    override fun setPosition(position: Int) {
        if (this.position != position) {
            this.position = position
        }
    }

    override fun getPosition(): Int = position

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Utils.init(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.subject = Headset.ObservationSubject.valueOf(arguments?.getString(ARGUMENT_OBSERVATION_SUBJECT) ?: Headset.ObservationSubject.UNSPECIFIED.name)
        Logging.d(name(), "Created with subject '$subject'.")
    }

    override fun getContentId(): Long = subject.id

    override fun getSubject(): Headset.ObservationSubject = subject

    override fun setOnOptionsItemSelectedListener(listener: ObservationFragment.OnOptionsItemSelectedListener?) {
        this.optionsItemSelectedListener = listener
    }

    override fun onBindViews(rootView: View, savedInstanceState: Bundle?) {
        super.onBindViews(rootView, savedInstanceState)
        val toolbar = rootView.findViewById<Toolbar>(R.id.ui_child_toolbar)
        toolbar?.let {
            toolbar.setTitle(subject.nameRes)
            toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
            toolbar.inflateMenu(R.menu.observation)
            this.menu = toolbar.menu
            toolbar.setOnMenuItemClickListener { onOptionsItemSelected(it) }
        }
        this.chartView = rootView.findViewById(R.id.chart_view)
    }

    override fun setPrimary(primary: Boolean) {
        if (this.primary != primary) {
            this.primary = primary
        }
    }

    override fun isPrimary(): Boolean = primary

    override fun onStart() {
        super.onStart()
        getController().startObservation()
    }

    override fun refreshChart() {
        this.chartView?.notifyDataSetChanged()
        this.chartView?.invalidate()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_stop -> {
                getController().stopObservation()
                item.isVisible = false
                menu.findItem(R.id.menu_item_download).isVisible = true
                menu.findItem(R.id.menu_item_remove).isVisible = true
                true
            }
            R.id.menu_item_download -> false
            else -> return optionsItemSelectedListener?.onObservationOptionsItemSelected(this, item) ?: false
        }
    }

    override fun onStop() {
        super.onStop()
        getController().stopObservation()
    }

    override fun onDetach() {
        super.onDetach()
        this.optionsItemSelectedListener = null
    }
}