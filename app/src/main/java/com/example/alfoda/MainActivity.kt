package com.example.alfoda

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //scan button
        val close = findViewById<Button>(R.id.scan)
        close.setOnClickListener(View.OnClickListener {
            ScanCode()
        })
        //search button
        val search=findViewById<Button>(R.id.search)
        search.setOnClickListener{
            val search =Intent(this,Search_page::class.java)
            startActivity(search)
        }
    }
    private fun ScanCode() {
        val options = ScanOptions().apply {
            setPrompt("Volume up to flash on")
            setBeepEnabled(true)
            setOrientationLocked(true)
            setCaptureActivity(CaptureAct::class.java)

        // Use class reference for Kotlin
        }
        barcodeLauncher.launch(options)
    }

    val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result ->
        if (result.getContents() != null) {
            AlertDialog.Builder(this).apply {
                setTitle("Result")
                setMessage(result.getContents())
                setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()
                    //OpenResultPage(result.getContents())
                }
            }.show()
        }
    }

 /*   private fun OpenResultPage(r:String) {
        val page =Result_page()
        page.result_text=r.toString()
        val r_page=Intent(this,Result_page::class.java)
        startActivity(r_page)
    }
*/

}


