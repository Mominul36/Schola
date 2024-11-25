package com.example.schola


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schola.Adapter.CourseAdapter
import com.example.schola.Class.MyClass
import com.example.schola.Model.AssignCourse
import com.example.schola.Model.Course
import com.example.schola.Model.CourseItem
import com.example.schola.databinding.ActivitySubjectBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SubjectActivity : AppCompatActivity() {

    lateinit var binding: ActivitySubjectBinding
    lateinit var adapter: CourseAdapter
    lateinit var courseList: MutableList<CourseItem>
    lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("AssignCourse")
        courseList = mutableListOf()
        adapter = CourseAdapter(this, courseList)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Show progress bar and hide RecyclerView initially
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE

        val myClass = MyClass()
        myClass.getCurrentStudentAndSection { result ->
            if (result != null) {
                val (studentId, sectionId) = result

                database.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (courseSnapshot in snapshot.children) {
                                val ac = courseSnapshot.getValue(AssignCourse::class.java)
                                if (ac != null && ac.section_id == sectionId) {
                                    val courseCode = ac.course_id
                                    val database2 =
                                        FirebaseDatabase.getInstance().getReference("Course")

                                    database2.addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists()) {
                                                for (courseSnapshot in snapshot.children) {
                                                    val course =
                                                        courseSnapshot.getValue(Course::class.java)
                                                    if (course != null && course.courseCode == courseCode) {
                                                        courseList.add(
                                                            CourseItem(
                                                                course.courseCode!!,
                                                                course.courseName!!,
                                                                course.icon!!,
                                                                ac.id!!
                                                            )
                                                        )
                                                    }
                                                }
                                                // Hide progress bar and show RecyclerView after data is loaded
                                                binding.progressBar.visibility = View.GONE
                                                binding.recyclerView.visibility = View.VISIBLE
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {}
                                    })
                                }
                            }
                            adapter.notifyDataSetChanged()
                        } else {
                            // Hide progress bar even if no data is found
                            binding.progressBar.visibility = View.GONE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            } else {
                println("Student not found or user not logged in.")
                // Hide progress bar if there's an error
                binding.progressBar.visibility = View.GONE
            }
        }

        binding.back.setOnClickListener {
            finish()
        }
    }
}
