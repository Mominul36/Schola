package com.example.schola.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schola.Adapter.User2Adapter
import com.example.schola.Adapter.UserAdapter
import com.example.schola.Class.MyClass
import com.example.schola.Model.Message
import com.example.schola.Model.User
import com.example.schola.Model.User2
import com.example.schola.R
import com.example.schola.SearchUserActivity
import com.example.schola.databinding.FragmentMessageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MessageFragment : Fragment() {
    lateinit var binding: FragmentMessageBinding

    lateinit var userAdapter: User2Adapter
    lateinit var userList: MutableList<User2>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageBinding.inflate(inflater, container, false)




        userList = mutableListOf()
        userAdapter = User2Adapter(requireContext(),userList)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = userAdapter



        fetchUser()



        binding.search.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                startActivity(Intent(requireContext(),SearchUserActivity::class.java))
            }
        }

        binding.search.setOnClickListener{
            startActivity(Intent(requireContext(),SearchUserActivity::class.java))
        }





        return binding.root

    }

    private fun fetchUser() {
        MyClass().getCurrentStudentAndSection { result ->

            if(result!=null){
                var (myId,_) = result



                // Reference to the "Message" node in Firebase
                val databaseReference = FirebaseDatabase.getInstance().getReference("Message")

                // Adding a real-time listener to the "Message" node
                databaseReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Clear the previous data to prevent duplication
                            userList.clear()

                            for (childSnapshot in snapshot.children) {
                                val key = childSnapshot.key

                                if (key!!.contains(myId!!)) {
                                    var opId = key.replace(myId, "")
                                    opId = opId.replace("-", "")

                                    // Reference to the specific "Message" node for each key
                                    val databaseReference2 = FirebaseDatabase.getInstance().getReference("Message").child(key)

                                    // Fetch the last message for each chat
                                    databaseReference2.limitToLast(1).get().addOnSuccessListener { dataSnapshot ->
                                        if (dataSnapshot.exists()) {
                                            // Get the last message
                                            val lastMessage = dataSnapshot.children.firstOrNull()?.getValue(Message::class.java)
                                            lastMessage?.let {
                                                val lastText = lastMessage.message

                                                // Fetch user details from the "User" node
                                                val databaseReference3 = FirebaseDatabase.getInstance().getReference("User")
                                                databaseReference3.child(opId).addListenerForSingleValueEvent(object : ValueEventListener {
                                                    override fun onDataChange(usersnapshot: DataSnapshot) {
                                                        val opponentUser = usersnapshot.getValue(User::class.java)
                                                        if (opponentUser != null) {
                                                            // Create a User2 object to hold the opponent's details and last message
                                                            val user2 = User2(opId, opponentUser.Name, opponentUser.pic, lastText)

                                                            // Add the new user to the list and notify the adapter
                                                            userList.add(user2)
                                                            userAdapter.notifyDataSetChanged()
                                                        }
                                                    }

                                                    override fun onCancelled(error: DatabaseError) {
                                                        // Handle possible cancellation
                                                    }
                                                })
                                            }
                                        }
                                    }.addOnFailureListener {
                                        // Handle failure to get data
                                        Log.e("MessageFragment", "Error getting last message", it)
                                    }
                                }
                            }

                            // Notify adapter after adding new data
                            userAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle database errors
                        Log.e("MessageFragment", "Error loading data", error.toException())
                    }
                })
            }



        }
    }



}