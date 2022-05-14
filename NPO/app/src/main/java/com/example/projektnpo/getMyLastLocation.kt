package com.example.projektnpo


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.projektnpo.interfaceLastLocation
import com.example.projektnpo.resultLocatioinRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class getMyLastLocation(val act: Activity) : interfaceLastLocation {

    companion object {
        val PERMISSION_ID = 42
    }

    override fun verifyGooglePlayServices() {
        val REQUEST_CODE = 0;

        val codeResult = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(act)

        if (codeResult != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance()
                .getErrorDialog(
                    act,
                    codeResult,
                    REQUEST_CODE,
                    DialogInterface.OnCancelListener {
                        act.finish();
                    })?.show()
        }

        when (codeResult) {
            ConnectionResult.SERVICE_MISSING -> Log.i("Errorji", "\n" +
                    "Nima storitev Google Play")
            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> Log.i(
                "Errorji",
                "Treba je posodobiti storitve Google Play"
            )
            ConnectionResult.SERVICE_MISSING -> Log.i(
                "Errorji",
                "\n" +
                        "Storitve Google Play so onemogočene"
            )
            ConnectionResult.SUCCESS -> {
                Log.i("Errorji", "Google Play Services ok")
            }

        }
    }

    @SuppressLint("MissingPermission")
    override fun getLastLocation(resutCallbeck: (result: resultLocatioinRequest) -> Unit) {

        if (checkPermissions(act)) {
            if (isLocationEnabled(act)) {
                LocationServices.getFusedLocationProviderClient(act).lastLocation.addOnCompleteListener(
                    act
                ) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData(act)
                    } else {

                        location?.let {

                            CoroutineScope(Dispatchers.Main).launch {

                                var addresses = listOf<Address>()

                                try {
                                    addresses = convertLocationAddress(
                                        location,
                                        Geocoder(act, Locale.getDefault())
                                    )
                                } catch (e: Exception) {
                                    Log.i("Errorji", e.message.toString())
                                }

                                if (addresses == null || addresses.isEmpty()) {
                                    // Log.i(TAG, "Nenhum endereço encontrado")
                                    resutCallbeck(resultLocatioinRequest.Erro("Ni nsalova"))

                                } else {
                                    // Log.i(TAG, addresses.toString())
                                    resutCallbeck(resultLocatioinRequest.Sucesso("naslov Najden", addresses.get(0)))
                                }
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(act, "aktiviram lokacijo", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                act.startActivity(intent)
            }
        } else {
            requestPermissions(act)
        }

        //  resutCallbeck(resultLocatioinRequest.Erro("aaaa"))

    }

    private fun checkPermissions(act: Activity): Boolean {
        if (ActivityCompat.checkSelfPermission(
                act,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                act,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("Errorji", "Dovoljenje za dostop do lokacije je odobreno")
            return true
        }
        return false
    }

    private fun isLocationEnabled(act: Activity): Boolean {
        var locationManager: LocationManager =
            act.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var result =
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        if (result) {
            Log.i("Errorji", "lokacija naprave omogočena")
        } else {
            Log.i("Erorrji", "Lokacija naprave je onemogočena")
        }

        return result
    }

    private fun requestPermissions(act: Activity) {
        Log.i("Errorji", "Zahteva dovoljenje za dostop do lokacije")
        ActivityCompat.requestPermissions(
            act,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData(act: Activity) {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 10

        var mFusedLocationClient = LocationServices.getFusedLocationProviderClient(act)
        Looper.myLooper()?.let {
            mFusedLocationClient!!.requestLocationUpdates(
                mLocationRequest,
                getmLocationCallback(act),
                it
            )
        }
    }

    private fun getmLocationCallback(act: Activity): LocationCallback {
        var mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                var mLastLocation: Location = locationResult.lastLocation
                mLastLocation?.let {
                    CoroutineScope(Dispatchers.Main).launch {
                        var addresses = listOf<Address>()
                        try {


                            addresses = convertLocationAddress(
                                mLastLocation,
                                Geocoder(act, Locale.getDefault())
                            )
                        } catch (e: Exception) {
                            Log.i("Erorji", e.message.toString())
                        }

                        if (addresses == null || addresses.isEmpty()) {
                            Log.i("Erorji", "Ni naslova")
                        }
                    }
                }
            }
        }

        return mLocationCallback
    }

    private suspend fun convertLocationAddress(
        location: Location,
        geocoder: Geocoder
    ): List<Address> {

        return withContext(Dispatchers.Default) {
            geocoder.getFromLocation(location.latitude, location.longitude, 1)

        }
    }


}