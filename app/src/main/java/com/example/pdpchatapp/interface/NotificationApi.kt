package com.example.pdpchatapp.`interface`

import com.android.volley.Response
import com.example.pdpchatapp.constains.Constants.Companion.CONTENT_TYPE
import com.example.pdpchatapp.constains.Constants.Companion.SERVICE_KEY
import com.example.pdpchatapp.notificationModels.PushNotification
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {
    @Headers("Authorization key=$SERVICE_KEY,Content_type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification:PushNotification
    ):Response<ResponseBody>
}