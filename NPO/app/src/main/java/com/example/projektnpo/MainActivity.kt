package com.example.projektnpo


import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.projektnpo.databinding.ActivityMainBinding
import com.example.projektnpo.utils.PermissionUtils
import com.google.android.gms.location.*
import java.util.*


class MainActivity : AppCompatActivity(), SensorEventListener {
    var iterator: Int = 0
    var id: String = "";
    var uniqueID: String=""
    lateinit var sensorManager: SensorManager
    lateinit var accelerometer: Sensor
    lateinit var gyroscope: Sensor
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = intent.extras
        id = b!!.getString("user_id").toString()
        uniqueID = b!!.getString("uuid").toString()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        //linear acceleration odšteje vektor gravitacije
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)


        if (Build.VERSION.SDK_INT > 9) {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        uniqueID = ""
    }

    override fun onResume() {
        super.onResume()
        uniqueID = UUID.randomUUID().toString()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)

    }

    override fun onSensorChanged(event: SensorEvent) {

        iterator++
        /*val sensorName: String = event.sensor!!.name
        Log.d(
            "Sensor",
            sensorName + ": X: " + event.values[0] + "; Y: " + event.values[1] + "; Z: " + event.values[2] + ";"
        )*/

        if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            binding.textView.text = event.values[0].toString()
            binding.textView2.text = event.values[1].toString()
            binding.textView3.text = event.values[2].toString()
        }
        else if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            //iz radianov v stopinje
            val x = event.values[0] * (180 / Math.PI)
            val y = event.values[1] * (180 / Math.PI)
            val z = event.values[2] * (180 / Math.PI)
            binding.textView4.text = x.toString()
            binding.textView5.text = y.toString()
            binding.textView6.text = z.toString()
        }

        //da mal počaka med pošiljanjem
        if (iterator == 300) {


            Log.d(
                "Tobiposlau?",
                "Accelertation  x,y,z: " + binding.textView.text.toString() + " "
                        + binding.textView2.text.toString() + " " +
                        binding.textView3.text.toString() + "  ,  Gyroscope x,y,z:  " +
                        binding.textView4.text.toString() + " " +
                        binding.textView5.text.toString() + " " +
                        binding.textView6.text.toString() + " ." +
                        "\n Lokacija " + binding.textView7.text.toString() + "  , " + binding.textView8.text.toString() + " uniqueID: " +uniqueID

            )
            iterator = 0

            if (binding.textView7.text.toString() == "TextView")
                return
            val queue = Volley.newRequestQueue(this)
            val url = "http://146.212.216.121:8080/cesta"

            val stringRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    Log.d(
                        "VREI",
                        "Response is: ${response.substring(0, response.length)}"
                    )
                },
                Response.ErrorListener {
                    Log.d(
                        "VREI",
                        "That didnt work"
                    )
                }) {
                override fun getParams(): Map<String, String> {
                    val paramV: MutableMap<String, String> = HashMap()
                    paramV["user_id"] = id;
                    paramV["gyroX"] = binding.textView4.text.toString();
                    paramV["gyroY"] = binding.textView5.text.toString();
                    paramV["gyroZ"] = binding.textView6.text.toString();

                    paramV["accelX"] = binding.textView.text.toString();
                    paramV["accelY"] = binding.textView2.text.toString();
                    paramV["accelZ"] = binding.textView3.text.toString();

                    paramV["latitude"] = binding.textView7.text.toString();
                    paramV["longitude"] = binding.textView8.text.toString();

                    paramV["uid"] = uniqueID;

                    return paramV
                }
            }
            queue.add(stringRequest)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(1000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        Looper.myLooper()?.let {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        for (location in locationResult.locations) {
                            binding.textView7.text = location.latitude.toString()
                            binding.textView8.text = location.longitude.toString()
                            Log.d("lokacija", "Lokacija: " + location.latitude.toString() + " , " +location.longitude.toString())

                        }
                        // Few more things we can do here:
                        // For example: Update the location of user on server
                    }
                },
                it
            )
        }
    }
    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(0),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


}