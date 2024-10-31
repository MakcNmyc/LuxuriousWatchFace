package com.shishkin.luxuriouswatchface.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.shishkin.luxuriouswatchface.databinding.SettingsEditTextBinding
import com.shishkin.luxuriouswatchface.databinding.SettingsFooterBinding
import com.shishkin.luxuriouswatchface.databinding.SettingsTextWithImageBinding
import com.shishkin.luxuriouswatchface.databinding.SettingsTitleBinding
import com.shishkin.luxuriouswatchface.models.SettingsData
import com.shishkin.luxuriouswatchface.models.toSettingsEditText
import com.shishkin.luxuriouswatchface.models.toSettingsTextWithImage
import com.shishkin.luxuriouswatchface.models.toSettingsTitle
import com.shishkin.luxuriouswatchface.ui.settings.SettingsFragmentDirections
import com.shishkin.luxuriouswatchface.ui.viewmodels.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsAdapter @Inject constructor(itemCallback: ItemCallback<SettingsData>) :
    ModelAdapter<SettingsData>(itemCallback) {

    lateinit var viewModel: SettingsViewModel

    lateinit var scope: CoroutineScope

    companion object {
        const val TITLE = 0
        const val TEXT_WITH_IMAGE = 1
        const val EDIT_TEXT = 2
        const val FOOTER = 3

        const val TEXT_SAVING_DELAY = 500L
    }

    override val vhProducer: (parent: ViewGroup) -> ModelViewHolder<SettingsData, SettingsTextWithImageBinding> =
        { parent ->
            ModelViewHolder(
                parent,
                SettingsTextWithImageBinding::inflate,
                this::setUpTextWithImage
            )
        }

    override fun getItemViewType(position: Int): Int =
        getItem(position)!!.type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TITLE -> ModelViewHolder(
                parent,
                SettingsTitleBinding::inflate,
                this::setUpTitle
            )

            EDIT_TEXT -> ModelViewHolder(
                parent,
                SettingsEditTextBinding::inflate,
                this::setUpEditText
            )

            FOOTER -> BindingViewHolder(
                parent,
                SettingsFooterBinding::inflate,
            )

            else -> super.onCreateViewHolder(parent, viewType)
        }

    private fun setUpTextWithImage(dataModel: SettingsData, binding: SettingsTextWithImageBinding) {
        val model = dataModel.toSettingsTextWithImage()
        binding.model = model

        model.rightImage?.let {
            binding.title.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                it.create(binding.root.context),
                null
            )
        }

        model.clickListenerType?.let { type ->
            when (type) {
                ClickListenerTypes.COLOR_PICK_LISTENER -> createColorPickerListener(model.id)
                ClickListenerTypes.IMAGE_PICK_LISTENER -> createImagePickerListener(model.id)
            }.let { listener ->
                binding.root.setOnClickListener(listener)
            }
        }
    }

    private fun setUpTitle(model: SettingsData, binding: SettingsTitleBinding) {
        binding.model = model.toSettingsTitle()
    }

    @OptIn(FlowPreview::class)
    private fun setUpEditText(model: SettingsData, binding: SettingsEditTextBinding) {

        binding.model = model.toSettingsEditText()

        val id = model.id

        callbackFlow {
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(v: Editable?) {
                    if (binding.editor.hasFocus()) {
                        trySend(v.toString())
                    }
                }
            }.also {
                binding.editor.addTextChangedListener(it)
                awaitClose { binding.editor.removeTextChangedListener(it) }
            }
        }.debounce(TEXT_SAVING_DELAY).also {
            scope.launch {
                it.collect { text ->
                    viewModel.saveTextSetting(id, text)
                }
            }
        }
    }

    private fun createColorPickerListener(settingsId: String) = View.OnClickListener { v ->
        Navigation.findNavController(v).navigate(
            SettingsFragmentDirections.actionSettingsToColorsPicker(settingsId)
        )
    }

    private fun createImagePickerListener(settingsId: String) = View.OnClickListener { v ->
        Navigation.findNavController(v).navigate(
            SettingsFragmentDirections.actionSettingsToImagePicker(settingsId)
        )
    }

    enum class ClickListenerTypes {
        COLOR_PICK_LISTENER,
        IMAGE_PICK_LISTENER
    }

}