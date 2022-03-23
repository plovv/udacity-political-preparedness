package com.example.android.politicalpreparedness.representative

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale


class DetailFragment : Fragment() {

    private val representativeViewModel: RepresentativeViewModel by viewModel()
    private lateinit var binding: FragmentRepresentativeBinding
    private val cancellationTokenSource = CancellationTokenSource()

    @SuppressLint("MissingPermission")
    private val permissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            getLocation()
        } else {
            Toast
                .makeText(requireContext(), "Location permission is required to use my location.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = representativeViewModel

        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            representativeViewModel.retrieveRepresentatives()
        }
        binding.buttonLocation.setOnClickListener {
            requestLocation()
        }
        binding.state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                representativeViewModel.state.value = p0?.selectedItem as String
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // nothing
            }

        }
        binding.listRepresentatives.adapter = RepresentativeListAdapter()

        representativeViewModel.showError.observe(this.viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                representativeViewModel.errorShown()
            }
        }

        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        if (isPermissionGranted()) {
            getLocation()
        } else {
            permissionRequest.launch(ACCESS_FINE_LOCATION)
        }
    }

    private fun isPermissionGranted() : Boolean {
        return ActivityCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
               ActivityCompat.checkSelfPermission(requireContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresPermission(ACCESS_FINE_LOCATION)
    private fun getLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token).addOnSuccessListener { location ->
            if (location != null) {
                val address = geoCodeLocation(location)

                representativeViewModel.setAddressFromMyLocation(address)
                representativeViewModel.retrieveRepresentatives()
            }
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onDestroy() {
        cancellationTokenSource.cancel()
        super.onDestroy()
    }

}