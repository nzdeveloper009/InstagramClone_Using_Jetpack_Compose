package com.nzdevelope009.instagramclone.domain.use_cases

import com.nzdevelope009.instagramclone.domain.repository.AuthenticationRepository
import javax.inject.Inject

class FirebaseSignUp @Inject constructor(
    private val repository: AuthenticationRepository
) {
    operator fun invoke(email:String, password: String, userName: String) = repository.firebaseSignUp(email,password,userName)
}