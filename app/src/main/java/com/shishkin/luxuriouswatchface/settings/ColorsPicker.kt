package com.shishkin.luxuriouswatchface.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.shishkin.luxuriouswatchface.adapters.ColorsPickerAdapter
import com.shishkin.luxuriouswatchface.databinding.ColorsListBinding
import com.shishkin.luxuriouswatchface.util.createBinding
import com.shishkin.luxuriouswatchface.util.setUpBaseList
import com.shishkin.luxuriouswatchface.viewmodels.ColorsPickerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ColorsPicker : Fragment() {

    @Inject
    lateinit var adapter: ColorsPickerAdapter
    private val viewModel: ColorsPickerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return createBinding(inflater, container, ColorsListBinding::inflate)
            .also { binding ->
                Log.e("qwe", "createBinding")

                viewModel.initColorsData(requireContext())

                adapter.settingsEditor = (requireActivity() as MainActivity).settingsEditor

                setUpBaseList(
                    binding.settingsList,
                    viewModel.colorsData,
                    adapter,
                    GridLayoutManager(context, ROW_COUNT)
                )

                Log.e("qwe", "lifecycleScope collect settingsId")
                viewLifecycleOwner.lifecycleScope.launch{
                    viewModel.settingsId.collectLatest{
                        Log.e("qwe", "lifecycleScope collect settingsId is $it")
                        adapter.settingsId = it
                    }
                }

            }.root
    }

    companion object{
        const val ROW_COUNT = 3
    }
}