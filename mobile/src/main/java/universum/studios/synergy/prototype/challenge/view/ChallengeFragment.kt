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
package universum.studios.synergy.prototype.challenge.view

import android.content.Context
import android.os.Bundle
import android.view.View
import universum.studios.android.support.fragment.annotation.ContentView
import universum.studios.android.util.BundleKey
import universum.studios.synergy.prototype.R
import universum.studios.synergy.prototype.challenge.ChallengeSession
import universum.studios.synergy.prototype.challenge.control.ChallengeController
import universum.studios.synergy.prototype.databinding.FragmentChallengeBinding
import universum.studios.synergy.prototype.view.BaseFragment

/**
 * @author Martin Albedinsky
 */
@ContentView(R.layout.fragment_challenge)
class ChallengeFragment : BaseFragment<ChallengeViewModel, ChallengeController>(), ChallengeView {

    companion object {

        val ARGUMENT_SESSION = BundleKey.argument(ChallengeFragment::class.java, "Session")

        fun newInstance(session: ChallengeSession) = ChallengeFragment().apply { arguments = createArguments(session) }

        fun createArguments(session: ChallengeSession) = Bundle().apply { putParcelable(ARGUMENT_SESSION, session) }
    }

    private lateinit var binding: FragmentChallengeBinding

    override fun onAttach(context: Context?) {
        requestFeature(FEATURE_INJECTION_BASIC)
        super.onAttach(context)
    }

    override fun onBindViews(rootView: View, savedInstanceState: Bundle?) {
        super.onBindViews(rootView, savedInstanceState)
        this.binding = FragmentChallengeBinding.bind(rootView).apply { this.viewModel = this@ChallengeFragment.getViewModel() }
    }

    override fun onStart() {
        super.onStart()
        getController().startChallenge()
    }

    override fun onStop() {
        super.onStop()
        getController().stopChallenge()
    }
}