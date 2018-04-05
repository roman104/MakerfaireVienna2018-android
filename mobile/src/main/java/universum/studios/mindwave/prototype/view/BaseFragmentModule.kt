/*
 * =================================================================================================
 *                                    Copyright (C) 2017 SurgLogs
 * =================================================================================================
 */
package universum.studios.mindwave.prototype.view

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import universum.studios.android.arkhitekton.control.Controller

/**
 * @author Martin Albedinsky
 */
abstract class BaseFragmentModule {

    companion object {

        fun <T : Controller.Holder<*>> provideControllerHolder(fragment: Fragment, holderClass: Class<T>): T = provideViewModelProvider(fragment).get(holderClass)
        fun <T : ViewModel> provideViewModel(fragment: Fragment, modelClass: Class<T>) = provideViewModelProvider(fragment).get(modelClass)
        fun provideViewModelProvider(fragment: Fragment) = ViewModelProviders.of(fragment)
        fun provideViewModelProvider(fragment: Fragment, factory: ViewModelProvider.Factory) = ViewModelProviders.of(fragment, factory)
    }
}