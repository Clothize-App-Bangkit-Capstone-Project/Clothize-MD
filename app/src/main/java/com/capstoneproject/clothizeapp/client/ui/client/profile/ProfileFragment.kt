package com.capstoneproject.clothizeapp.client.ui.client.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.clothizeapp.R
import com.capstoneproject.clothizeapp.data.local.preferences.client.ClientPrefViewModel
import com.capstoneproject.clothizeapp.data.local.preferences.client.UserPreferences
import com.capstoneproject.clothizeapp.data.local.preferences.client.ClientPreferencesFactory
import com.capstoneproject.clothizeapp.data.local.preferences.client.ClientSession
import com.capstoneproject.clothizeapp.data.local.preferences.client.dataStore
import com.capstoneproject.clothizeapp.databinding.FragmentProfileBinding
import com.capstoneproject.clothizeapp.ui.custom_view.CustomPasswordEditText
import com.capstoneproject.clothizeapp.ui.login.LoginActivity


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var clientPrefViewModel: ClientPrefViewModel
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        init()



        return binding.root
    }

    private fun init(){
        val pref = UserPreferences.getInstance(requireActivity().dataStore)
        clientPrefViewModel = ViewModelProvider(this, ClientPreferencesFactory(pref))[ClientPrefViewModel::class.java]
        profileViewModel = obtainViewModel(requireActivity())
        clientPrefViewModel.getSessionUser().observe(requireActivity()){ session ->
            if (session != null){
                showUser(session)
            }
        }

        binding.btnSave.setOnClickListener {
            configureSpecificElement("edit")
        }

        binding.btnCancel.setOnClickListener {
            configureSpecificElement("cancel")
        }

        binding.tvChangePass.setOnClickListener {
            changePasswordDialog()
        }
    }

    private fun showUser(userSession: ClientSession){
        binding.tvUsername.text = userSession.username
        binding.emailEditText.setText(userSession.email)
        binding.nameEditText.setText(userSession.fullName)
        binding.phoneEditText.setText(userSession.phone)
        binding.addressEditText.setText(userSession.address)
    }

    private fun configureSpecificElement(tag: String){
        if (tag == "edit"){
            if (binding.btnSave.text == "Save"){
                hideElement()
            }else {
                showElement()
            }
        }else{
            if (binding.btnCancel.text == "Cancel"){
                hideElement()
            }else {
                clientPrefViewModel.logoutUser()
                val intentToLogin = Intent(activity, LoginActivity::class.java)
                activity?.finish()
                startActivity(intentToLogin)
            }
        }
    }

    private fun showElement(){
        binding.tvChangePhoto.visibility = View.VISIBLE
        binding.btnSave.text = "Save"
        binding.btnCancel.text = "Cancel"

        binding.emailEditText.isFocusable = true
        binding.emailEditText.isFocusableInTouchMode = true

        binding.nameEditText.isFocusable = true
        binding.nameEditText.isFocusableInTouchMode = true

        binding.phoneEditText.isFocusable = true
        binding.phoneEditText.isFocusableInTouchMode = true

        binding.addressEditText.isFocusable = true
        binding.addressEditText.isFocusableInTouchMode = true
    }

    private fun hideElement(){
        binding.tvChangePhoto.visibility = View.GONE
        binding.btnSave.text = "Edit"
        binding.btnCancel.text = "Logout"

        binding.emailEditText.isFocusable = false
        binding.emailEditText.isFocusableInTouchMode = false

        binding.nameEditText.isFocusable = false
        binding.nameEditText.isFocusableInTouchMode = false

        binding.phoneEditText.isFocusable = false
        binding.phoneEditText.isFocusableInTouchMode = false

        binding.addressEditText.isFocusable = false
        binding.addressEditText.isFocusableInTouchMode = false
    }

    private fun changePasswordDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_change_password, null)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val currentPass = view.findViewById<CustomPasswordEditText>(R.id.passwordEditText).toString()
        val newPass = view.findViewById<CustomPasswordEditText>(R.id.newPasswordEditText).toString()

        view.findViewById<AppCompatButton>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
            // Change Password Here
        }
        view.findViewById<AppCompatButton>(R.id.btn_replace).setOnClickListener {
            dialog.dismiss()
        }

    }


    private fun updateUserData(){

    }

    private fun obtainViewModel(activity: Activity): ProfileViewModel {
        val factory = ProfileViewModelFactory.getInstance(activity.applicationContext)
        return ViewModelProvider(
            this, factory
        )[ProfileViewModel::class.java]
    }



}