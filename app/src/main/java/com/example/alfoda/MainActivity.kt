package com.example.alfoda

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import java.util.Locale


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
        //language button
        val language =findViewById<ImageButton>(R.id.language)

        language.setOnClickListener{
            showChangeLang()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed() // Handle back button press if needed
        finish() // Finish the current activity
        startActivity(Intent(this, MainActivity::class.java)) // Restart the activity
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

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result ->
        if (result.getContents() != null) {
            AlertDialog.Builder(this).apply {
                setTitle("Result")
                setMessage(result.getContents())
                setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()
                    OpenResultPage(result.getContents())
                }
            }.show()
        }
    }

   private fun OpenResultPage(r:String) {

        val r_page=Intent(this,Result_page::class.java)
        val bundle=Bundle()
       bundle.putString("barcode",r)
       r_page.putExtras(bundle)
        startActivity(r_page)
    }
    private fun showChangeLang() {

        val listItmes = arrayOf("عربي", "English")

        val mBuilder = AlertDialog.Builder(this@MainActivity)
        mBuilder.setTitle("Choose Language")
        mBuilder.setSingleChoiceItems(listItmes, -1) { dialog, which ->
            if (which == 0) {
                setLocate("ar")
                recreate()
            } else{
                setLocate("en")
                recreate()
            }

            dialog.dismiss()
        }
        val mDialog = mBuilder.create()

        mDialog.show()

    }

    private fun setLocate(Lang: String) {

        val locale = Locale(Lang)

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

    private fun loadLocate() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        setLocate(language.toString())
    }


}


