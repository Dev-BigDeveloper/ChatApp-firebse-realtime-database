package com.example.pdpchatapp.models

import java.io.Serializable

class UserService:Serializable{
    var email: String? = null
    var displayName: String? = null
    var phoneNumber: String? = null
    var photoUrl: String? = null
    var uid: String? = null
    var notificationUs:Boolean? = null

    constructor(
        email:String?,
        displayName: String?,
        phoneNumber: String?,
        photoUrl: String?,
        uid: String?,
        notificationUs:Boolean?
    ){
        this.email = email
        this.displayName = displayName
        this.phoneNumber = phoneNumber
        this.photoUrl = photoUrl
        this.uid = uid
        this.notificationUs = notificationUs
    }
    constructor()
 }