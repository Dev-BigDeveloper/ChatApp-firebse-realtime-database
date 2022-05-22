package com.example.pdpchatapp.models

import java.io.Serializable

class UserMessagesService : Serializable {
    var email: String? = null
    var displayName: String? = null
    var phoneNumber: String? = null
    var photoUrl: String? = null
    var uid: String? = null
    var data: String? = null
    var message: String? = null

    constructor(
        email: String?,
        displayName: String?,
        phoneNumber: String?,
        photoUrl: String?,
        uid: String?,
        data: String?,
    message:String?
    ) {
        this.email = email
        this.displayName = displayName
        this.phoneNumber = phoneNumber
        this.photoUrl = photoUrl
        this.uid = uid
        this.data = data
        this.message = message
     }

    constructor()
}