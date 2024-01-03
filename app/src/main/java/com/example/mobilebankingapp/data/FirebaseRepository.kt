package com.example.mobilebankingapp.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.mobilebankingapp.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface FirebaseRepository {
    fun getUserData(): Flow<UserData>

    fun writeToFirebase(userData: UserData)

}

class NetworkFirebaseRepository(userId: String) : FirebaseRepository {

    private val database = FirebaseDatabase.getInstance().reference.child("users").child(userId)

    override fun getUserData(): Flow<UserData> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("FirebaseDbStore", "getUserData:", p0.toException())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val nodeState = mutableStateOf(UserData())

                if (dataSnapshot.key != null && dataSnapshot.value != null) {
                    val nodeValue = dataSnapshot.getValue(UserData::class.java)
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

    override fun writeToFirebase(firebaseUser: UserData) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val databaseRef: DatabaseReference = database.getReference("users")
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        databaseRef.child(userId!!).setValue(firebaseUser)
    }
}
