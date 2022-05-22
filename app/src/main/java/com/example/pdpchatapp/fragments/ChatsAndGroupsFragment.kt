package com.example.pdpchatapp.fragments

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.viewpager.widget.ViewPager
import com.example.pdpchatapp.R
import com.example.pdpchatapp.adapters.ViewPagerAdapter
import com.example.pdpchatapp.databinding.FragmentChatsAndGroupsBinding
import com.example.pdpchatapp.firebase.FirebaseService
import com.example.pdpchatapp.fragments.chatsAndGroupsService.ChatListFragment
import com.example.pdpchatapp.fragments.chatsAndGroupsService.GroupsListFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ChatsAndGroupsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentChatsAndGroupsBinding
    lateinit var root: View
    private lateinit var referenceNotification: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private val referenceOnline = FirebaseDatabase.getInstance().getReference("isOnline")
    private lateinit var firebaseAuth: FirebaseAuth
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private var CHANNEL_ID = "1"
    private val notificationID = 1
    private val TAG = "ChatsAndGroupsFragment"
    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
            Log.d(TAG, "onCreateView: run")
        firebaseAuth = FirebaseAuth.getInstance()
        binding = FragmentChatsAndGroupsBinding.inflate(inflater, container, false)
        root = binding.root
        FirebaseService.sharedPreference = requireContext().getSharedPreferences("sharedPref",Context.MODE_PRIVATE)
        firebaseDatabase = FirebaseDatabase.getInstance()
        referenceNotification = firebaseDatabase.getReference("notification")
        binding.layoutRight.setBackgroundResource(R.drawable.chat_custom_view_off)
        binding.layoutLeft.setBackgroundResource(R.drawable.chat_custom_view)

        referenceNotification.child(currentUser!!.uid).addValueEventListener(object:
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val n = snapshot.getValue(Int::class.java)
                if (n==1){
                    Log.d(TAG, "onDataChange: notifications run ${currentUser.uid}")
                    notificationChats()
                }
            }
        })

        val fragmentAdapter = ViewPagerAdapter(childFragmentManager)
        fragmentAdapter.addFragment(ChatListFragment(), "Chats")
        fragmentAdapter.addFragment(GroupsListFragment(), "Groups")
        binding.viewPager.adapter = fragmentAdapter

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    binding.layoutRight.setBackgroundResource(R.drawable.chat_custom_view_off)
                    binding.layoutLeft.setBackgroundResource(R.drawable.chat_custom_view)
                } else {
                    binding.layoutLeft.setBackgroundResource(R.drawable.chat_custom_view_off)
                    binding.layoutRight.setBackgroundResource(R.drawable.chat_custom_view)
                }

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        binding.layoutLeft.setOnClickListener {
            binding.viewPager.currentItem = 0
        }

        binding.layoutRight.setOnClickListener {
            binding.viewPager.currentItem = 1
        }


        return root
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Start")
        if (currentUser != null) {
            referenceOnline.child(currentUser.uid).setValue(1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Dest")
        referenceNotification.child(currentUser!!.uid).setValue(0)
        if (currentUser != null) {
            referenceOnline.child(currentUser.uid).setValue(0)
        }
    }

    override fun onPause() {
        super.onPause()
        referenceNotification.child(currentUser!!.uid).setValue(0)
        Log.d(TAG, "onPause: Pause")
    }

    private fun notificationChats() {
        Log.d(TAG, "notificationChats: Run")
        val builder = NotificationCompat.Builder(root.context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Content text")
            .setContentText("Content text")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Chat list message")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager: NotificationManager =
            root.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()
        notificationManager.notify(notificationID, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = root.context.getString(R.string.app_name)
            val descriptionText = root.context.getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                root.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatsAndGroupsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}