package com.shishkin.luxuriouswatchface.util

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.wear.widget.WearableRecyclerView
import com.shishkin.luxuriouswatchface.usersstyles.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

inline fun <T : ViewDataBinding> Fragment.createBinding(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> T
): T = inflateBinding(inflater, parent, false).apply {
    lifecycleOwner = this@createBinding
}

fun <T : Any> Fragment.setUpBaseList(
    recyclerView: WearableRecyclerView,
    pagedData: Flow<PagingData<T>?>,
    adapter: PagingDataAdapter<T, RecyclerView.ViewHolder>,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.context)
) {
    this.setUpPagedList(pagedData, adapter)
    recyclerView.apply {
        setHasFixedSize(true)
        this.layoutManager = layoutManager
        this.adapter = adapter
//        isEdgeItemsCenteringEnabled = true
        isCircularScrollingGestureEnabled = false
    }
}

fun <T : Any> Fragment.setUpPagedList(
    pagedData: Flow<PagingData<T>?>,
    adapter: PagingDataAdapter<T, RecyclerView.ViewHolder>
){
    this.viewLifecycleOwner.lifecycleScope.launch {
        pagedData.collectLatest{ data ->
            Log.e("colorsPick", "collectLatest paged data ${data}")
            data?.let {
                adapter.submitData(data)
            }
        }
    }
}

fun Int.fromDimension(context: Context) =
    context.resources.getDimension(this)

fun Boolean.toVisibility() =
    if (this) View.VISIBLE else View.GONE

fun <V : Any> List<V>.toPagingData() = PagingData.from(this)

@Suppress("UNCHECKED_CAST")
fun <V : Any> KProperty1<*, V>.typeFromClassifier() : KClass<V> =
    returnType.classifier as KClass<V>

fun KProperty1<UserSettings, *>.toId() = name

inline fun <reified T : Enum<T>> Int.toEnum(): T? =
    if(this < 0) null
        else enumValues<T>().firstOrNull { it.ordinal == this }

inline fun <reified T : Enum<T>> T.toInt(): Int =
    this.ordinal

