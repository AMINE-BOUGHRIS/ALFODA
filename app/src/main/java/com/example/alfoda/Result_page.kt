package com.example.alfoda
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class Result_page : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_page)
        val bundle=intent.extras
        var result_text= bundle?.getString("barcode")
        var text: EditText=findViewById<EditText>(R.id.barcode_result)
        if(result_text!=null)
            text.setText(result_text)
        else
            text.setText("Error")
    }
}