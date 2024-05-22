package com.example.alfoda

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore

class Product_Information : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_information)
        val name = findViewById<TextView>(R.id.name)
        val producer_et = findViewById<TextView>(R.id.producer)
        val category_et = findViewById<TextView>(R.id.category)
        val component_et = findViewById<TextView>(R.id.component)
        val additive_et = findViewById<TextView>(R.id.additive)
        val back = findViewById<Button>(R.id.back)
        val db = Firebase.firestore
        val bundle=intent.extras
        val result_text= bundle?.getString("barcode")
        var AdditiveList: MutableList<String>? =null
        var ComponentList:MutableList<String>? =null
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")

        data class Products(
            val product_Barcode: String? = null,
            val product_Name: String? = null,
            val product_Arabic_Name: String? = null,
            val product_Lab: String? = null,
            val product_Producer: DocumentReference? = null,
            val product_Component: List<DocumentReference> = emptyList(),
            val product_Additives: List<DocumentReference> = emptyList(),
            val producer_Category: DocumentReference? = null,
        ){
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

        data class Producer(
            val producer_Adr: String? = null,
            val producer_Email: String? = null,
            val producer_Lab: String? = null,
            val producer_Phone: String? = null,
        ){
            constructor() : this(
                producer_Adr="",
                producer_Email="",
                producer_Lab="",
                producer_Phone=""
            )
        }

        data class Category(
            val arabic_Category_Lab: String? = null,
            val category_Lab: String? = null,
        ){
            constructor() : this(
            arabic_Category_Lab="",
                category_Lab=""
            )
        }
        data class Component(
            val arabic_Component_Lab:String?=null,
            val component_Lab:String?=null,
        ){
            constructor() : this(
                arabic_Component_Lab="",
                component_Lab=""
            )
        }
        fun Set_infomation(products: Products){
            products.product_Producer?.get()?.addOnCompleteListener { task->
                if(task.isSuccessful){
                    val producer=task.result.toObject(Producer::class.java)
                    producer_et.text=producer?.producer_Lab
                }
            }
            products.producer_Category?.get()?.addOnCompleteListener { task->
                if(task.isSuccessful){
                    val category=task.result.toObject(Category::class.java)
                    if(language.equals("en"))
                        category_et.text= category?.category_Lab
                    else
                        category_et.text=category?.arabic_Category_Lab
                }
            }
            if (products.product_Additives.isNotEmpty()) {
                val tasks = products.product_Additives.map { it.get() }
                Tasks.whenAllComplete(tasks).addOnCompleteListener { allTasks ->
                    if (allTasks.isSuccessful) {
                        val additiveList = mutableListOf<String>()
                        for (task in tasks) {
                            if (task.isSuccessful) {
                                additiveList.add(task.result.id)
                            }
                        }
                        additive_et.text = additiveList.joinToString(",")
                    } else {
                        // Handle errors during additive retrieval
                    }
                }
            }
            if (products.product_Component.isNotEmpty()) {
                val tasks = products.product_Component.map { it.get() }
                Tasks.whenAllComplete(tasks).addOnCompleteListener { allTasks ->
                    if (allTasks.isSuccessful) {
                        val componentList = mutableListOf<String>()
                        for (task in tasks) {
                            if (task.isSuccessful) {
                                val component = task.result.toObject(Component::class.java)
                                if(language.equals("en"))
                                    component?.component_Lab?.let { componentList.add(it) }
                                else
                                    component?.arabic_Component_Lab?.let { componentList.add(it) }
                            }
                        }
                        component_et.text = componentList.joinToString(",")
                    } else {
                        // Handle errors during component retrieval
                    }
                }
            }

            if(language.equals("en"))
                name.text=products.product_Name
             else
                name.text=products.product_Arabic_Name
        }
        val docRef = db.collection("Products").whereEqualTo("product_Barcode", result_text).get()
        docRef.addOnCompleteListener{task->
            val document = task.result?.toObjects(Products::class.java)?.firstOrNull()
            if (document!=null)
                Set_infomation(document)
        }
    }
    }


