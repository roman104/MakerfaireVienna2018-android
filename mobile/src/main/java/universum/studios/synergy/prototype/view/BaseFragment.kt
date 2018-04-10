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
package universum.studios.synergy.prototype.view

import android.content.Context
import android.support.annotation.IntDef
import android.support.annotation.IntRange
import dagger.android.support.AndroidSupportInjection
import universum.studios.android.arkhitekton.control.Controller
import universum.studios.android.arkhitekton.presentation.Presenter
import universum.studios.android.arkhitekton.util.Preconditions
import universum.studios.android.arkhitekton.view.ViewModel
import universum.studios.android.support.dialog.DialogOptions
import universum.studios.android.support.universi.UniversiFragment
import universum.studios.android.util.Flags
import javax.inject.Inject

/**
 * @author Martin Albedinsky
 */
abstract class BaseFragment<VM : ViewModel, C : Controller<*>> : UniversiFragment(), ScreenView<VM> {

    companion object {

        const val FEATURE_INJECTION_BASIC = 0x00000001
    }

    @IntDef(flag = true, value = [
        FEATURE_INJECTION_BASIC
    ])
    @Retention(AnnotationRetention.SOURCE)
    protected annotation class Feature

    private val features = Flags(0)
    private var viewModel: VM? = null
    private var controller: C? = null

    protected fun requestFeature(@Feature feature: Int) = features.add(feature)

    protected fun hasFeature(@Feature feature: Int) = features.has(feature)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (hasFeature(FEATURE_INJECTION_BASIC)) {
            onInject()
        }
    }

    protected open fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    @Inject
    protected fun attachViewModel(viewModel: VM) {
        this.viewModel = viewModel
        onViewModelAttached(viewModel)
    }

    protected open fun onViewModelAttached(viewModel: VM) {}

    override fun getViewModel(): VM = Preconditions.checkNotNull(viewModel, "No view model attached!")

    protected fun detachViewModel() {
        if (viewModel != null) {
            val detachedViewModel = this.viewModel!!
            this.viewModel = null
            onViewModelDetached(detachedViewModel)
        }
    }

    protected open fun onViewModelDetached(viewModel: VM) {}

    @Inject
    protected fun attachController(controller: C) {
        this.controller = controller
        onControllerAttached(controller)
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun onControllerAttached(controller: C) {
        val presenter = controller.getPresenter() as Presenter<ScreenView<VM>>
        presenter.attachView(this)
    }

    protected fun getController(): C = Preconditions.checkNotNull(controller, "No controller attached!")

    protected fun detachController() {
        if (controller != null) {
            val detachedController = this.controller!!
            this.controller = null
            onControllerDetached(detachedController)
        }
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun onControllerDetached(controller: C) {
        val presenter = controller.getPresenter() as Presenter<ScreenView<VM>>
        presenter.detachView(this)
    }

    public override fun showDialogWithId(@IntRange(from = 0) dialogId: Int, options: DialogOptions<*>?) = super.showDialogWithId(dialogId, options)
    public override fun dismissDialogWithId(@IntRange(from = 0) dialogId: Int) = super.dismissDialogWithId(dialogId)

    override fun onDestroy() {
        // Detach controller with view model before view models store is cleared.
        this.detachController()
        this.detachViewModel()
        super.onDestroy()
    }
}