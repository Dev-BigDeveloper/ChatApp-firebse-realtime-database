package com.example.pdpchatapp.fragments.chatsAndGroupsService

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.pdpchatapp.R
import com.example.pdpchatapp.adapters.UserAdapterList
import com.example.pdpchatapp.cacheMemoryService.MySharedPreference
import com.example.pdpchatapp.databinding.FragmentChatListBinding
import com.example.pdpchatapp.models.AppConstants
import com.example.pdpchatapp.models.Message
import com.example.pdpchatapp.models.UserMessagesService
import com.example.pdpchatapp.models.UserService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ChatListFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding:FragmentChatListBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    private lateinit var referenceData: DatabaseReference
    lateinit var userAdapterList: UserAdapterList
    private var list = ArrayList<UserMessagesService>()
    private lateinit var mySharedPreference: MySharedPreference
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private var textS = ""
    private val referenceOnline = FirebaseDatabase.getInstance().getReference("isOnline")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatListBinding.inflate(inflater,container,false)

        mySharedPreference = MySharedPreference.getInstance(requireActivity(), "firebase")

        setFragmentListener()

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")
        referenceData = firebaseDatabase.getReference("data")

        val email = currentUser!!.email
        val displayName = currentUser.displayName
        val phoneNumber = currentUser.phoneNumber
        val photoUrl = currentUser.photoUrl
        val uid = currentUser.uid

        val user = UserService(email,displayName,phoneNumber,photoUrl.toString(),uid,false)

        var data = ""

        referenceData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot != null) {
                    data = snapshot.value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                val filterList = arrayListOf<UserMessagesService>()
                val children = snapshot.children
                for (child in children) {
                    val value = child.getValue(UserMessagesService::class.java)
                    if (value != null && uid != value.uid){
                        list.add(value)
                    }
                    if (value != null && value.uid == uid){
                        filterList.add(value)
                    }
                }
                if (filterList.isEmpty()){
                    reference.child(uid).setValue(user)
                }

                userAdapterList = UserAdapterList(list, object : UserAdapterList.OnItemClickListener {
                    override fun onItemClick(user: UserMessagesService) {
                        val bundle = Bundle()
                        user.message = textS
                        user.data = data
                        bundle.putSerializable("user",user)
                        findNavController().navigate(R.id.messageFragmentUsers,bundle)
                    }
                })

                binding.rvChat.adapter = userAdapterList
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        return binding.root
    }

    private fun setFragmentListener() {
        setFragmentResultListener(AppConstants.REQUEST_KEY){ requestKey, bundle ->
            val result = bundle.getString(AppConstants.BUNDLE_KEY)
            if (result != null) {
               // binding.textM.text = result
            }else{
              //  binding.textM.text = "Bo`sh"
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (currentUser != null) {
            referenceOnline.child(currentUser.uid).setValue(1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (currentUser != null) {
            referenceOnline.child(currentUser.uid).setValue(0)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}