package com.shishkin.luxuriouswatchface.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.shishkin.luxuriouswatchface.adapters.SettingsAdapter
import com.shishkin.luxuriouswatchface.databinding.SettingsListBinding
import com.shishkin.luxuriouswatchface.usersstyles.SettingsEditor
import com.shishkin.luxuriouswatchface.util.createBinding
import com.shishkin.luxuriouswatchface.util.setUpBaseList
import com.shishkin.luxuriouswatchface.util.toVisibility
import com.shishkin.luxuriouswatchface.viewmodels.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class Settings : Fragment() {

//    private var binding: SettingsListBinding? = null
//    private val settingsArgs: SettingsArgs by navArgs()
    @Inject lateinit var adapter: SettingsAdapter
    private val viewModel: SettingsViewModel by viewModels()
//    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var settingsEditor: SettingsEditor

    override fun onCreate(savedInstanceState: Bundle?) {
//
        settingsEditor = (requireActivity() as MainActivity).settingsEditor
//
//        viewModel.createPagedData(requireContext(), settingsEditor)
//
//        super.onCreate(savedInstanceState)
//    }

    //    override fun onCreate(savedInstanceState: Bundle?) {
//
//        settingsEditor = (requireActivity() as MainActivity).settingsEditor

//        lifecycleScope.launch {
//            settingsEditor.state.collect { state ->
//                Log.e("settingsIdFlow", "settingsEditor state is ${state}")
//                (state == SettingsEditor.State.Ready).let {
//                    Log.e("settingsIdFlow", "settingsEditor visibility is ${it.toVisibility()}")
//                    binding?.settingsList?.visibility = it.toVisibility()
//                    binding?.progressBar?.visibility = (!it).toVisibility()
//                }
//            }
//        }
//
//        lifecycleScope.launch {
//            settingsEditor.settingsHolder.collect{
//
//                viewModel.refreshPagedList(requireContext(), settingsEditor)
//
//                adapter.title = viewModel.title
//                binding?.let { b ->
//                    setUpBaseList(
//                        b.settingsList,
//                        { viewModel.pagedList },
//                        adapter
//                    )
//                }
//
////                binding?.settingsList?.adapter?.notifyDataSetChanged()
//                Log.e("settingsHolder", "createSettingsList")
//            }
//        }

//        setFragmentResultListener(SETTINGS_CHANGE){ _, result ->
//
//        }

////        setFragmentResultListener(SETTINGS_CHANGE) { _, result ->
//////            settingsEditor.set(
//////                SettingsSchemaImp.BACKGROUND_COLOR,
//////                bundle.getInt(SettingsSchemaImp.BACKGROUND_COLOR)
//////            )
////
////            viewModel.changeSettings(
////                result.getParcelable(SETTINGS_CHANGE, SettingsChanges::class.java),
////                settingsEditor)
////        }
//
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.e("settingsIdFlow", "Settings onCreateView")

        return createBinding(inflater, container, SettingsListBinding::inflate)
            .also { binding ->
                Log.e("settingsIdFlow", "createBinding")
//                this.binding = binding

                viewLifecycleOwner.lifecycleScope.launch {
                    settingsEditor.state.collectLatest { state ->
                        Log.e("settingsIdFlow", "settingsEditor state is ${state}")
                        (state == SettingsEditor.State.Ready).let { isReady ->
                            Log.e("settingsIdFlow", "settingsEditor visibility is ${isReady.toVisibility()}")
                            binding.settingsList.visibility = isReady.toVisibility()
                            binding.progressBar.visibility = (!isReady).toVisibility()
                        }
                    }
                }

//                viewLifecycleOwner.lifecycleScope.launch {
//                    settingsEditor.settingsHolder.collect{

//                        Log.e("settingsIdFlow", "settingsHolder collect")

//                        viewModel.refreshPagedList(requireContext(), settingsEditor)

//                        adapter.title = viewModel.title
//                        setUpBaseList(
//                            binding.settingsList,
//                            { viewModel.pagedList },
//                            adapter
//                        )

//                binding?.settingsList?.adapter?.notifyDataSetChanged()
//                        Log.e("settingsHolder", "createSettingsList")
//                    }
//                }


                viewModel.initSettingsData(requireContext(), settingsEditor)

                adapter.settingsEditor = settingsEditor
                adapter.scope = viewLifecycleOwner.lifecycleScope
                setUpBaseList(
                    binding.settingsList,
                    viewModel.settingsData,
                    adapter
                )
//                Log.e("settingsIdFlow", "viewModel.recyclerViewState = ${activityViewModel.recyclerViewState}")
//                activityViewModel.recyclerViewState?.let {
//                    Log.e("settingsIdFlow", "onRestoreInstanceState")
//                    binding.settingsList.layoutManager?.onRestoreInstanceState(it)
//                }
            }.root

//        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        Log.e("settingsIdFlow", "onViewCreated viewModel.recyclerViewState = ${activityViewModel.recyclerViewState}")
//        activityViewModel.recyclerViewState?.let {
//            Log.e("settingsIdFlow", "onRestoreInstanceState")
//            binding?.settingsList.layoutManager?.onRestoreInstanceState(it)
//        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        Log.e("settingsIdFlow", "onResume")
//        Log.e("settingsIdFlow", "viewModel.recyclerViewState = ${activityViewModel.recyclerViewState}")
//        activityViewModel.recyclerViewState?.let {
//            Log.e("settingsIdFlow", "onRestoreInstanceState")
//            binding.settingsList.layoutManager?.onRestoreInstanceState(it)
//        }
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
//        activityViewModel.recyclerViewState = binding.settingsList.layoutManager?.onSaveInstanceState()
//        Log.e("settingsIdFlow", "onDestroyView computeVerticalScrollOffset = ${binding.settingsList.computeVerticalScrollOffset()}")
//        binding = null
        super.onDestroyView()
    }

    override fun onDetach() {
        Log.e("settingsIdFlow", "onDetach")
        super.onDetach()
    }

    companion object{
        const val SETTINGS_CHANGE = "settings_change"
    }
}