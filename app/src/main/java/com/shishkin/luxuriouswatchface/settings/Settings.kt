package com.shishkin.luxuriouswatchface.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.shishkin.luxuriouswatchface.adapters.SettingsAdapter
import com.shishkin.luxuriouswatchface.databinding.SettingsListBinding
import com.shishkin.luxuriouswatchface.util.createBinding
import com.shishkin.luxuriouswatchface.util.setUpBaseList
import com.shishkin.luxuriouswatchface.viewmodels.MainActivityViewModel
import com.shishkin.luxuriouswatchface.viewmodels.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class Settings : Fragment() {

    private lateinit var binding: SettingsListBinding
    private val settingsArgs: SettingsArgs by navArgs()
    @Inject lateinit var adapter: SettingsAdapter
    private val viewModel: SettingsViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.e("settingsIdFlow", "Settings onCreateView")

//        setFragmentResultListener("requestKey") { requestKey, bundle ->
//            // We use a String here, but any type that can be put in a Bundle is supported.
//            val result = bundle.getString("bundleKey")
//            // Do something with the result.
//            Log.e("settingsIdFlow", "Settings FragmentResultListener result: $result")
//        }



        viewModel.changeSettings(settingsArgs.settingsChanges)

        return createBinding(inflater, container, SettingsListBinding::inflate)
            .also { binding ->
                Log.e("settingsIdFlow", "createBinding")
                this.binding = binding
                Log.e("settingsIdFlow", "viewModel.recyclerViewState = ${activityViewModel.recyclerViewState}")
                activityViewModel.recyclerViewState?.let {
                    Log.e("settingsIdFlow", "onRestoreInstanceState")
                    binding.settingsList.layoutManager?.onRestoreInstanceState(it)
                }
                adapter.title = viewModel.title
                setUpBaseList(
                    binding.settingsList,
                    { viewModel.pagedList },
                    adapter
                )
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e("settingsIdFlow", "onViewCreated viewModel.recyclerViewState = ${activityViewModel.recyclerViewState}")
        activityViewModel.recyclerViewState?.let {
            Log.e("settingsIdFlow", "onRestoreInstanceState")
            binding.settingsList.layoutManager?.onRestoreInstanceState(it)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        Log.e("settingsIdFlow", "onResume")
        Log.e("settingsIdFlow", "viewModel.recyclerViewState = ${activityViewModel.recyclerViewState}")
        activityViewModel.recyclerViewState?.let {
            Log.e("settingsIdFlow", "onRestoreInstanceState")
            binding.settingsList.layoutManager?.onRestoreInstanceState(it)
        }
        super.onResume()
    }

    override fun onDestroy() {
        Log.e("settingsIdFlow", "onDestroy")
        super.onDestroy()
    }

    override fun onPause() {
        Log.e("settingsIdFlow", "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.e("settingsIdFlow", "onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.e("settingsIdFlow", "onDestroyView")
        activityViewModel.recyclerViewState = binding.settingsList.layoutManager?.onSaveInstanceState()
        Log.e("settingsIdFlow", "onDestroyView computeVerticalScrollOffset = ${binding.settingsList.computeVerticalScrollOffset()}")
        super.onDestroyView()
    }

    override fun onDetach() {
        Log.e("settingsIdFlow", "onDetach")
        super.onDetach()
    }
}