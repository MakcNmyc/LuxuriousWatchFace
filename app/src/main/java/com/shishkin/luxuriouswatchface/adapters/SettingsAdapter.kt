package com.shishkin.luxuriouswatchface.adapters

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.shishkin.luxuriouswatchface.databinding.SettingsEditTextBinding
import com.shishkin.luxuriouswatchface.databinding.SettingsTextWithImageBinding
import com.shishkin.luxuriouswatchface.databinding.SettingsTitleBinding
import com.shishkin.luxuriouswatchface.models.SettingsData
import com.shishkin.luxuriouswatchface.models.toSettingsEditText
import com.shishkin.luxuriouswatchface.models.toSettingsTextWithImage
import com.shishkin.luxuriouswatchface.models.toSettingsTitle
import com.shishkin.luxuriouswatchface.ui.settings.SettingsFragmentDirections
import com.shishkin.luxuriouswatchface.usersstyles.SettingsEditor
import com.shishkin.luxuriouswatchface.util.StabilizingWorker
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class SettingsAdapter @Inject constructor(itemCallback: ItemCallback<SettingsData>)  : ModelAdapter<SettingsData> (itemCallback) {

//    lateinit var title: SettingsTitle

    lateinit var settingsEditor: SettingsEditor
    lateinit var scope: CoroutineScope

    val textWorkers = HashMap<String, StabilizingWorker<String>>()

    companion object {
        const val TITLE = 0
        const val TEXT_WITH_IMAGE = 1
        const val EDIT_TEXT = 2

//        const val ITEM_PADDING = 1
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
//    {
//        val res = if (position == 0) TITLE else getItem(position - ITEM_PADDING)!!.type
//        Log.e("colorsPick", "getItemViewType = ${res}")
//        return res
//    }

//    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
//        Log.e("settingsIdFlow", "adapterPosition = ${holder.adapterPosition}")
//        super.onViewRecycled(holder)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when(viewType){
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
            else -> super.onCreateViewHolder(parent, viewType)
        }

//    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
////        Log.e("colorsPick", "onBindViewHolder position $position")
//        super.onBindViewHolder(viewHolder, position - ITEM_PADDING)
//    }

//    override fun getItemCount(): Int {
//        Log.e("colorsPick", "getItemCount count is ${super.getItemCount() + ITEM_PADDING}")
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

    private fun setUpTitle(model: SettingsData, binding: SettingsTitleBinding) {
        binding.model = model.toSettingsTitle()
    }

    private fun setUpEditText(model: SettingsData, binding: SettingsEditTextBinding) {

        binding.model = model.toSettingsEditText()

        val id = model.id

        if(!textWorkers.containsKey(model.id)) textWorkers[id] = StabilizingWorker(scope,
            { v->
                Log.e("customData", "StabilizingWorker text - $v")
                 settingsEditor.set(id, v)
            }
        )

        binding.editor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                textWorkers[id]?.setData(p0.toString())
            }
        })

//        binding.editor.setOnEditorActionListener { textView, i, _ ->
//            Log.e("customData", "setOnEditorActionListener i = $i")
//            if(i == EditorInfo.IME_ACTION_DONE) {
//                Log.e("customData", "done button textView.text = ${textView.text}")
//                settingsEditor.set(id, textView.text.toString())
//            }
//
//            return@setOnEditorActionListener false
//        }

//        binding.editor.setOnFocusChangeListener{ view, hasFocus ->
//            Log.e("customData", "OnFocusChangeListener model.id = ${model.id} hasFocus = ${hasFocus}")
//        }
    }

    private fun createColorPickerListener(settingsId: String) = View.OnClickListener { v ->
        Navigation.findNavController(v).navigate(
            SettingsFragmentDirections.actionSettingsToColorsPicker(settingsId)
        )
    }

    enum class ClickListenerTypes{
        ColorPickListener
    }

}