package com.capstoneproject.clothizeapp.client.ui.client.detail

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.capstoneproject.clothizeapp.R
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.ClientPrefViewModel
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.ClientPreferences
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.ClientPreferencesFactory
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.dataStore
import com.capstoneproject.clothizeapp.client.ui.client.home.OrderFormActivity
import com.capstoneproject.clothizeapp.databinding.ActivityDetailBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class DetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var clientPrefViewModel: ClientPrefViewModel
    private lateinit var geocoder: Geocoder
    private lateinit var mMap: GoogleMap
    private var nameTailor = ""
    private var descriptionTailor = ""
    private var photoTailor = ""
    private var latitude = 0.0
    private var longitude = 0.0

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true
                && permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show()
            } else {
                // No location access granted.
                Toast.makeText(this, "Permission Rejected!", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        loadMapLocation()

    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun init() {

        if (!checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) &&
            !checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            )

        }

        val pref = ClientPreferences.getInstance(application.dataStore)
        clientPrefViewModel =
            ViewModelProvider(this, ClientPreferencesFactory(pref))[ClientPrefViewModel::class.java]

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_view_tailor) as SupportMapFragment
        mapFragment.getMapAsync(this)

        geocoder = Geocoder(this)

        loadContent()

        binding.btnDetailBack.setOnClickListener {
            finish()
        }

        binding.btnOrderTailor.setOnClickListener {
            clientPrefViewModel.getSessionUser().observe(this) { session ->
                if (session != null) {
                    val intentToDetail = Intent(this@DetailActivity, OrderFormActivity::class.java)
                    intentToDetail.putExtra(OrderFormActivity.TAILOR, nameTailor)
                    intentToDetail.putExtra(OrderFormActivity.CLIENT_NAME, session.fullName)
                    startActivity(intentToDetail)
                }

            }
        }
    }

    private fun loadContent() {

        val drawable = CircularProgressDrawable(this)
        drawable.setColorSchemeColors(R.color.brown_gold)
        drawable.centerRadius = 30f
        drawable.strokeWidth = 5f
        drawable.start()

        nameTailor = intent.getStringExtra("nameTailor").toString()
        descriptionTailor = intent.getStringExtra("descriptionTailor").toString()
        photoTailor = intent.getStringExtra("photoTailor").toString()
        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)
        if (nameTailor != "" && descriptionTailor != "" && photoTailor != "") {
            binding.apply {
                detailTitleTailor.text = nameTailor
                detailDescTailor.text = descriptionTailor
                Glide.with(this@DetailActivity)
                    .load(photoTailor)
                    .placeholder(drawable)
                    .transition(
                        DrawableTransitionOptions.withCrossFade(
                            DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
                        )
                    )
                    .into(imgDetail)
            }
        }


    }

    @Suppress("DEPRECATION")
    private fun loadMapLocation() {

        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address = addresses[0].getAddressLine(0)
                    val latLng = LatLng(latitude, longitude)
                    binding.tvLocDetail.text = address
                    Log.d("TAG", "loadMapLocation: $addresses")
                    mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(nameTailor)
                    )
                    mMap.setOnMarkerClickListener { marker ->
                        openGoogleMaps(latLng, nameTailor)
                        true
                    }
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                } else {
                    Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error Load Location!", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun openGoogleMaps(location: LatLng, placeName: String) {
        val uri =
            "geo:${location.latitude},${location.longitude}?z=15&q=${location.latitude},${location.longitude}($placeName)"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }


}