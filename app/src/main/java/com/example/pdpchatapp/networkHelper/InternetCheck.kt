package com.example.pdpchatapp.networkHelper

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData

internal class InternetCheck(private val connectivityManager:ConnectivityManager) :LiveData<Boolean>() {
    constructor(application: Application) : this(
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    )
}