package com.bangkit.dermascan.data

import com.google.firebase.auth.FirebaseAuth
import android.util.Log
private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

fun signIn(callback: (String?) -> Unit) {
    val email = "irun@gmail.com"
    val pass = "Irun1234"

    firebaseAuth.signInWithEmailAndPassword(email, pass)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseAuth.currentUser?.getIdToken(true)
                    ?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val idToken = tokenTask.result?.token
                            callback(idToken) // Kembalikan idToken melalui callback
                        } else {
                            Log.e("AccesToken", "Error getting ID token: ${tokenTask.exception?.message}")
                            callback(null)
                        }
                    }
            } else {
                Log.e("AccesToken", "Sign in failed: ${task.exception?.message}")
                callback(null)
            }
        }
}
//class AccesToken {
//
//}
