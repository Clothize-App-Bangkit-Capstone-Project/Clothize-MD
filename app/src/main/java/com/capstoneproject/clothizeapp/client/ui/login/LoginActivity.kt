package com.capstoneproject.clothizeapp.client.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.clothizeapp.R
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.ClientPrefViewModel
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.ClientPreferencesFactory
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.ClientSession
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.UserPreferences
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.dataStore
import com.capstoneproject.clothizeapp.client.ui.client.MainClientActivity
import com.capstoneproject.clothizeapp.client.ui.register.RegisterActivity
import com.capstoneproject.clothizeapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var pref: UserPreferences
    private lateinit var clientPrefViewModel: ClientPrefViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()


    }

    private fun init() {
        auth = Firebase.auth
        pref = UserPreferences.getInstance(application.dataStore)
        clientPrefViewModel =
            ViewModelProvider(this, ClientPreferencesFactory(pref))[ClientPrefViewModel::class.java]

        binding.tvIntentRegis.setOnClickListener {
            val intentToRegister = Intent(this, RegisterActivity::class.java)
            finish()
            startActivity(intentToRegister)
        }

        binding.btnLogin.setOnClickListener {
            if (checkFillOrNot()) {
                authenticate()
            }
        }

        dialog = loadingDialog()

    }

    private fun authenticate() {
        val emailUsername = binding.emailUsernameEdt.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        Log.d("TAG", "authenticate: ${emailUsername} $password")

        dialog.show()
        auth.signInWithEmailAndPassword(emailUsername, password)
            .addOnCompleteListener(this) { task ->
                dialog.dismiss()
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user?.isEmailVerified == true) {
                        val userSession = ClientSession(
                            email = user.email.toString(),
                            username = "ucup123",
                            address = "Jl.lorem ipsum"
                        )

                        clientPrefViewModel.saveSessionUser(userSession)

                        val intentToMain = Intent(this, MainClientActivity::class.java)
                        finish()
                        startActivity(intentToMain)
                    } else {
                        Toast.makeText(
                            this,
                            "Please verify your email!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    dialog.dismiss()
                    val exception = task.exception
                    if (exception is FirebaseAuthException) {
                        errorDialog()
                    } else {
                        Toast.makeText(
                            this,
                            "There is problem to create account!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


            }
    }

    private fun checkFillOrNot(): Boolean {

        if (binding.emailUsernameEdt.text.toString().isEmpty()) {
            binding.emailUsernameTIL.error = getString(R.string.not_fill)
            binding.emailUsernameEdt.error = null
        } else if (binding.emailUsernameEdt.error != null) {
            binding.emailUsernameTIL.isErrorEnabled = false
            binding.emailUsernameTIL.error = null
        } else {
            binding.emailUsernameTIL.isErrorEnabled = false
            binding.emailUsernameTIL.clearFocus()
            binding.passTIL.requestFocus()
        }

        if (binding.passwordEditText.text.toString().isEmpty()) {
            binding.passTIL.error = getString(R.string.not_fill)
            binding.passwordEditText.error = null
        } else if (binding.passwordEditText.error != null) {
            binding.passTIL.isErrorEnabled = false
            binding.passTIL.error = null
        } else {

            binding.passTIL.isErrorEnabled = false
            binding.passTIL.clearFocus()
        }


        return !((binding.emailUsernameTIL.error != null && binding.emailUsernameEdt.error == null)
                || (binding.passTIL.error != null || binding.passwordEditText.error != null))
    }

    private fun errorDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_error_login, null)
        val builder = AlertDialog.Builder(this@LoginActivity)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        view.findViewById<Button>(R.id.btn_dismiss).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun loadingDialog(): AlertDialog {
        val view = layoutInflater.inflate(R.layout.dialog_loading, null)
        val builder = AlertDialog.Builder(this@LoginActivity)
        builder.setView(view)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }
}