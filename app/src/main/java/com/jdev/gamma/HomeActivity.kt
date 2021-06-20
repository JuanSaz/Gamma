package com.jdev.gamma

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*


enum class ProviderType{

    Email

}
class HomeActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        homeBanner()
    }


    private fun homeBanner() {
        val now = Calendar.getInstance()
        val nowInMinutes = now[Calendar.HOUR_OF_DAY] * 60 + now[Calendar.MINUTE]
        val dateInMinutesToCompareDay = 6 * 60
        val dateInMinutesToCompareNight = 20 * 60
        var isNight: Boolean = false
        println(isNight)
        when {
            nowInMinutes == dateInMinutesToCompareDay -> {
                println("Day")
                isNight = false
            }
            nowInMinutes > dateInMinutesToCompareNight -> {
                println("Night")
                isNight = true
            }
            nowInMinutes > dateInMinutesToCompareDay -> {
                println("Day")
                isNight = false
            }
        }
        if (isNight) {
            textBienvenida.text = "Buenas Noches"
        } else {
            textBienvenida.text = "Buenos Dias"
        }

        val email = intent.extras!!.getString("email")
        if (email != null) {
            db.collection("users").document(email).get().addOnSuccessListener {
                textNombre.text = (it.get("username")as String?)
            }
        }

 }





        // DEBUG //

        fun toasted(view: View) {
            val toast = Toast.makeText(applicationContext, "text", Toast.LENGTH_SHORT)
            toast.show()
        }




    }



