package com.shishkin.luxuriouswatchface.adapters

import android.util.Log
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.shishkin.luxuriouswatchface.databinding.ColorsElementBinding
import com.shishkin.luxuriouswatchface.models.ColorsElement
import com.shishkin.luxuriouswatchface.usersstyles.SettingsEditor
import com.shishkin.luxuriouswatchface.usersstyles.SettingsSchemaImp
import com.shishkin.luxuriouswatchface.util.ColorImageCreator
import com.shishkin.luxuriouswatchface.viewmodels.SettingsViewModel
import javax.inject.Inject

class ColorsPickerAdapter @Inject constructor(itemCallback: ItemCallback<ColorsElement>)  : ModelAdapter<ColorsElement> (itemCallback)  {

//    private lateinit var owner: Fragment
    lateinit var settingsEditor: SettingsEditor

    override val vhProducer: (parent: ViewGroup) -> ModelViewHolder<ColorsElement, ColorsElementBinding> =
        { parent ->
            ModelViewHolder(
                parent,
                ColorsElementBinding::inflate,
                this::setUpData
            )
        }

    var settingsId = SettingsViewModel.SETTINGS_ID_NOT_SET

    private fun setUpData(model: ColorsElement, binding: ColorsElementBinding) {

        binding.color.setOnClickListener{
            Log.e("settingsIdFlow", "setUpData ClickListener settingsId = $settingsId")
            if(settingsId == SettingsViewModel.SETTINGS_ID_NOT_SET) return@setOnClickListener
            Log.e("settingsIdFlow", "setUpData ClickListener color is ${model.color}")

//            owner.setFragmentResult(Settings.SETTINGS_CHANGE, bundleOf(Settings.SETTINGS_CHANGE to ))

            settingsEditor.set(SettingsSchemaImp.BACKGROUND_COLOR, model.color)

            Navigation.findNavController(binding.root).navigateUp()
//            Navigation.findNavController(binding.root).navigate(
//                ColorsPickerDirections.actionColorsPickerToSettings(
//                    SettingsChanges(settingsId, model.color)
//                )
//            )
        }

        binding.color.setImageDrawable(
            ColorImageCreator(model.color).create(binding.root.context)
        )
    }

}