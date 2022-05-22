package com.example.pdpchatapp.fragments.groupsFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pdpchatapp.adapters.MessageGroupAdapterList
import com.example.pdpchatapp.databinding.FragmentGroupMessagesBinding
import com.example.pdpchatapp.models.GroupService
import com.example.pdpchatapp.models.UserService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GroupMessagesFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentGroupMessagesBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var referenceUser: DatabaseReference
    private lateinit var referenceUid: DatabaseReference
    private lateinit var messageGroupsAdapterList: MessageGroupAdapterList
    private  val TAG = "GroupMessagesFragment"

    private var list = ArrayList<UserService>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGroupMessagesBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("groupsMessages")
        referenceUser = firebaseDatabase.getReference("users")
        referenceUid = firebaseDatabase.getReference("usersUidM")

        val nameGroups = arguments?.getString("name")

        referenceUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                for (child in children) {
                    if (child != null) {
                        val value = child.getValue(UserService::class.java)
                        if (value != null) {
                            list.add(value)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        binding.send.setOnClickListener {
            if (binding.editMessage.text == null) {
                Toast.makeText(requireContext(), "No messages!", Toast.LENGTH_SHORT).show()
            }else{
                val mes = binding.editMessage.text.toString()
                val simpleDataFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
                val date = simpleDataFormat.format(Date())
                val message =
                    GroupService(
                        mes,
                        date,
                        firebaseAuth.currentUser!!.uid,
                        firebaseAuth.currentUser!!.displayName
                    )
                val key = reference.push().key
                if (mes != "") {
                    reference.child("$nameGroups/$key")
                        .setValue(message)
                }
                binding.editMessage.text.clear()
            }
        }

        reference.child("$nameGroups")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list1 = ArrayList<GroupService>()
                    val children = snapshot.children
                    for (child in children) {
                        val value = child.getValue(GroupService::class.java)
                        if (value != null) {
                            list1.add(value)
                        }
                    }

                    messageGroupsAdapterList =
                        MessageGroupAdapterList(list1, firebaseAuth.currentUser!!.uid)
                    Log.d(TAG, "onDataChange: " +
                            "${firebaseAuth.currentUser!!.uid} \n ${firebaseAuth.currentUser!!.displayName}")
                    binding.rvMessage.adapter = messageGroupsAdapterList
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


        binding.name.text = arguments?.getString("name")

        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    fun adapter() {

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GroupMessagesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}