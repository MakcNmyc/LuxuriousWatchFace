package com.shishkin.luxuriouswatchface.adapters

import android.view.ViewGroup
import androidx.navigation.Navigation
import com.shishkin.luxuriouswatchface.databinding.ImagePickerElementBinding
import com.shishkin.luxuriouswatchface.models.ImagePickElement
import com.shishkin.luxuriouswatchface.ui.util.toImageResourceCreator
import com.shishkin.luxuriouswatchface.ui.viewmodels.ImagePickerViewModel
import javax.inject.Inject

class ImagePickerAdapter @Inject constructor(itemCallback: ItemCallback<ImagePickElement>)  : ModelAdapter<ImagePickElement> (itemCallback)  {

    lateinit var viewModel: ImagePickerViewModel

    override val vhProducer: (parent: ViewGroup) -> ModelViewHolder<ImagePickElement, ImagePickerElementBinding> =
        { parent ->
            ModelViewHolder(
                parent,
                ImagePickerElementBinding::inflate,
                this::setUpData
            )
        }

    private fun setUpData(model: ImagePickElement, binding: ImagePickerElementBinding) {

        binding.image.setOnClickListener{
            if(viewModel.saveToSetting(model.image))
                Navigation.findNavController(binding.root).navigateUp()
        }

        binding.image.setImageDrawable(
            model.image.toImageResourceCreator()?.create(binding.root.context)
        )
    }

}