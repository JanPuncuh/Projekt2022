package com.example.projektnpo


import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.projektnpo.databinding.ActivityMain4Binding
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream
import java.util.*


class MainActivity4 : AppCompatActivity() {
    private lateinit var binding: ActivityMain4Binding
    lateinit var browse: Button
    lateinit var upload: Button
    lateinit var img: ImageView
    lateinit var bitmap: Bitmap
    lateinit var id: String
    lateinit var uniqueID: String
    var encodeImageString: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        val b = intent.extras
         id = b!!.getString("user_id").toString()
        super.onCreate(savedInstanceState)
        binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)
        uniqueID= UUID.randomUUID().toString()
        browse = binding.browse;
        upload = binding.upload;
        img = binding.img;
        browse!!.setOnClickListener {
            Dexter.withActivity(this@MainActivity4)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(Intent.createChooser(intent, "Browse Image"), 1)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {}
                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        }
        upload!!.setOnClickListener { uploaddatatodb() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val filepath = data!!.data
            try {
                val inputStream = contentResolver.openInputStream(filepath!!)
                bitmap = BitmapFactory.decodeStream(inputStream)
                img!!.setImageBitmap(bitmap)
                encodeBitmapImage(bitmap)
            } catch (ex: Exception) {
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

  /* private fun encodeBitmapImage(bitmap: Bitmap) {
        val resizedBitmap = Bitmap.createScaledBitmap(
            bitmap, 2700, 2100, false
        )
        val byteArrayOutputStream = ByteArrayOutputStream()
        resizedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream)
        val bytesofimage = byteArrayOutputStream.toByteArray()
        encodeImageString = Base64.encodeToString(bytesofimage, Base64.DEFAULT)
    }*/
  // ČE BOJO PROBLEMI TUKAJ SPREMENIMA REZOLUCJIO ČE BO TIMEOUT
    private fun encodeBitmapImage(bitmap: Bitmap?) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream)
        val bytesofimage = byteArrayOutputStream.toByteArray()
        encodeImageString = Base64.encodeToString(bytesofimage, Base64.DEFAULT)
    }

    private fun uploaddatatodb() {
        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
             //   img!!.setImageResource(R.drawable.ic_launcher_foreground)
               // Toast.makeText(applicationContext, response, Toast.LENGTH_LONG).show()
                var x = response.substring(2, 3)
                if((id == "Vrbko" && x == "1") || (id == "Jann" && x == "0") ){
                    Toast.makeText(this@MainActivity4, "Pozdravljen ${id}", Toast.LENGTH_SHORT).show();
                    Log.d(

                        "SlikaGood",
                        "Response is: ${x} +   id   + ${id}"
                    )
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("user_id", id);
                    intent.putExtra("uuid", uniqueID);
                    startActivity(intent)
                    finish()

                }
                else{
                    Toast.makeText(this@MainActivity4, "Nepravilna prijava", Toast.LENGTH_SHORT).show();
                }

            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    applicationContext,
                    error.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map: MutableMap<String, String> = HashMap()
                map["upload"] = encodeImageString!!
                map["uuid"] = uniqueID
                map["username"] = id
                return map
            }
        }
        val queue = Volley.newRequestQueue(applicationContext)
        queue.add(request)
    }

    companion object {
        private const val url = "http://146.212.216.121:8080/photo/api"
    }
}