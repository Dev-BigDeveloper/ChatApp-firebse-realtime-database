package com.example.pdpchatapp.models

import java.io.Serializable

class GroupService : Serializable{

    var message:String? = null

    var date:String? = null

    var fromUid:String? = null

    var nameUser:String? = null

    constructor(message: String?, date: String?, fromUid: String?, nameUser: String?) {
        this.message = message
        this.date = date
        this.fromUid = fromUid
        this.nameUser = nameUser
    }


    constructor()
}