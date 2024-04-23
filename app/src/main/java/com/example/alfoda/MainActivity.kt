package com.example.alfoda

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private var backPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        loadLocate()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Load the saved language on app launch

        //scan button
        val scanbtn = findViewById<ImageButton>(R.id.scanbtn)
        scanbtn.setOnClickListener {
            if (isNetworkAvailable()) {
                ScanCode()
            } else {
                Toast.makeText(this, R.string.connection, Toast.LENGTH_SHORT).show()
            }
        }
        //search button
        val searchbtn=findViewById<ImageButton>(R.id.searchbtn)
        searchbtn.setOnClickListener{
            if(isNetworkAvailable()){
            val search =Intent(this,Search_page::class.java)
            startActivity(search)
            }else{
                Toast.makeText(this, R.string.connection, Toast.LENGTH_SHORT).show()
            }
        }
        //language button
        val language =findViewById<ImageButton>(R.id.languagebtn)

        language.setOnClickListener{
            showChangeLang()
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


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }


    private fun ScanCode() {
        val options = ScanOptions().apply {
            setPrompt(getString(R.string.flach))
            setBeepEnabled(true)
            setOrientationLocked(true)
            captureActivity = CaptureAct::class.java

        // Use class reference for Kotlin
        }
        barcodeLauncher.launch(options)
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result ->
        if (result.contents != null) {
            OpenResultPage(result.contents)
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
            } else if (which == 1) {
                setLocate("en")
                recreate()
            }

            dialog.dismiss()
        }
        val mDialog = mBuilder.create()

        mDialog.show()

    }

    private fun setLocate(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", lang)
        editor.apply()
    }

    private fun loadLocate() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        Log.d("Language", "Loaded language: $language")
        if (!language.isNullOrEmpty()) {
            setLocate(language)
        }
    }



}



