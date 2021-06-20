package com.jdev.gamma

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_auth.iniciarSesionButton
import kotlinx.android.synthetic.main.activity_auth.inputContraseña
import kotlinx.android.synthetic.main.activity_auth.inputEmail
import kotlinx.android.synthetic.main.activity_create.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)



        //Setup
        setup()
        createAccountText()
    }


    private fun createAccountText(){
        val spannableString = SpannableString("Crea una aquí")
        val clickableSpan= object : ClickableSpan() {
            override fun onClick(widget: View) {
                showCreate()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color= Color.rgb(	250, 115, 22)
                ds.isUnderlineText= (false)
                createText.highlightColor = Color.TRANSPARENT;
            }
        }
        spannableString.setSpan(clickableSpan,0,13, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        createText.text=spannableString
        createText.movementMethod= LinkMovementMethod.getInstance()
    }

    private fun setup(){
        iniciarSesionButton.setOnClickListener{
            if (inputEmail.text.isNotEmpty() && inputContraseña.text.isNotEmpty()){

            FirebaseAuth.getInstance().signInWithEmailAndPassword(inputEmail.text.toString(), inputContraseña.text.toString()) .addOnCompleteListener {

                if(it.isSuccessful){
                    showHome(it.result?.user?.email ?: "", ProviderType.Email)
                    val toast = Toast.makeText(applicationContext, "Sesión Iniciada", Toast.LENGTH_SHORT)
                    toast.show()
                }
                    else{
                        showError()
                        }
                    }
                }
            }
    }


    private fun showCreate(){
        createText.setOnClickListener{
            val createIntent = Intent(this, createActivity::class.java).apply {
            }
            startActivity(createIntent)
        }
    }




    private fun showError(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Se ha producido un error al iniciar sesión.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()


    }

    private fun showHome(email: String, provider: ProviderType){
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(homeIntent)
    }

}