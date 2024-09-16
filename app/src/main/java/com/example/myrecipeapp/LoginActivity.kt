package com.example.myrecipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        emailEditText = findViewById(R.id.etEmailLogin)
        passwordEditText = findViewById(R.id.etPassLogin)
        signInButton = findViewById(R.id.btnLogin)

        signInButton.setOnClickListener{
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            performLogin(email,password)
        }
    }

    private fun performLogin(email: String , password: String){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){
            task -> if(task.isSuccessful){

            val intent = Intent(this, FrontPageActivity::class.java)
            startActivity(intent)

        } else {

            Toast.makeText(
                this,task.exception!!.message.toString(),
                Toast.LENGTH_SHORT,
            ).show()

        }
        }
    }
}