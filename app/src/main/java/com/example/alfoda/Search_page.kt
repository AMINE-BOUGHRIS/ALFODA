package com.example.alfoda

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

class Search_page : AppCompatActivity() {
    private var backPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_page)

        val back =findViewById<Button>(R.id.home)
        back.setOnClickListener {
            val page= Intent(this,MainActivity::class.java)
            startActivity(page)
        }
        val find =findViewById<Button>(R.id.search)
        val search_edittext =findViewById<EditText>(R.id.search_text)

        find.setOnClickListener {
            val search_text = search_edittext.text.toString()
                if (search_text == ""){
                    Toast.makeText(this,R.string.enter,Toast.LENGTH_SHORT).show()
                }else{
                val r_page= Intent(this,Result_page::class.java)
                val bundle=Bundle()
                bundle.putString("barcode",search_text)
                r_page.putExtras(bundle)
                startActivity(r_page)
        }
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