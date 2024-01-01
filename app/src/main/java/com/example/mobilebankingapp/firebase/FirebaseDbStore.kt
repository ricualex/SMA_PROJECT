package com.example.mobilebankingapp.firebase

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.mobilebankingapp.AppRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow

class FirebaseDbStore (userId: String?) : AppRepository {

    private val database = FirebaseDatabase.getInstance().reference.child("users").child(userId!!)

    override fun getUserData(): Flow<UserDataModel> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("FirebaseDbStore", "getUserData:", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val nodeState = mutableStateOf(UserDataModel())

                if (p0.key != null && p0.value != null) {
                    val nodeValue = p0.getValue(UserDataModel::class.java)
                    if (nodeValue != null) {
                        nodeState.value = nodeValue
                    }
                }
                trySend(nodeState.value)
            }
        }
        database.addValueEventListener(listener)
        awaitClose { database.removeEventListener(listener) }
    }

    override fun writeToFirebase(firebaseUser: UserDataModel) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val databaseRef: DatabaseReference = database.getReference("users")
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        databaseRef.child(userId!!).setValue(firebaseUser)
    }
}