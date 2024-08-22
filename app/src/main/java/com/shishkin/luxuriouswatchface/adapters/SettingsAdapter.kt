package com.shishkin.luxuriouswatchface.adapters

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.shishkin.luxuriouswatchface.databinding.SettingsEditTextBinding
import com.shishkin.luxuriouswatchface.databinding.SettingsTextWithImageBinding
import com.shishkin.luxuriouswatchface.databinding.SettingsTitleBinding
import com.shishkin.luxuriouswatchface.models.SettingsData
import com.shishkin.luxuriouswatchface.models.SettingsTitle
import com.shishkin.luxuriouswatchface.models.toSettingsEditText
import com.shishkin.luxuriouswatchface.models.toSettingsTextWithImage
import com.shishkin.luxuriouswatchface.settings.SettingsDirections
import javax.inject.Inject

class SettingsAdapter @Inject constructor(itemCallback: ItemCallback<SettingsData>)  : ModelAdapter<SettingsData> (itemCallback) {

    lateinit var title: SettingsTitle

    companion object {
        const val TITLE = 0
        const val TEXT_WITH_IMAGE = 1
        const val EDIT_TEXT = 2

        const val ITEM_PADDING = 1
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
//        if (position == 0) TITLE else getItem(position - ITEM_PADDING)!!.type
    getItem(position)!!.type

//    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
//        Log.e("settingsIdFlow", "adapterPosition = ${holder.adapterPosition}")
//        super.onViewRecycled(holder)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when(viewType){
            TITLE -> ModelProducerVH(
                parent,
                SettingsTitleBinding::inflate,
                this::setUpTitle
            ) { title }
            EDIT_TEXT -> ModelViewHolder(
                parent,
                SettingsEditTextBinding::inflate,
                this::setUpEditText
            )
            else -> super.onCreateViewHolder(parent, viewType)
        }

//    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
////        Log.e("ColorsPicker", "onBindViewHolder position $position")
//        super.onBindViewHolder(viewHolder, position - ITEM_PADDING)
//    }

//    override fun getItemCount(): Int {
//        return super.getItemCount() + ITEM_PADDING
//    }

    private fun setUpTextWithImage(dataModel: SettingsData, binding: SettingsTextWithImageBinding) {
        Log.e("qwe", "setUpData title = ${dataModel.title}")

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
                ClickListenerTypes.ColorPickListener -> createColorPickerListener(model.id)
            }.let { listener ->
                binding.root.setOnClickListener(listener)
            }
        }
    }

    private fun setUpTitle(model: SettingsTitle, binding: SettingsTitleBinding) {
        binding.model = model
    }

    private fun setUpEditText(model: SettingsData, binding: SettingsEditTextBinding) {
        binding.model = model.toSettingsEditText()
    }

    private fun createColorPickerListener(settingsId: String) = View.OnClickListener { v ->
        Navigation.findNavController(v).navigate(
            SettingsDirections.actionSettingsToColorsPicker(settingsId)
        )
    }

    enum class ClickListenerTypes{
        ColorPickListener
    }

}