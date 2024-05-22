package com.example.alfoda
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import java.io.Serializable

class Result_page : AppCompatActivity() {
    interface DangerCallback {
        fun onDangerCalculated(dangerScore: Int)
    }
    val orange: Int = Color.argb(255, 255, 165, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_page)
        val db = Firebase.firestore
        val text=findViewById<TextView>(R.id.status)
        val color: android.view.View? = findViewById(R.id.colorCircle)
        val homebtn=findViewById<Button>(R.id.home)
        val productinfo =findViewById<Button>(R.id.productinfo)
        val bundle=intent.extras
        val result_text= bundle?.getString("barcode")
        val productname=findViewById<TextView>(R.id.productname)


        data class Products(
        val product_Barcode:String?=null,
        val product_Name:String?=null,
        val product_Arabic_Name:String?=null,
        val product_Lab:String?=null,
        val product_Producer: DocumentReference?=null,
        val product_Component: List<DocumentReference> = emptyList(),
        val product_Additives: List<DocumentReference> = emptyList(),
        val producer_Category: DocumentReference?=null,
            ) {
            constructor() : this(
                product_Barcode = "",
                product_Name = "",
                product_Arabic_Name = "",
                product_Lab = "",
                product_Producer = null,
                product_Component = emptyList(),
                product_Additives = emptyList(),
                producer_Category = null
            )
        }
        data class Additive(
            val additive_Lab:String?=null,
            val additive_Use:String?=null,
            val additive_Danger: DocumentReference? =null,
            val additive_Risks: List<DocumentReference> = emptyList()
        ){
            constructor() :this(
                additive_Lab="",
                additive_Use="",
                additive_Danger=null,
                additive_Risks= emptyList()
            )
        }
        productinfo.setOnClickListener{
                val r_page=Intent(this,Product_Information::class.java)
                val bundle=Bundle()
                bundle.putString("barcode",result_text)
                r_page.putExtras(bundle)
                startActivity(r_page)
            }




        //calcule danger fonction


        fun calculateDanger(products: Products, callback: DangerCallback) {
            var danger = 0
            if (products.product_Additives.isNotEmpty()) {
                val tasks = products.product_Additives.map { it.get() }
                Tasks.whenAllComplete(tasks).addOnCompleteListener { allTasks ->
                    if (allTasks.isSuccessful) {
                        for (task in tasks) {
                            if (task.isSuccessful) {
                                val additive = task.result.toObject(Additive::class.java)
                                if (additive?.additive_Danger != null) {
                                    danger += additive.additive_Danger.id.toInt()
                                }
                            }
                        }
                        callback.onDangerCalculated(danger)
                    } else {
                        // Handle errors during data retrieval
                        Log.e("calculateDanger", "Error fetching additive data", allTasks.exception)
                    }
                }
            } else {
                callback.onDangerCalculated(danger) // No additives, danger is 0
            }
        }
        fun setproductdetail(products: Products){
            val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
            val language = sharedPreferences.getString("My_Lang", "")
            if(language.equals("en"))
                productname.text = products.product_Name
            else
                productname.text = products.product_Arabic_Name

        }


        val docRef = db.collection("Products").whereEqualTo("product_Barcode", result_text).get()
        docRef.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Firestore", "Retrieved document data: $task")
                val document = task.result?.toObjects(Products::class.java)?.firstOrNull()

                if (document != null) {
                    setproductdetail(document)
                    calculateDanger(document, object : DangerCallback {
                        override fun onDangerCalculated(dangerScore: Int) {
                            if(dangerScore==0){
                                text.setText(R.string.safe)
                                color?.setBackgroundColor(Color.GREEN)

                            }else if(dangerScore<3){
                                text.setText(R.string.low)
                                color?.setBackgroundColor(Color.YELLOW)
                            }else if (dangerScore<5){
                                text.setText(R.string.medium)
                                color?.setBackgroundColor(orange)
                            }else{
                                text.setText(R.string.high)
                                color?.setBackgroundColor(Color.RED)
                            }


                        }
                    })
                } else {
                    // Document not found, handle case (e.g., show error message)
                    text.text = "Product not found"
                }
            } else {
                // Handle potential errors during document retrieval
                Toast.makeText(this, "Error retrieving product", Toast.LENGTH_SHORT).show()
            }
        }
        homebtn.setOnClickListener{
            val page= Intent(this,MainActivity::class.java)
            startActivity(page)
        }



    }


}

