package com.example.pdpchatapp.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.pdpchatapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class FirebaseService : FirebaseMessagingService() {
    val CHANEL_ID = "my_notification_channel"
    companion object{
        var sharedPreference:SharedPreferences? = null

        var token:String?
        get(){
            return sharedPreference?.getString("token","")
        }
        set(value){
            sharedPreference?.edit()!!.putString("token",value)?.apply()
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        token = p0
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChanel(notificationManager)
        }

        val notification = NotificationCompat.Builder(this,CHANEL_ID)
            .setContentTitle(p0.data["title"])
            .setContentTitle(p0.data["message"])
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId,notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChanel(notificationManager: NotificationManager) {
        val channelName = "ChanelFirebaseChat"
        val chanel = NotificationChannel(CHANEL_ID,channelName,IMPORTANCE_HIGH).apply {
            description="MY FIREBASE CHAT DESCRIPTION"
            enableLights(true)
            lightColor = Color.WHITE
        }
        notificationManager.createNotificationChannel(chanel)
    }
}