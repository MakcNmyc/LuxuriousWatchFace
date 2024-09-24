package com.shishkin.luxuriouswatchface.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.shishkin.luxuriouswatchface.adapters.ColorPickerAdapter
import com.shishkin.luxuriouswatchface.databinding.ColorsListBinding
import com.shishkin.luxuriouswatchface.ui.util.createBinding
import com.shishkin.luxuriouswatchface.ui.util.setUpBaseList
import com.shishkin.luxuriouswatchface.ui.viewmodels.ColorPickerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ColorPickerFragment : Fragment() {

    @Inject
    lateinit var adapter: ColorPickerAdapter
    private val viewModel: ColorPickerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return createBinding(inflater, container, ColorsListBinding::inflate)
            .also { binding ->

                viewModel.initBackgroundColorsData(requireContext())

                adapter.viewModel = viewModel
                setUpBaseList(
                    binding.settingsList,
                    viewModel.colorsData,
                    adapter,
                    GridLayoutManager(context, ROW_COUNT)
                )

            }.root
    }

    companion object{
        const val ROW_COUNT = 3
    }
}