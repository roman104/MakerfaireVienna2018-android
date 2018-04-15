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
package universum.studios.synergy.prototype.observation.meditation.control

import android.bluetooth.BluetoothAdapter
import android.content.Context
import universum.studios.android.arkhitekton.control.ReactiveController
import universum.studios.android.arkhitekton.interaction.Interactor
import universum.studios.synergy.prototype.device.Device
import universum.studios.synergy.prototype.device.headset.Headset
import universum.studios.synergy.prototype.device.headset.MeditationListener
import universum.studios.synergy.prototype.device.headset.data.MeditationData
import universum.studios.synergy.prototype.device.headset.neurosky.NeuroSkyHeadset
import universum.studios.synergy.prototype.observation.meditation.view.presentation.MeditationObservationPresenter

/**
 * @author Martin Albedinsky
 */
class DefaultMeditationObservationController internal constructor(builder: Builder)
    : ReactiveController<Interactor, MeditationObservationPresenter>(builder), MeditationObservationController {

    companion object {
    
    }

    private val context = builder.context
    private val bluetoothAdapter = builder.bluetoothAdapter
    private val device = builder.device
    private var headset: Headset? = null

    override fun onActivated() {
        super.onActivated()
        startObservation()
    }

    override fun startObservation() {
        this.headset = NeuroSkyHeadset(context, bluetoothAdapter.getRemoteDevice(device.address))
        this.headset?.registerMeditationListener(object : MeditationListener {

            override fun onMeditationChanged(data: MeditationData) {
                getPresenter().onMeditationChanged(data)
            }
        })
        this.headset?.connect()
    }

    override fun stopObservation() {
        this.headset?.disconnect()
        this.headset = null
    }

    override fun onDeactivated() {
        super.onDeactivated()
        stopObservation()
    }
    
    class Builder(
        interactor: Interactor,
        presenter: MeditationObservationPresenter
    ) : ReactiveController.BaseBuilder<Builder, Interactor, MeditationObservationPresenter>(interactor, presenter) {
    
        override val self = this
        lateinit var context: Context
        lateinit var bluetoothAdapter: BluetoothAdapter
        lateinit var device: Device

        override fun build() = DefaultMeditationObservationController(this)
    }
}