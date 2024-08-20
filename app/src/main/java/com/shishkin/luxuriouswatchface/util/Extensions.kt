package com.shishkin.luxuriouswatchface.util

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.paging.PositionalDataSource
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.wear.widget.WearableRecyclerView
import com.shishkin.luxuriouswatchface.AppConfiguration.Companion.PAGING_SIZE

inline fun <T : ViewDataBinding> Fragment.createBinding(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> T
): T = inflateBinding(inflater, parent, false).apply {
    lifecycleOwner = this@createBinding
}

inline fun <T> Fragment.setUpBaseList(
    recyclerView: WearableRecyclerView,
    crossinline pagedListProducer: ()-> LiveData<PagedList<T>>,
    pagedAdapter: PagedListAdapter<T, RecyclerView.ViewHolder>,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.context)
) {
    this.setUpPagedList(pagedListProducer, pagedAdapter)
    recyclerView.apply {
        setHasFixedSize(true)
        this.layoutManager = layoutManager
        adapter = pagedAdapter
        isEdgeItemsCenteringEnabled = true
        isCircularScrollingGestureEnabled = false
    }
}

inline fun <T> Fragment.setUpPagedList(
    pagedListProducer: ()-> LiveData<PagedList<T>>,
    pagedAdapter: PagedListAdapter<T, RecyclerView.ViewHolder>
){
    pagedListProducer().observe(this) { newPagedList -> pagedAdapter.submitList(newPagedList) }
}

// TODO: Migrate to pager3
fun <V> Array<V>.asPagedList(onSourceCreated: ((ListDataSource<V>) -> Unit)? = null) = LivePagedListBuilder(
    ListDataSource.Factory(this, onSourceCreated),
    PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(PAGING_SIZE)
        .build()
).build().also {
    Log.e("qwe", "asPagedList")
}

fun Int.fromDimension(context: Context) =
    context.resources.getDimension(this)

fun Boolean.toVisibility() =
    if (this) View.VISIBLE else View.GONE

class ListDataSource<V>(private val itemList: Array<V>) : PositionalDataSource<V>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<V>) {
        Log.e("qwe", "loadInitial requestedStartPosition = ${params.requestedStartPosition} and requestedLoadSize = ${params.requestedLoadSize}, itemListSize = ${itemList.size}")

        callback.onResult(
            itemList
                .drop(params.requestedStartPosition)
                .take(params.requestedLoadSize)
                .toList(),
            0
        )
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<V>) {

        Log.e("qwe", "loadInitial startPosition = ${params.startPosition} and loadSize = ${params.loadSize}")

        Log.e("qwe", "loadRange")
        callback.onResult(
            itemList
                .drop(params.startPosition)
                .take(params.loadSize)
                .toList()
        )
    }

    class Factory<V>(private val itemList: Array<V>, private val onSourceCreated: ((ListDataSource<V>) -> Unit)?): DataSource.Factory<Int, V>() {
        override fun create(): DataSource<Int, V>  = ListDataSource(itemList).also {
            onSourceCreated?.let { it1 -> it1(it) }
        }
    }

}
