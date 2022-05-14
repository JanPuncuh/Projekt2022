package com.example.projektnpo

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.projektnpo.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices


import com.example.projektnpo.getMyLastLocation.Companion.PERMISSION_ID






class MainActivity : AppCompatActivity(), SensorEventListener {
    var iterator: Int = 0
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
    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)

    }

    override fun onSensorChanged(event: SensorEvent) {

        iterator++
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



        if(iterator == 100){
            val gtml = getMyLastLocation(this)
            gtml.verifyGooglePlayServices()
            gtml.getLastLocation { result: resultLocatioinRequest ->

                when (result) {

                    is resultLocatioinRequest.Sucesso -> {
                        val address = (result as resultLocatioinRequest.Sucesso).adress

                        Log.d("Errorji", "Baje da so koordinate " + address.postalCode +  " ++ "  + address.latitude.toString() + " , " +address.longitude.toString())
                        binding.textView7.text  = address.latitude.toString()
                        binding.textView8.text = address.longitude.toString()
                      //  binding.textView9.text = address.postalCode
                    }
                    is resultLocatioinRequest.Erro -> {
                        val msg = (result as resultLocatioinRequest.Erro).msg
                        Log.d("Errorji", msg)
                    }
                }
            }

            Log.d(
                "Tobiposlau?",
                "Accelertation  x,y,z: " + binding.textView.text.toString() + " "
                        +  binding.textView2.text.toString() + " " +
                            binding.textView3.text.toString() + "  ,  Gyroscope x,y,z:  " +
                        binding.textView4.text.toString() + " " +
                        binding.textView5.text.toString() + " " +
                        binding.textView6.text.toString() + " ." +
                        "\n Lokacija " + binding.textView7.text.toString() + "  , " + binding.textView8.text.toString()

            )
          //  postData("xd");
            iterator = 0


            if(binding.textView7.text.toString() == "TextView")
                return
            val queue = Volley.newRequestQueue(this)
            val url = "http://46.164.31.241:8080/cesta"

            val stringRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response -> Log.d(
                    "VREI",
                    "Response is: ${response.substring(0, response.length)}"
                ) },
                Response.ErrorListener {  Log.d(
                    "VREI",
                    "That didnt wor"
                ) }) {
                override fun getParams(): Map<String, String> {
                    val paramV: MutableMap<String, String> = HashMap()
                    paramV["user_id"] = "Vrbko";
                    paramV["gyroX"] = binding.textView4.text.toString();
                    paramV["gyroY"] = binding.textView5.text.toString();
                    paramV["gyroZ"] = binding.textView6.text.toString();

                    paramV["accelX"] = binding.textView.text.toString();
                    paramV["accelY"] = binding.textView2.text.toString();
                    paramV["accelZ"] = binding.textView3.text.toString();

                    paramV["latitude"] =binding.textView7.text.toString();
                    paramV["longitude"] = binding.textView8.text.toString();

                    return paramV
                }
            }
            queue.add(stringRequest)
        }










    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }





    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Log.i("Errorji", "\n" +
                        "Dovoljenje za dostop do lokacije je odobreno")
            } else {
                finish()
            }
        }
    }

}