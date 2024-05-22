package com.example.alfoda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.Toast

class First_Page : AppCompatActivity() {
    private var backPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_page)
        val nextbtn=findViewById<Button>(R.id.next)
        nextbtn.setOnClickListener{
            val next = Intent(this,Second_Page::class.java)
            startActivity(next)
        }

    }
    override fun onBackPressed() {
        if (backPressedOnce) {
            super.onBackPressed()
            finishAffinity()
            return
        }
        this.backPressedOnce = true
        Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ backPressedOnce = false }, 2000) // Reset flag after 2 seconds
    }
}