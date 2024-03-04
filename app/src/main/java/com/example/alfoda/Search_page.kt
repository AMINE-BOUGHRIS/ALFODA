package com.example.alfoda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Search_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_page)

        val back =findViewById<Button>(R.id.back)
        back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        val find =findViewById<Button>(R.id.search1)
        val search_edittext =findViewById<EditText>(R.id.search_text)

        find.setOnClickListener(View.OnClickListener {
            val search_text =search_edittext.text.toString()
            if (search_text == "5"){
                Toast.makeText(applicationContext, "correct answer", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "We didn't add that product yet.", Toast.LENGTH_SHORT).show()

            }
        })
    }
}