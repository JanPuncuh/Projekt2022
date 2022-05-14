package com.example.projektnpo

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationRequest
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.projektnpo.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices


class MainActivity : AppCompatActivity(), SensorEventListener {
    var serverResponse = ""
    lateinit var sensorManager: SensorManager
    lateinit var accelerometer: Sensor
    lateinit var gyroscope: Sensor

    private var fusedLocationClient: FusedLocationProviderClient? = null

    private var lastLocation: Location? = null
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var requestingLocationUpdates = false

    companion object {
        val REQUEST_CHECK_SETTINGS = 20202
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

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
    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val sensorName: String = event.sensor!!.name
        Log.d(
            "Sensor",
            sensorName + ": X: " + event.values[0] + "; Y: " + event.values[1] + "; Z: " + event.values[2] + ";"
        )

        //todo event.timestamp

        if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            binding.textView.text = event.values[0].toString()
            binding.textView2.text = event.values[1].toString()
            binding.textView3.text = event.values[2].toString()
        }
        else if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            val x = event.values[0] * (180 / Math.PI)
            val y = event.values[1] * (180 / Math.PI)
            val z = event.values[2] * (180 / Math.PI)

            binding.textView4.text = x.toString()
            binding.textView5.text = y.toString()
            binding.textView6.text = z.toString()
        }
        postData("xd");
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }


     fun postData(text: String) {
         val queue = Volley.newRequestQueue(this)
         val url = "http://92.37.53.18:8080/cesta"
//GET
/*
// Request a string response from the provided URL.
         val stringRequest = StringRequest(Request.Method.GET, url,
             Response.Listener<String> { response ->
                 // Display the first 500 characters of the response string.
                 Log.d(
                     "VREI",
                     "Response is: ${response.substring(0, 500)}"
                 )
             },
             Response.ErrorListener {  Log.d(
                 "VREI",
                 "no"
             ) })

// Add the request to the RequestQueue.
         queue.add(stringRequest)*/




         // POST
        val stringRequest: StringRequest = object : StringRequest(
             Method.POST, url,
             Response.Listener { response -> Log.d(
                 "VREI",
                 "Response is: ${response.substring(0, 42)}"
             ) },
             Response.ErrorListener {  Log.d(
                 "VREI",
                 "That didnt wor"
             ) }) {
             override fun getParams(): kotlin.collections.Map<String, String> {
                 val paramV: MutableMap<String, String> = HashMap()
                 paramV["user_id"] = "Vrbko";
                 paramV["Latitude"] = "123";
                 paramV["Longitude"] = "321";
                 paramV["Altitude"] = "55";
                 return paramV
             }
         }
         queue.add(stringRequest)






    }
}