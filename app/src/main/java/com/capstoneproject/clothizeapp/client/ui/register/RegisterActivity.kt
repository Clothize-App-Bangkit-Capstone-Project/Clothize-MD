package com.capstoneproject.clothizeapp.client.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.clothizeapp.R
import com.capstoneproject.clothizeapp.client.ui.login.LoginActivity
import com.capstoneproject.clothizeapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()


    }

    private fun init() {
        auth = Firebase.auth

        binding.btnRegisBack.setOnClickListener {
            if (checkFillOrNot()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        binding.btnRegis.setOnClickListener {
            if (checkFillOrNot()) {
                setNewUser()
            }
        }
    }

    private fun checkFillOrNot(): Boolean {
        if (binding.nameRegis.text.toString().isEmpty()) {
            binding.nameEditTextLayout.error = getString(R.string.not_fill)
        } else {
            binding.nameEditTextLayout.error = null
            binding.nameEditTextLayout.isErrorEnabled = false
            binding.nameEditTextLayout.clearFocus()
            binding.emailEditTextLayout.requestFocus()

        }

        if (binding.emailRegis.text.toString().isEmpty()) {
            binding.emailEditTextLayout.error = getString(R.string.not_fill)
            binding.emailRegis.error = null
        } else if (binding.emailRegis.error != null) {
            binding.emailEditTextLayout.isErrorEnabled = false
            binding.emailEditTextLayout.error = null
        } else {
            binding.emailEditTextLayout.isErrorEnabled = false
            binding.emailEditTextLayout.clearFocus()
            binding.passwordEditTextLayout.requestFocus()
        }

        if (binding.passwordRegis.text.toString().isEmpty()) {
            binding.passwordEditTextLayout.error = getString(R.string.not_fill)
            binding.passwordRegis.error = null
        } else if (binding.passwordRegis.error != null) {
            binding.passwordEditTextLayout.isErrorEnabled = false
            binding.passwordEditTextLayout.error = null
        } else {

            binding.passwordEditTextLayout.isErrorEnabled = false
            binding.passwordEditTextLayout.clearFocus()
        }


        return !(binding.nameEditTextLayout.error != null ||
                (binding.emailEditTextLayout.error != null && binding.emailRegis.error == null) ||
                (binding.passwordEditTextLayout.error != null || binding.passwordRegis.error != null))
    }

    private fun setNewUser() {
        val email = binding.emailRegis.text.toString()
        val password = binding.passwordRegis.text.toString()


        loadingDialog(true)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    loadingDialog(false)
                    auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                        successDialog()
                    }?.addOnFailureListener {
                        Toast.makeText(
                            this,
                            "There is problem to verify account!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    loadingDialog(false)
                    val exception = task.exception
                    if (exception is FirebaseAuthUserCollisionException) {
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

    private fun successDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_success_register, null)
        val builder = AlertDialog.Builder(this@RegisterActivity)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        view.findViewById<Button>(R.id.btn_dismiss).setOnClickListener {
            dialog.dismiss()
            val intentToLogin = Intent(this, LoginActivity::class.java)
            intentToLogin.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intentToLogin)
            finish()
        }
    }

    private fun errorDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_error_register, null)
        val builder = AlertDialog.Builder(this@RegisterActivity)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        view.findViewById<Button>(R.id.btn_dismiss).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun loadingDialog(state: Boolean) {
        val view = layoutInflater.inflate(R.layout.dialog_loading, null)
        val builder = AlertDialog.Builder(this@RegisterActivity)
        builder.setView(view)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        if (state) dialog.show() else dialog.dismiss()
    }
}