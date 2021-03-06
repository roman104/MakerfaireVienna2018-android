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
package universum.mind.synergy.view

import android.os.Bundle
import android.support.annotation.IntDef
import android.support.v4.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import universum.mind.synergy.R
import universum.studios.android.arkhitekton.util.Preconditions
import universum.studios.android.support.universi.UniversiCompatActivity
import universum.studios.android.util.Flags
import javax.inject.Inject

/**
 * @author Martin Albedinsky
 */
abstract class BaseActivity : UniversiCompatActivity(), HasSupportFragmentInjector {

    companion object {

        const val FEATURE_INJECTION_BASIC = 0x00000001
        const val FEATURE_INJECTION_UI = 0x00000001 shl 1
    }

    @IntDef(flag = true, value = [
        FEATURE_INJECTION_BASIC,
        FEATURE_INJECTION_UI
    ])
    @Retention(AnnotationRetention.SOURCE)
    protected annotation class Feature

    private val features = Flags(0)
    private var fragmentInjector: DispatchingAndroidInjector<Fragment>? = null
    private var unbinder: Unbinder? = null

    fun name(): String = javaClass.simpleName

    protected fun requestFeature(@Feature feature: Int) = features.add(feature)

    protected fun hasFeature(@Feature feature: Int) = features.has(feature)

    override fun onCreate(savedInstanceState: Bundle?) {
        if (hasFeature(FEATURE_INJECTION_BASIC)) {
            onInject()
        }
        fragmentController.viewContainerId = R.id.ui_container
        super.onCreate(savedInstanceState)
    }

    protected open fun onInject() {
        AndroidInjection.inject(this)
    }

    @Inject fun attachFragmentInjector(injector: DispatchingAndroidInjector<Fragment>) {
        this.fragmentInjector = injector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = Preconditions.checkNotNull(fragmentInjector, "No fragment injector attached!")

    override fun onBindViews() {
        super.onBindViews()
        if (hasFeature(FEATURE_INJECTION_UI)) {
            this.unbinder =  ButterKnife.bind(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.unbinder?.unbind()
        this.unbinder = null
    }
}