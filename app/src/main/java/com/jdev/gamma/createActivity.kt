package com.jdev.gamma

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_auth.inputContraseña
import kotlinx.android.synthetic.main.activity_auth.inputEmail
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.activity_home.*


class createActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        setup()
        passData()
        getDB()
    }

    private fun setup(){
        backButton.setOnClickListener(View.OnClickListener {
            finish()
            }
        )

        crearCuentaButton.setOnClickListener{
            if (inputEmail.text.isNotEmpty() && inputContraseña.text.isNotEmpty()&& inputUsername.text.isNotEmpty()){
                db.collection("users").document(inputEmail.text.toString()).set(
                    hashMapOf("email" to inputEmail.text.toString(),
                             "username" to inputUsername.text.toString(),
                             "password" to inputContraseña.text.toString()
                    )
                )
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(inputEmail.text.toString(), inputContraseña.text.toString()) .addOnCompleteListener {
                    if (it.isSuccessful){
                        showAuth(it.result?.user?.email ?: "", ProviderType.Email)
                        val toast =Toast.makeText(applicationContext, "Usuario Creado", Toast.LENGTH_SHORT)
                        toast.show()
                    } else {
                        val toast =Toast.makeText(applicationContext, "Datos erroneos", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
            }
          }
       }

    private fun getDB(){
        check_Button.setOnClickListener{
                    db.collection("users").document(inputEmail.text.toString()).get().addOnSuccessListener { document ->
                    if(document.exists()){
                        val username: String? =document.getString("username")
                        val toast =Toast.makeText(applicationContext, "Usuario existente", Toast.LENGTH_SHORT)                                  // Sintax para imprimir variables y texto
                        Log.d("Doc Data: ","$username")
                    }
                    else{
                        Log.d("Doc Data: ","Null")
                        val toast =Toast.makeText(applicationContext, "Usuario inexistente", Toast.LENGTH_SHORT)
                    }
                }
            }
        }

    private fun showError(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Se ha producido un error a la hora de crear la cuenta")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAuth(email: String, provider: ProviderType){
        val authIntent = Intent(this, AuthActivity::class.java).apply {
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(authIntent)
    }


    private fun passData(){                                                             // PASAR DATOS ENTRE ACTIVITIES DIFERENTES
        val intent1 = Intent(this , HomeActivity::class.java)
        intent.putExtra("email",inputEmail.text.toString()) // pass your values and retrieve them in the other Activity using "email"
    }
}
