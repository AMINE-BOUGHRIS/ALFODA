package com.example.alfoda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //scan button
        val close = findViewById<Button>(R.id.scan)
        close.setOnClickListener(View.OnClickListener {
            val scan=Intent(this,scan_page::class.java)
            startActivity(scan)
        })
        //search button
        val search=findViewById<Button>(R.id.search)
        search.setOnClickListener{
            val search =Intent(this,Search_page::class.java)
            startActivity(search)
        }




    }
}


