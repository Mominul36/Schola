package com.example.schola.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schola.Adapter.AnnouncementAdapter
import com.example.schola.Model.Announcement
import com.example.schola.R
import com.example.schola.databinding.FragmentAnnouncementBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AnnouncementFragment : Fragment() {
    lateinit var binding: FragmentAnnouncementBinding
    lateinit var assignCourseId: String


    val announcementList = mutableListOf<Announcement>()
    lateinit var adapter : AnnouncementAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnnouncementBinding.inflate(inflater, container, false)





        assignCourseId = arguments?.getString("assignCourseId") ?: ""




        adapter = AnnouncementAdapter(requireContext(),requireActivity().activityResultRegistry,announcementList)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter


        fetchAnnouncementData()




























        return binding.root
    }



    private fun fetchAnnouncementData() {
        var database = FirebaseDatabase.getInstance().getReference("Announcement")

        database.orderByChild("assignCourseId").equalTo(assignCourseId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        announcementList.clear()
                        for (data in snapshot.children) {
                            val announcement = data.getValue(Announcement::class.java)
                            if (announcement != null) {
                                announcementList.add(announcement)
                            }
                        }
                        announcementList.reverse()
                        adapter.notifyDataSetChanged()
                    } else {
                        Log.d("FirebaseData", "No data found for assignCourseId: $assignCourseId")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error: ${error.message}")
                }
            })


    }


}