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

import android.content.Context
import android.os.Bundle
import universum.studios.android.arkhitekton.control.Controller
import universum.studios.android.arkhitekton.view.ViewModel
import universum.studios.android.support.fragment.annotation.ContentView
import universum.studios.mindwave.prototype.R
import universum.studios.mindwave.prototype.view.BaseFragment

/**
 * @author Martin Albedinsky
 */
@ContentView(R.layout.fragment_participants)
class ParticipantsFragment : BaseFragment<ViewModel, Controller<*>>() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        requireActivity().setTitle(R.string.participants_title)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


}