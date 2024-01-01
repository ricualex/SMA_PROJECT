package com.example.mobilebankingapp
import com.example.mobilebankingapp.firebase.UserDataModel
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getUserData(): Flow<UserDataModel>

    fun writeToFirebase(userDataModel: UserDataModel)

}