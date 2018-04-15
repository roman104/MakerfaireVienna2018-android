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
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import universum.studios.android.arkhitekton.control.Controller
import universum.studios.android.arkhitekton.view.ViewModel
import universum.studios.synergy.prototype.R
import universum.studios.synergy.prototype.view.BaseFragment

/**
 * @author Martin Albedinsky
 */
abstract class BaseObservationFragment<VM : ViewModel, C : Controller<*>> : BaseFragment<VM, C>() {

    override fun onBindViews(rootView: View, savedInstanceState: Bundle?) {
        super.onBindViews(rootView, savedInstanceState)
        val toolbar = rootView.findViewById<Toolbar>(R.id.ui_child_toolbar)
        toolbar?.let {
            toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
            toolbar.inflateMenu(R.menu.observation)
            toolbar.setOnMenuItemClickListener { onOptionsItemSelected(it) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_stop -> {
                // todo: remove this fragment from pager adapter ...
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}