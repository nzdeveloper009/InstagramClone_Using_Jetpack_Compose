package com.nzdevelope009.instagramclone.domain.use_cases

import com.nzdevelope009.instagramclone.domain.repository.AuthenticationRepository
import javax.inject.Inject

class FirebaseAuthState @Inject constructor(
    private val repository: AuthenticationRepository
) {
    operator fun invoke() = repository.getFirebaseAuthState()
}