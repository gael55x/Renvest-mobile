package com.business.renvest.app

import android.app.Application
import android.util.Log
import com.business.renvest.data.repository.AuthRepository
import com.business.renvest.data.repository.AuthRepositoryImpl

class RenvestApp : Application() {

    val authRepository: AuthRepository by lazy { AuthRepositoryImpl() }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    companion object {
        private const val TAG = "RenvestApp"
    }
}
