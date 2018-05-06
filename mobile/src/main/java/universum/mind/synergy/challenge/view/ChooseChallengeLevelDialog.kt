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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import universum.mind.synergy.R
import universum.studios.android.support.dialog.SimpleDialog

/**
 * @author Martin Albedinsky
 */
class ChooseChallengeLevelDialog : SimpleDialog() {

    internal lateinit var valueView: TextView
    internal lateinit var seekBar: SeekBar
    internal var selectedLevel = 50

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_layout_challenge_level_slider, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.valueView = view.findViewById(R.id.level_value)
        this.valueView.text = selectedLevel.toString()
        this.seekBar = view.findViewById(R.id.level_slider)
        this.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (selectedLevel != progress) {
                    selectedLevel = progress
                    valueView.text = selectedLevel.toString()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    fun getSelectedLevel(): Int = selectedLevel
}