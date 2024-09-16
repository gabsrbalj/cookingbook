package com.example.myrecipeapp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Surface
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.myrecipeapp.databinding.ActivityMainBinding
import com.google.android.material.theme.overlay.MaterialThemeOverlay
import java.lang.reflect.Modifier

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val textView: TextView = findViewById(R.id.textView2)
        //textView.setTextColor(ContextCompat.getColor(this, R.color.green))
       // textView.textSize = 35f


        binding.btnForSignUp.setOnClickListener {
            val intent = Intent(this@MainActivity, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnForLogin.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}