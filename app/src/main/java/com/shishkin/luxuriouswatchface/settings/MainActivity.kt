/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.shishkin.luxuriouswatchface.settings

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.shishkin.luxuriouswatchface.databinding.ActivityMainBinding
import com.shishkin.luxuriouswatchface.usersstyles.SettingsEditor
import com.shishkin.luxuriouswatchface.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    lateinit var binding: ActivityMainBinding

    val viewModel: MainActivityViewModel by viewModels()
    var settingsEditor = SettingsEditor(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityMainBinding.inflate(layoutInflater)
            .apply {
                setContentView(root)
//                lifecycleScope.launch {
//                    settingsEditor.state.collect {
//                        root.isEnabled = it == SettingsEditor.State.Ready
//                    }
//                }

//                Navigation.findNavController(this@MainActivity, R.id.main_navigation_container)
//                setSupportActionBar(toolbar)
//                val host = supportFragmentManager.findFragmentById(R.id.main_navigation_container) as NavHostFragment
//                setupActionBarWithNavController(host.navController,)
                Log.e("qwe", "setupActionBarWithNavController")
            }

//        val qwe = Navigation.findNavController(this, R.id.main_navigation_container)
//        setupActionBarWithNavController(this, qwe)
//        val host = supportFragmentManager.findFragmentById(R.id.main_navigation_container) as NavHostFragment
//        Navigation.findNavController(binding.contentMainNavigationContainer)

    }

//    override fun onNavigateUp(): Boolean {
//        Log.e("qwe", "onNavigateUp")
//        return Navigation.findNavController(binding.root).navigateUp()
//    }

}