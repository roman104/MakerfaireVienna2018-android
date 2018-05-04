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

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import universum.mind.synergy.R
import universum.mind.synergy.challenge.control.ChallengeController
import universum.mind.synergy.databinding.FragmentChallengeBinding
import universum.mind.synergy.view.BaseFragment
import universum.studios.android.support.dialog.Dialog
import universum.studios.android.support.fragment.annotation.ContentView

/**
 * @author Martin Albedinsky
 */
@ContentView(R.layout.fragment_challenge)
class ChallengeFragment : BaseFragment<ChallengeViewModel, ChallengeController>(), ChallengeView, Dialog.OnDialogListener {

    companion object {

        fun newInstance() = ChallengeFragment()
    }

    private lateinit var binding: FragmentChallengeBinding

    override fun onAttach(context: Context) {
        requestFeature(FEATURE_INJECTION_BASIC)
        super.onAttach(context)
        setDialogXmlFactory(R.xml.dialogs_challenge)
    }

    override fun onBindViews(rootView: View, savedInstanceState: Bundle?) {
        super.onBindViews(rootView, savedInstanceState)
        val viewModel = getViewModel()
        this.binding = FragmentChallengeBinding.bind(rootView).apply { this.viewModel = this@ChallengeFragment.getViewModel() }
        this.binding.uiChildToolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        this.binding.uiChildToolbar.inflateMenu(R.menu.challenge)
        this.binding.uiChildToolbar.setOnMenuItemClickListener(this::onOptionsItemSelected)
        this.binding.chartViewLive.data = viewModel.chartDataLive.get()
        this.binding.chartViewLive.xAxis.valueFormatter = viewModel.chartDataLiveXAxisFormatter
        this.binding.chartViewLive.description.text = getString(R.string.observation_bar_type_live_waves)
        this.binding.chartViewHistogram.data = viewModel.chartDataHistogram.get()
        this.binding.chartViewHistogram.groupBars(0f, 0.5f, 0.25f)
        this.binding.chartViewHistogram.isAutoScaleMinMaxEnabled = true
        this.binding.chartViewHistogram.description.text = getString(R.string.observation_bar_type_histogram)
    }

    override fun onStart() {
        super.onStart()
        getController().startChallenge()
    }

    override fun refreshCharts() {
        this.binding.chartViewLive.notifyDataSetChanged()
        this.binding.chartViewLive.invalidate()
        this.binding.chartViewHistogram.notifyDataSetChanged()
        this.binding.chartViewHistogram.setFitBars(true)
        this.binding.chartViewHistogram.invalidate()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_stop -> {
                showDialogWithId(R.id.dialog_challenge_stop)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDialogButtonClick(dialog: Dialog, button: Int): Boolean {
        return when (dialog.dialogId) {
            R.id.dialog_challenge_stop -> {
                if (button == Dialog.BUTTON_POSITIVE) {
                    binding.uiChildToolbar.menu.findItem(R.id.menu_item_stop).isVisible = false
                    getController().stopChallenge()
                    this.binding.observationBarActualTitle.text = getString(R.string.observation_bar_title_analysis)
                    this.binding.chartViewLive.description.text = getString(R.string.observation_bar_type_total_observed_data)
                    this.binding.chartViewLive.data = getViewModel().chartDataAll.get()
                    this.binding.chartViewLive.invalidate()
                }
                true
            }
            R.id.dialog_challenge_exit -> {
                if (button == Dialog.BUTTON_POSITIVE) {
                    finishActivityWithNavigationalTransition()
                }
                true
            }
            else -> false
        }
    }

    override fun onStop() {
        super.onStop()
        getController().stopChallenge()
    }

    override fun onUnbindViews() {
        super.onUnbindViews()
        this.binding.unbind()
    }

    override fun onBackPress(): Boolean {
        if (getController().isChallengeRunning()) {
            showDialogWithId(R.id.dialog_challenge_exit)
            return true
        }
        return false
    }
}