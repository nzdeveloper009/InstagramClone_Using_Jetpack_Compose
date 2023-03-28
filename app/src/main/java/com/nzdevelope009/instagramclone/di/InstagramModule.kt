package com.nzdevelope009.instagramclone.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nzdevelope009.instagramclone.data.AuthenticationRepositoryImplementation
import com.nzdevelope009.instagramclone.domain.repository.AuthenticationRepository
import com.nzdevelope009.instagramclone.domain.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
* Singleton classes ko dependency injection k through inject krte hai
* apne particular class k andhr
* jis py humy us particular singleton class ka istemal krna hota hai
* For example:
*   Human class
*       boy, girl, women, men
*   4 human, then can you build 4 human nhi naw
*   The common things in human class and the instance is one of this class
*   but we copies of this human class
* */
@Module
@InstallIn(SingletonComponent::class)
object InstagramModule {

    @Singleton
    @Provides
    fun provideFirebaseAuthentication(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun providesFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Singleton
    @Provides
    fun provideAuthenticationRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthenticationRepository =
        AuthenticationRepositoryImplementation(auth = auth, firestore = firestore)

    @Singleton
    @Provides
    fun provideAuthUseCases(repository: AuthenticationRepositoryImplementation) =
        AuthenticationUseCases(
            isUserAuthenticated = IsUserAuthenticated(repository = repository),
            firebaseAuthState = FirebaseAuthState(repository = repository),
            firebaseSignOut = FirebaseSignOut(repository = repository),
            firebaseSignIn = FirebaseSignIn(repository = repository),
            firebaseSignUp = FirebaseSignUp(repository = repository)
        )
}