package com.example.projektnpo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.projektnpo.databinding.ActivityMain2Binding
import com.example.projektnpo.databinding.ActivityMainBinding
///LOGIN
class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun onLogin(view: View){
        val queue = Volley.newRequestQueue(this)
        val url = "http://146.212.216.121:8080/users/login"

        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                Toast.makeText(this@MainActivity2, "Uspešna prijava", Toast.LENGTH_SHORT).show();
                Log.d(

                    "Login",
                    "Response is: ${response.substring(0, response.length)}"
                )
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("user_id", binding.editTextTextPersonName.text.toString());
                startActivity(intent)
            },
            Response.ErrorListener {
                Toast.makeText(this@MainActivity2, "Neuspešna prijava", Toast.LENGTH_SHORT).show();
                Log.d(
                    "LoginNotGood:)",
                    "That didnt work" + it.toString()
                )
            }) {
            override fun getParams(): Map<String, String> {
                val paramV: MutableMap<String, String> = HashMap()
                paramV["username"] = binding.editTextTextPersonName.text.toString();

                paramV["password"] = binding.editTextTextPassword.text.toString();



                return paramV
            }
        }
        queue.add(stringRequest)
    }

    fun onRegister(view: View){
        val intent = Intent(this, MainActivity3::class.java)

        startActivity(intent)
    }
}