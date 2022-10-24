package com.example.tokon.util

import android.content.Context
import android.widget.Toast
import com.example.tokon.database.ShopeItem
import com.example.tokon.database.User
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class UtilFirestore {
    fun addData(context: Context, collection: String, document: String, data: Any) {
        val db = FirebaseFirestore.getInstance()
        db.collection(collection).document(document)
            .set(data)
            .addOnSuccessListener {
                Toast.makeText(
                    context, "Data Uploaded.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    context, "Failed Data Uploaded.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}