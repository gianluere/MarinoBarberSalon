package com.example.marinobarbersalon

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginRepository {
    fun loginUser(email: String, password: String, onResult: (String?, String?) -> Unit) {
        if (email.isNotBlank() && password.isNotBlank()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("LOGIN", "sono dentro loginUser")
                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        if (userId != null) {
                            Log.d("LOGIN", "sono dentro loginUser giusto")
                            onResult(userId, null)
                        } else {
                            Log.d("LOGIN", "sono dentro loginUser sbagliato")
                            onResult(null, "User ID not found")
                        }
                    } else {
                        onResult(null, task.exception?.message)
                    }
                }
        }else{
            onResult(null, null)
        }

    }

    fun determineUserType(onResult: (String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser?.email

        Log.d("LOGIN", "sono dentro determineUserType")

        if (user != null) {
            db.collection("utenti").document(user).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        Log.d("LOGIN", "sono dentro determineUserType cliente")
                        val userType = "cliente"
                        onResult(userType)
                    } else {
                        Log.d("LOGIN", "sono dentro determineUserType admin")
                        onResult("admin")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("LoginRepository", "Error checking user type: ${e.message}")
                    onResult(null)
                }
        }
    }
}
