package com.example.pdpchatapp.fragments.chatsAndGroupsService

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pdpchatapp.R
import com.example.pdpchatapp.adapters.GroupsAdapterList
import com.example.pdpchatapp.databinding.FragmentGroupsListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GroupsListFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentGroupsListBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var groupsAdapterList: GroupsAdapterList

    private var list = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        list.clear()
        binding = FragmentGroupsListBinding.inflate(inflater, container, false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("groups")
        firebaseAuth = FirebaseAuth.getInstance()

        binding.addButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val dialogLayout = inflater.inflate(R.layout.edit_dialog, container, false)
            val editBinding = dialogLayout.findViewById<EditText>(R.id.editText)
            with(builder) {
                setTitle("Gruppani nomini kiritng")
                setPositiveButton("Ok") { dialog, which ->
                    val name = editBinding.text.toString()
                    list.add(name)
                    if (name != "") {
                        val key = reference.push().key
                        reference.child("$key")
                            .setValue(name)
                    }
                }
                setView(dialogLayout)
                show()
            }
        }

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val filterList = arrayListOf<String>()
                val children = snapshot.children
                for ((i, child) in children.withIndex()) {
                    if (child.value != null) {
                        list.add(child.value.toString())
                    }
                }
                binding.groupListRv.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                groupsAdapterList =
                    GroupsAdapterList(list, object : GroupsAdapterList.OnItemClickListener {
                        override fun onItemClick(name: String) {
                            val bundle = Bundle()
                            bundle.putString("name", name)
                            findNavController().navigate(R.id.groupMessagesFragment, bundle)
                        }
                    })
                binding.groupListRv.adapter = groupsAdapterList
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GroupsListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}