package com.shishkin.luxuriouswatchface.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.shishkin.luxuriouswatchface.adapters.ImagePickerAdapter
import com.shishkin.luxuriouswatchface.databinding.ImagesListBinding
import com.shishkin.luxuriouswatchface.ui.settings.ColorPickerFragment.Companion.ROW_COUNT
import com.shishkin.luxuriouswatchface.ui.util.createBinding
import com.shishkin.luxuriouswatchface.ui.util.setUpBaseList
import com.shishkin.luxuriouswatchface.ui.viewmodels.ImagePickerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ImagePickerFragment  : Fragment() {

    @Inject
    lateinit var adapter: ImagePickerAdapter
    private val viewModel: ImagePickerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        createBinding(inflater, container, ImagesListBinding::inflate)
            .also { binding ->
                viewModel.initImagesData()
                adapter.viewModel = viewModel
                setUpBaseList(
                    binding.imagesList,
                    viewModel.imagesData,
                    adapter,
                    GridLayoutManager(context, ROW_COUNT)
                )
            }.root

}