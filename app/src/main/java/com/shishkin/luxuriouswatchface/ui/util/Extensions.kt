package com.shishkin.luxuriouswatchface.ui.util

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
import com.shishkin.luxuriouswatchface.data.usersstyles.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
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

fun KClass<*>.allProperties() : Collection<KProperty1<*, *>> = this.members.filterIsInstance<KProperty1<*, *>>()

fun KClass<*>.findMember(memberName: String) = this.members.firstOrNull { it.name == memberName }

//@Suppress("UNCHECKED_CAST")
//fun <T: Any> T.findProperty(propertyName: String) = this.findMember(propertyName)?.let {
//    it as KProperty1<T, *>
//}

@Suppress("UNCHECKED_CAST")
fun <T, V> KCallable<V>.setProperty(receiver: T, value: V) =
        (this as KMutableProperty1<T, V>).set(receiver, value)

@Suppress("UNCHECKED_CAST")
fun <T: Any, V> T.setProperty(propertyName: String, value: V) =
    this::class.findMember(propertyName)?.let {
        (it as KMutableProperty1<T, V>).set(this, value)
        true
    } ?: false

fun <T> isEquals(collection: Collection<T>, collection2: Collection<T>) =
    collection.size == collection2.size && collection.toSet() == collection2.toSet()

fun Int.fromDimension(context: Context) =
    context.resources.getDimension(this)

fun Boolean.toVisibility() =
    if (this) View.VISIBLE else View.GONE

fun <V : Any> List<V>.toPagingData() = PagingData.from(this)

@Suppress("UNCHECKED_CAST")
fun <V : Any> KProperty1<*, V>.typeFromClassifier() : KClass<V> =
    returnType.classifier as KClass<V>

val CUSTOM_DATA_PROPERTY = UserSettings::customData
const val CUSTOM_DATA_ID = "CustomValue"
fun KProperty1<*, *>.toId() =
    if(this == CUSTOM_DATA_PROPERTY) CUSTOM_DATA_ID else name

inline fun <reified T : Enum<T>> Int.toEnum(): T? =
    if(this < 0) null
        else enumValues<T>().firstOrNull { it.ordinal == this }

inline fun <reified T : Enum<T>> T.toInt(): Int =
    this.ordinal

