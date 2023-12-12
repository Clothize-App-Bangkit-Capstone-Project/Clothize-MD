package com.capstoneproject.clothizeapp.client.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.clothizeapp.data.local.preferences.client.ClientPrefViewModel
import com.capstoneproject.clothizeapp.data.local.preferences.client.UserPreferences
import com.capstoneproject.clothizeapp.data.local.preferences.client.ClientPreferencesFactory
import com.capstoneproject.clothizeapp.data.local.preferences.client.dataStore
import com.capstoneproject.clothizeapp.databinding.ActivitySplashBinding
import com.capstoneproject.clothizeapp.ui.client.MainClientActivity
import com.capstoneproject.clothizeapp.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private lateinit var clientPrefViewModel: ClientPrefViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        val pref = UserPreferences.getInstance(application.dataStore)
        clientPrefViewModel =
            ViewModelProvider(this, ClientPreferencesFactory(pref))[ClientPrefViewModel::class.java]

        val intentToMain = Intent(this, MainClientActivity::class.java)
        val intentToLogin = Intent(this, LoginActivity::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            clientPrefViewModel.getSessionUser().observe(this@SplashActivity) { session ->
                if (session != null) {
                    startActivity(intentToMain)
                    finish()
                } else {
                    startActivity(intentToLogin)
                    finish()
                }
            }
        }
    }
}