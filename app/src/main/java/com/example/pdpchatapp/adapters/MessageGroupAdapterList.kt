package com.example.pdpchatapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pdpchatapp.databinding.ItemMessageFromBinding
import com.example.pdpchatapp.databinding.ItemMessageToBinding
import com.example.pdpchatapp.models.GroupService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MessageGroupAdapterList(var list:List<GroupService>, var uid:String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var reference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    inner class VhFrom(var itemMessageFrom: ItemMessageFromBinding):RecyclerView.ViewHolder(itemMessageFrom.root){
        @SuppressLint("RestrictedApi")
        fun onBind(message: GroupService){
            itemMessageFrom.message.text = message.message
        }
    }

    inner class VhTo(var itemMessageToBinding: ItemMessageToBinding):RecyclerView.ViewHolder(itemMessageToBinding.root){
        @SuppressLint("RestrictedApi")
        fun onBind(message: GroupService){
            itemMessageToBinding.message.text = message.message
            itemMessageToBinding.nameUser.text = message.nameUser
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1){
            VhFrom(ItemMessageFromBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }else{
            VhTo(ItemMessageToBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1){
            val vhFrom = holder as VhFrom
            vhFrom.onBind(list[position])
        }else{
            val vhTo = holder as VhTo
            vhTo.onBind(list[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        reference = firebaseDatabase.getReference("groupsMessages")

        return if (list[position].fromUid == uid){
            1
        }else{
            2
        }
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
       return list.size
    }
}