package com.shishkin.luxuriouswatchface.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shishkin.luxuriouswatchface.models.ListElementModel
import javax.inject.Inject

abstract class ModelAdapter<V : ListElementModel<*>>(itemCallback: ItemCallback<V>) :
    PagingDataAdapter<V, RecyclerView.ViewHolder>(itemCallback) {

    abstract val vhProducer: (parent: ViewGroup) -> ModelViewHolder<V, *>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return vhProducer(parent)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is ModelProducerVH<*, *> -> viewHolder.setModel()
            is ModelViewHolder<*, *> -> getItem(position)?.let {
                (viewHolder as ModelViewHolder<V, *>).setModel(it)
            }
        }
    }

    open class BindingViewHolder<T : ViewDataBinding>(
        parent: ViewGroup,
        val binding: T
    ) : RecyclerView.ViewHolder(binding.apply {
        lifecycleOwner = ViewTreeLifecycleOwner.get(parent)
    }.root) {
        constructor(
            parent: ViewGroup,
            inflateBinding: (LayoutInflater, ViewGroup, Boolean) -> T
        ) : this(parent, inflateBinding(LayoutInflater.from(parent.context), parent, false))
    }

    open class ModelViewHolder<in V : ListElementModel<*>, T : ViewDataBinding>(
        parent: ViewGroup,
        inflateBinding: (LayoutInflater, ViewGroup, Boolean) -> T,
        private inline val contentSetter: (model: V, binding: T) -> Unit
    ) : BindingViewHolder<T>(parent, inflateBinding) {

        fun setModel(model: V) {
            contentSetter(model, binding)
            binding.executePendingBindings()
        }
    }

    open class ModelProducerVH<in V : ListElementModel<*>, T : ViewDataBinding> (
        parent: ViewGroup,
        inflateBinding: (LayoutInflater, ViewGroup, Boolean) -> T,
        contentSetter: (model: V, binding: T) -> Unit,
        private inline val modelProducer: (() -> V)
    ) : ModelViewHolder<V, T>(parent, inflateBinding, contentSetter) {
        fun setModel() = setModel(modelProducer())
    }

}

class ItemCallback<T : ListElementModel<*>> @Inject constructor() : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}
