package com.example.pdpchatapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pdpchatapp.databinding.ItemUserBinding
import com.example.pdpchatapp.models.Message
import com.example.pdpchatapp.models.UserMessagesService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class UserAdapterList(private var list:List<UserMessagesService>, var onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<UserAdapterList.Vh>(){
    inner class Vh(private var itemUserBinding: ItemUserBinding):RecyclerView.ViewHolder(itemUserBinding.root){

        var firebaseDatabase = FirebaseDatabase.getInstance()
        var reference = FirebaseDatabase.getInstance().getReference("users")
        lateinit var referenceInfoMessage: DatabaseReference
        var firebaseAuth = FirebaseAuth.getInstance()
        val referenceOnline = firebaseDatabase.getReference("isOnline")
        var listMessages = ArrayList<Message>()

        fun onBind(user:UserMessagesService){
            itemUserBinding.nameUser.text = user.displayName
            Picasso.get().load(user.photoUrl).into(itemUserBinding.imageUser)
            itemUserBinding.texInMessage.text = user.message
            itemUserBinding.textTime.text = user.data
            itemView.setOnClickListener{
                onItemClickListener.onItemClick(user)
            }

            referenceInfoMessage = firebaseDatabase.getReference("messageInfo")


            referenceOnline.child(user.uid!!).addValueEventListener(object: ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val n = snapshot.getValue(Int::class.java)
                    if (n==1){
                        itemUserBinding.onlineAndOffIdi.visibility = View.VISIBLE
                    }else{
                        itemUserBinding.onlineAndOffIdi.visibility = View.INVISIBLE
                    }
                }
            })


            reference.child("${user.uid}/messages/${firebaseAuth.currentUser!!.uid}").addValueEventListener(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {

                }

                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot.children
                    listMessages.clear()
                    var count = 0
                    for (child in children) {
                        if (child!=null) {
                            listMessages.add(child.getValue(Message::class.java)!!)
                        }
                        count++
                    }
                    if (listMessages.isNotEmpty()) {
                        itemUserBinding.texInMessage.text = listMessages[listMessages.size - 1].message
                        val message = itemUserBinding.texInMessage.text.toString()
                        if (message.isNotEmpty()){
                            itemUserBinding.textTime.visibility = View.VISIBLE
                            val minut = listMessages[count-1].date!!.substring(listMessages[count-1].date!!.length-2)
                            val time =  listMessages[count-1].date!!.substring(listMessages[count-1].date!!.length-5,listMessages[count-1].date!!.length-3)
                            itemUserBinding.textTime.text ="$time:$minut"
                        }
                    }
                }

            })


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemUserBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener{
        fun onItemClick(user: UserMessagesService)
    }
}