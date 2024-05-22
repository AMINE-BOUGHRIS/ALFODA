package com.example.alfoda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Second_Page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_page)
        val startedbtn=findViewById<Button>(R.id.started)
        startedbtn.setOnClickListener{
            val started = Intent(this,MainActivity::class.java)
            startActivity(started)
        }
    }
}