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
package universum.studios.synergy.prototype.observation.attention.control

import android.bluetooth.BluetoothAdapter
import android.content.Context
import universum.studios.android.arkhitekton.control.ReactiveController
import universum.studios.android.arkhitekton.interaction.Interactor
import universum.studios.synergy.prototype.device.Device
import universum.studios.synergy.prototype.device.headset.AttentionListener
import universum.studios.synergy.prototype.device.headset.Headset
import universum.studios.synergy.prototype.device.headset.data.AttentionData
import universum.studios.synergy.prototype.device.headset.neurosky.NeuroSkyHeadset
import universum.studios.synergy.prototype.observation.attention.view.presentation.AttentionObservationPresenter

/**
 * @author Martin Albedinsky
 */
class DefaultAttentionObservationController internal constructor(builder: Builder) : ReactiveController<Interactor, AttentionObservationPresenter>(builder), AttentionObservationController {

    companion object {
    
    }

    private val context = builder.context
    private val bluetoothAdapter = builder.bluetoothAdapter
    private val device = builder.device
    private var headset: Headset? = null

    override fun startObservation() {
        this.headset = NeuroSkyHeadset(context, bluetoothAdapter.getRemoteDevice(device.address))
        this.headset?.registerAttentionListener(object : AttentionListener {

            override fun onAttentionChanged(data: AttentionData) {
                getPresenter().onAttentionChanged(data)
            }
        })
        this.headset?.connect()
    }

    override fun stopObservation() {
        this.headset?.disconnect()
        this.headset = null
    }
    
    class Builder(
        interactor: Interactor,
        presenter: AttentionObservationPresenter
    ) : ReactiveController.BaseBuilder<Builder, Interactor, AttentionObservationPresenter>(interactor, presenter) {
    
        override val self = this
        lateinit var context: Context
        lateinit var bluetoothAdapter: BluetoothAdapter
        lateinit var device: Device

        override fun build() = DefaultAttentionObservationController(this)
    }
}