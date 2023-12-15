package com.capstoneproject.clothizeapp.client.ui.client.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.clothizeapp.R
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.ClientPrefViewModel
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.ClientPreferencesFactory
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.ClientSession
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.UserPreferences
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.dataStore
import com.capstoneproject.clothizeapp.client.ui.custom_view.CustomEmailEditText
import com.capstoneproject.clothizeapp.client.ui.custom_view.CustomPasswordEditText
import com.capstoneproject.clothizeapp.client.ui.login.LoginActivity
import com.capstoneproject.clothizeapp.databinding.FragmentProfileBinding
import com.capstoneproject.clothizeapp.utils.getImageUri
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var clientPrefViewModel: ClientPrefViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var loadingDialog: AlertDialog
    private lateinit var clientSession: ClientSession
    private var currentImageUri: Uri? = null

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Toast.makeText(requireActivity(), "No media selected", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        init()

        return binding.root
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()

        val pref = UserPreferences.getInstance(requireActivity().dataStore)
        clientPrefViewModel =
            ViewModelProvider(this, ClientPreferencesFactory(pref))[ClientPrefViewModel::class.java]
        profileViewModel = obtainViewModel(requireActivity())

        loadingDialog = loadingDialog()

        clientPrefViewModel.getSessionUser().observe(requireActivity()) { session ->
            if (session != null) {
                clientSession = session
                showUser(clientSession)
            }
        }

        binding.btnSave.setOnClickListener {
            configureSpecificElement("edit")
        }

        binding.btnCancel.setOnClickListener {
            configureSpecificElement("cancel")
        }

        binding.tvChangePass.setOnClickListener {
            changeAccountDialog(clientSession)
        }

        binding.tvChangePhoto.setOnClickListener {
            pickerImageDialog()
        }
    }

    private fun showUser(userSession: ClientSession) {
        binding.tvUsername.text = userSession.username
        binding.nameEditText.setText(userSession.fullName)
        binding.phoneEditText.setText(userSession.phone)
        binding.addressEditText.setText(userSession.address)
    }

    private fun configureSpecificElement(tag: String) {
        if (tag == "edit") {
            if (binding.btnSave.text == "Save") {
                hideElement()
                updateUserData()
            } else {
                showElement()
            }
        } else {
            if (binding.btnCancel.text == "Cancel") {
                hideElement()
            } else {
                clientPrefViewModel.logoutUser()
                val intentToLogin = Intent(activity, LoginActivity::class.java)
                activity?.finish()
                startActivity(intentToLogin)
            }
        }
    }

    private fun showElement() {
        binding.tvChangePhoto.visibility = View.VISIBLE
        binding.btnSave.text = "Save"
        binding.btnCancel.text = "Cancel"

        binding.nameEditText.isFocusable = true
        binding.nameEditText.isFocusableInTouchMode = true

        binding.phoneEditText.isFocusable = true
        binding.phoneEditText.isFocusableInTouchMode = true

        binding.addressEditText.isFocusable = true
        binding.addressEditText.isFocusableInTouchMode = true
    }

    private fun hideElement() {
        binding.tvChangePhoto.visibility = View.GONE
        binding.btnSave.text = "Edit"
        binding.btnCancel.text = "Logout"

        binding.nameEditText.isFocusable = false
        binding.nameEditText.isFocusableInTouchMode = false

        binding.phoneEditText.isFocusable = false
        binding.phoneEditText.isFocusableInTouchMode = false

        binding.addressEditText.isFocusable = false
        binding.addressEditText.isFocusableInTouchMode = false
    }

    private fun changeAccountDialog(clientSession: ClientSession) {
        val view = layoutInflater.inflate(R.layout.dialog_change_password, null)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val email = view.findViewById<CustomEmailEditText>(R.id.emailEditText)
        val currentPass =
            view.findViewById<CustomPasswordEditText>(R.id.passEditText)
        val newPass =
            view.findViewById<CustomPasswordEditText>(R.id.newPassEditText)

        email.setText(clientSession.email)


        view.findViewById<AppCompatButton>(R.id.btn_replace).setOnClickListener {
            changeAccount(
                email.text.toString(),
                currentPass.text.toString(),
                newPass.text.toString(),
                clientSession,
                view
            )
        }

        view.findViewById<AppCompatButton>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun changeAccount(
        newMail: String,
        oldPassword: String,
        newPassword: String,
        session: ClientSession,
        view: View,
    ) {
        val user = auth.currentUser

        if (checkFillOrNot(view)) {
            Log.d("TAG", "changeAccountDialog: $newMail, $oldPassword, $newPassword")
            val credential = EmailAuthProvider.getCredential(session.email, oldPassword)
            loadingDialog.show()
            user?.reauthenticate(credential)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (newPassword.isEmpty()) {
                        user.verifyBeforeUpdateEmail(newMail)
                            .addOnCompleteListener { status ->
                                loadingDialog.dismiss()
                                if (status.isSuccessful) {
                                    successDialog()
                                    session.email = newMail
                                } else {
                                    errorDialog()
                                }
                            }.addOnFailureListener {
                                loadingDialog.dismiss()
                                errorDialog()
                            }
                    } else {
                        user.updatePassword(newPassword).addOnCompleteListener { task ->
                            loadingDialog.dismiss()
                            if (task.isSuccessful) {
                                successDialog()
                            } else {
                                errorDialog()
                            }
                        }.addOnFailureListener {
                            loadingDialog.dismiss()
                            errorDialog()
                        }
                    }
                } else {
                    loadingDialog.dismiss()
                    errorDialog()
                }
            }?.addOnFailureListener {
                loadingDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    "Your password doesn't match!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }


    private fun updateUserData() {
        val fullName = binding.nameEditText.text.toString()
        val phone = binding.phoneEditText.text.toString()
        val address = binding.addressEditText.text.toString()
        var isSuccessAdd = false

        clientPrefViewModel.getSessionUser().observe(requireActivity()) { session ->
            isSuccessAdd = if (session != null) {
                clientSession = ClientSession(
                    username = session.username,
                    email = session.email,
                    fullName = fullName,
                    phone = phone,
                    address = address,
                )
                true
            } else {
                false

            }
        }

        if (isSuccessAdd) {
            clientPrefViewModel.saveSessionUser(clientSession)
            successDialog()
        } else errorDialog()


    }

    private fun obtainViewModel(activity: Activity): ProfileViewModel {
        val factory = ProfileViewModelFactory.getInstance(activity.applicationContext)
        return ViewModelProvider(
            this, factory
        )[ProfileViewModel::class.java]
    }

    private fun loadingDialog(): AlertDialog {
        val view = layoutInflater.inflate(R.layout.dialog_loading, null)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    private fun successDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_success_change_profile, null)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        view.findViewById<Button>(R.id.btn_dismiss).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun errorDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_error_change_profile, null)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        view.findViewById<Button>(R.id.btn_dismiss).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun checkFillOrNot(view: View): Boolean {
        val fillEmail = view.findViewById<CustomEmailEditText>(R.id.emailEditText)
        val fillOldPass = view.findViewById<CustomPasswordEditText>(R.id.passEditText)
        val fillNewPass = view.findViewById<CustomPasswordEditText>(R.id.newPassEditText)

        val fillEmailTIL = view.findViewById<TextInputLayout>(R.id.emailEditTextLayout)
        val fillOldPassTIL = view.findViewById<TextInputLayout>(R.id.passwordEditTextLayout)
        val fillNewPassTIL = view.findViewById<TextInputLayout>(R.id.newPasswordEditTextLayout)


        if (fillEmail.text.toString().isEmpty()) {
            fillEmailTIL.error = getString(R.string.not_fill)
            fillEmail.error = null
        } else {
            fillEmailTIL.error = null
        }

        if (fillOldPass.text.toString().isEmpty()) {
            fillOldPassTIL.error = getString(R.string.not_fill)
            fillOldPass.error = null
        } else {
            fillOldPassTIL.error = null
        }

        if (fillOldPass.text.toString().isEmpty()) {
            fillNewPassTIL.error = getString(R.string.not_fill)
            fillNewPass.error = null
        } else {
            fillNewPassTIL.error = null
        }

        return if (fillNewPass.text.toString().isEmpty()) {
            (fillEmail.text.toString().isNotEmpty() && fillOldPass.text.toString().isNotEmpty())
        } else {
            (fillEmail.text.toString().isNotEmpty() && fillOldPass.text.toString()
                .isNotEmpty() && fillNewPass.text.toString().isNotEmpty())
        }


    }

    private fun pickerImageDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        with(alertDialogBuilder) {
            setTitle("Picker Imager")
            setMessage("Choose resource to pick your image!")
            setCancelable(false)
            setPositiveButton("Gallery") { _, _ ->
                launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            setNegativeButton("Camera") { _, _ ->
                currentImageUri = getImageUri(requireActivity())
                launcherIntentCamera.launch(currentImageUri)
            }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.avatar.setImageURI(it)
            profileViewModel.imageUri.postValue(it)
        }
    }
}