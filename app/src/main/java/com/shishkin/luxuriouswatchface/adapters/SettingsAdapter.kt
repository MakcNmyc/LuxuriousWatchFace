package com.shishkin.luxuriouswatchface.adapters

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shishkin.luxuriouswatchface.databinding.SettingsEditTextBinding
import com.shishkin.luxuriouswatchface.databinding.SettingsElementBinding
import com.shishkin.luxuriouswatchface.databinding.SettingsTitleBinding
import com.shishkin.luxuriouswatchface.models.SettingsEditText
import com.shishkin.luxuriouswatchface.models.SettingsElement
import com.shishkin.luxuriouswatchface.models.SettingsTextWithImage
import com.shishkin.luxuriouswatchface.models.SettingsTitle
import javax.inject.Inject

class SettingsAdapter @Inject constructor(itemCallback: ItemCallback<SettingsElement>)  : ModelAdapter<SettingsElement> (itemCallback) {

    lateinit var title: SettingsTitle

    private companion object {
        const val TITLE = 0
        const val TEXT_WITH_IMAGE = 1
        const val EDIT_TEXT = 2

        const val ITEM_PADDING = 1
    }

    override val vhProducer: (parent: ViewGroup) -> ModelViewHolder<SettingsElement, SettingsElementBinding> =
        { parent ->
            ModelViewHolder(
                parent,
                SettingsElementBinding::inflate,
                this::setUpData
            )
        }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) return TITLE
        return when (getItem(position - ITEM_PADDING)) {
            is SettingsEditText -> EDIT_TEXT
            else -> TEXT_WITH_IMAGE
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        Log.e("settingsIdFlow", "adapterPosition = ${holder.adapterPosition}")
        super.onViewRecycled(holder)
    }

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

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        Log.e("settingsIdFlow", "onBindViewHolder ${viewHolder.adapterPosition}")
        super.onBindViewHolder(viewHolder, position - ITEM_PADDING)
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + ITEM_PADDING
    }

    private fun setUpData(model: SettingsElement, binding: SettingsElementBinding) {
        Log.e("qwe", "setUpData title = ${model.title}")
        binding.model = model
        if (model is SettingsTextWithImage){
            if (model.rightImage != null)
                binding.title.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    model.rightImage.create(binding.root.context),
                    null
                )
            if(model.onClickListener != null)
                binding.root.setOnClickListener(model.onClickListener)
        }
    }

    private fun setUpTitle(model: SettingsTitle, binding: SettingsTitleBinding) {
        binding.model = model
    }

    private fun setUpEditText(model: SettingsEditText, binding: SettingsEditTextBinding) {
        binding.model = model
    }

}