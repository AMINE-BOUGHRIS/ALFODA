package com.example.alfoda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class Result_page : AppCompatActivity() {
    var result_text: String? =null
        get() = field
        set(value) {
            field=value
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_page)
        val text=findViewById<EditText>(R.id.barcode_result)
        if(result_text!=null)
        text.setText(result_text)
        else
            text.setText("Error")
    }
}