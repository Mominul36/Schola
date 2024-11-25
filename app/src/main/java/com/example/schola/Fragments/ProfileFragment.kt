package com.example.schola.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.schola.Class.MyClass
import com.example.schola.databinding.FragmentProfileBinding
import com.google.firebase.database.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        database = FirebaseDatabase.getInstance().getReference("Student")

        fetchStudentData()

        return binding.root
    }

    private fun fetchStudentData() {


        MyClass().getCurrentStudentAndSection { result->

            if(result!=null){

                val(studentId,_) = result

                database.child(studentId!!).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val name = snapshot.child("name").getValue(String::class.java)
                            val id = snapshot.child("id").getValue(String::class.java)
                            val department = snapshot.child("dept").getValue(String::class.java)
                            val batch = snapshot.child("batch").getValue(String::class.java)
                            val section = snapshot.child("section").getValue(String::class.java)
                            val email = snapshot.child("email").getValue(String::class.java)
                            val phone = snapshot.child("phone").getValue(String::class.java)
                            val level = "3"
                            val term = "1"

                            binding.name.text = name
                            binding.id.text = id
                            binding.department.text = department
                            binding.section.text = section
                            binding.batch.text = batch
                            binding.email.text = email
                            binding.phone.text = phone
                            binding.level.text = level
                            binding.term.text = term
                        } else {
                            Toast.makeText(context, "Student data not found", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })




            }


        }






    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
