package com.shishkin.luxuriouswatchface.adapters

import android.view.ViewGroup
import androidx.navigation.Navigation
import com.shishkin.luxuriouswatchface.databinding.ColorsElementBinding
import com.shishkin.luxuriouswatchface.models.ColorPickElement
import com.shishkin.luxuriouswatchface.ui.util.ColorImageCreator
import com.shishkin.luxuriouswatchface.ui.viewmodels.ColorPickerViewModel
import com.shishkin.luxuriouswatchface.ui.viewmodels.SettingsViewModel
import javax.inject.Inject

class ColorPickerAdapter @Inject constructor(itemCallback: ItemCallback<ColorPickElement>)  : ModelAdapter<ColorPickElement> (itemCallback)  {

    lateinit var viewModel: ColorPickerViewModel

    var settingsId = SettingsViewModel.SETTINGS_ID_NOT_SET

    override val vhProducer: (parent: ViewGroup) -> ModelViewHolder<ColorPickElement, ColorsElementBinding> =
        { parent ->
            ModelViewHolder(
                parent,
                ColorsElementBinding::inflate,
                this::setUpData
            )
        }

    private fun setUpData(model: ColorPickElement, binding: ColorsElementBinding) {

        binding.color.setOnClickListener{
            if(settingsId == SettingsViewModel.SETTINGS_ID_NOT_SET) return@setOnClickListener
            viewModel.saveToSetting(settingsId, model.color)
            Navigation.findNavController(binding.root).navigateUp()
        }

        binding.color.setImageDrawable(
            ColorImageCreator(model.color).create(binding.root.context)
        )
    }

}