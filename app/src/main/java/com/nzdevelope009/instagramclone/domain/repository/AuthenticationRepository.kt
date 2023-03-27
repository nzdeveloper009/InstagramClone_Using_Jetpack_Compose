package com.nzdevelope009.instagramclone.domain.repository

import com.nzdevelope009.instagramclone.util.Response
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    fun isUserAuthenticatedInFirebase(): Boolean
    fun getFirebaseAuthState(): Flow<Boolean>
    fun firebaseSignIn(email:String, password:String): Flow<Response<Boolean>> // Response Sealed class hogi
    fun firebaseSignOut(): Flow<Response<Boolean>>
    fun firebaseSignUp(email: String, password: String, userName: String): Flow<Response<Boolean>>

}