package com.shishkin.luxuriouswatchface.viewmodels

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel(){

    var recyclerViewState: Parcelable? = null

}