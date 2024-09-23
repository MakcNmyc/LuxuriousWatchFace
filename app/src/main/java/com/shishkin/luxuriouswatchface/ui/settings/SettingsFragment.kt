package com.shishkin.luxuriouswatchface.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.shishkin.luxuriouswatchface.adapters.SettingsAdapter
import com.shishkin.luxuriouswatchface.data.usersstyles.SettingsEditor
import com.shishkin.luxuriouswatchface.databinding.SettingsListBinding
import com.shishkin.luxuriouswatchface.ui.util.createBinding
import com.shishkin.luxuriouswatchface.ui.util.setUpBaseList
import com.shishkin.luxuriouswatchface.ui.util.toVisibility
import com.shishkin.luxuriouswatchface.ui.viewmodels.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    @Inject lateinit var adapter: SettingsAdapter
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.initSettingsSession(requireActivity())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return createBinding(inflater, container, SettingsListBinding::inflate)
            .also { binding ->

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.subscribeToSettingsState { state ->
                        (SettingsEditor.isReady(state)).let { isReady ->
                            binding.settingsList.visibility = isReady.toVisibility()
                            binding.progressBar.visibility = (!isReady).toVisibility()
                        }
                    }
                }

                adapter.viewModel = viewModel
                adapter.scope = viewLifecycleOwner.lifecycleScope
                setUpBaseList(
                    binding.settingsList,
                    viewModel.settingsData,
                    adapter
                )

            }.root
    }
}