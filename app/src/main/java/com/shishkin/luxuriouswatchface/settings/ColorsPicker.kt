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
import com.shishkin.luxuriouswatchface.databinding.SettingsListBinding
import com.shishkin.luxuriouswatchface.util.createBinding
import com.shishkin.luxuriouswatchface.util.setUpBaseList
import com.shishkin.luxuriouswatchface.viewmodels.ColorsPickerViewModel
import dagger.hilt.android.AndroidEntryPoint
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

        return createBinding(inflater, container, SettingsListBinding::inflate)
            .also { binding ->
                Log.e("qwe", "ColorsPicker createBinding")

                adapter.owner = this

                setUpBaseList(
                    binding.settingsList,
                    { viewModel.pagedList },
                    adapter,
                    GridLayoutManager(context, ROW_COUNT)
                )
                Log.e("qwe", "lifecycleScope collect settingsId")
                lifecycleScope.launch{
                    viewModel.settingsId.collect{
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