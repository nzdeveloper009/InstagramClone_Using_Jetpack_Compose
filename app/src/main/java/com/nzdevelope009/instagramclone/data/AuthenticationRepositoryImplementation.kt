package com.nzdevelope009.instagramclone.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nzdevelope009.instagramclone.domain.model.User
import com.nzdevelope009.instagramclone.domain.repository.AuthenticationRepository
import com.nzdevelope009.instagramclone.util.Constants
import com.nzdevelope009.instagramclone.util.Response
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImplementation @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthenticationRepository {
    var operationSuccessful: Boolean = false
    override fun isUserAuthenticatedInFirebase(): Boolean = auth.currentUser != null

    @ExperimentalCoroutinesApi
    override fun getFirebaseAuthState(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener {
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override fun firebaseSignIn(email: String, password: String): Flow<Response<Boolean>> = flow {
        operationSuccessful = false
        try {
            emit(Response.Loading)
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                operationSuccessful = true
            }.await()
            emit(Response.Success(operationSuccessful))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "An Unexpected Error"))
        }
    }

    override fun firebaseSignOut(): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            auth.signOut()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "An Unexpected Error"))
        }
    }

    override fun firebaseSignUp(
        email: String,
        password: String,
        userName: String
    ): Flow<Response<Boolean>> = flow {
        operationSuccessful = false
        try {
            emit(Response.Loading)
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                operationSuccessful = true
            }
            if (operationSuccessful) {
                val userid = auth.currentUser?.uid!!
                val obj =
                    User(username = userName, userid = userid, password = password, email = email)
                firestore.collection(Constants.COLLECTION_NAME_USERS).document(userid).set(obj)
                    .addOnSuccessListener {

                    }.await()
                emit(Response.Success(operationSuccessful))
            } else {
                Response.Success(operationSuccessful)
            }
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "An Unexpected Error"))
        }
    }
}