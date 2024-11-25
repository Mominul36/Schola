package com.example.schola

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schola.Adapter.AssignmentAdapter
import com.example.schola.Adapter.ClassTestAdapter
import com.example.schola.Adapter.CourseAdapter
import com.example.schola.Class.MyClass
import com.example.schola.Model.AssignCourse
import com.example.schola.Model.Assignment
import com.example.schola.Model.AssignmentItem
import com.example.schola.Model.ClassTest
import com.example.schola.Model.ClassTestItem
import com.example.schola.Model.Course
import com.example.schola.databinding.ActivityAssignmentBinding
import com.example.schola.databinding.ActivityClassTestListBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AssignmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssignmentBinding
    private lateinit var assignmentAdapter: AssignmentAdapter
    private var assignmentList = mutableListOf<AssignmentItem>()
    private lateinit var assRef: DatabaseReference
    private lateinit var acRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        assRef = FirebaseDatabase.getInstance().getReference("Assignment")
        acRef = FirebaseDatabase.getInstance().getReference("AssignCourse")

        assignmentList = mutableListOf()
        assignmentAdapter = AssignmentAdapter(assignmentList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = assignmentAdapter


        setAssignment()




        binding.back.setOnClickListener{
            finish()
        }



    }

    private fun setAssignment() {
        MyClass().getCurrentStudentAndSection { result ->
            if (result != null) {
                val (_, sectionId) = result

                val assignCourseList = mutableListOf<String>()

                // Fetch AssignCourse IDs for the current section
                acRef.orderByChild("section_id").equalTo(sectionId)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            assignCourseList.clear()

                            for (data in snapshot.children) {
                                val assignCourse = data.getValue(AssignCourse::class.java)
                                assignCourse?.id?.let { assignCourseList.add(it) }
                            }

                            if (assignCourseList.isNotEmpty()) {
                                fetchClassTests(assignCourseList) // change
                            } else {
                                println("No AssignCourse IDs found for this section.")
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            println("Database Error: ${error.message}")
                        }
                    })
            } else {
                println("Error: Could not retrieve current student and section.")
            }
        }
    }











    private fun fetchClassTests(assignCourseList: List<String>) {



        assRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                assignmentList.clear()

                val tempAssignmentList = mutableListOf<Assignment>()

                for (data in snapshot.children) {
                    val assignment = data.getValue(Assignment::class.java)
                    if (assignment != null && assignment.assignCourseId in assignCourseList) {
                        tempAssignmentList.add(assignment)
                    }
                }

                if (tempAssignmentList.isNotEmpty()) {
                    fetchCourseDetails(tempAssignmentList)
                } else {
                    println("No ClassTests found for the given AssignCourse IDs.")
                    assignmentAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Database Error: ${error.message}")
            }
        })
    }

    private fun fetchCourseDetails(assignments: List<Assignment>) {
        val assignCourseRef = FirebaseDatabase.getInstance().getReference("AssignCourse")

        // Create a map to store AssignCourse objects for efficient lookup
        val assignCourseMap = mutableMapOf<String, AssignCourse>()

        assignCourseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Populate the assignCourseMap with AssignCourse data
                for (data in snapshot.children) {
                    val assignCourse = data.getValue(AssignCourse::class.java)
                    assignCourse?.id?.let { assignCourseMap[it] = assignCourse }
                }

                assignmentList.clear()

                // Iterate over ClassTest and enrich details using AssignCourse
                for (assignment in assignments) {
                    val assignCourse = assignCourseMap[assignment.assignCourseId]


                    var (formattedDate, _) =  formatDateAndFindDay(assignment.date!!)

                    if (assignCourse != null) {
                        val assignmentItem = AssignmentItem(
                            courseCode = assignCourse.course_id, // Retrieve course ID from AssignCourse
                            assignmentNo = assignment.assignmentNo,
                            date = formattedDate,
                            topic = "Topic: "+ assignment.topic
                        )
                        assignmentList.add(assignmentItem)
                    }
                }

                // Notify the adapter to update the RecyclerView
                assignmentAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                println("Database Error in AssignCourse: ${error.message}")
            }
        })
    }




    fun formatDateAndFindDay(inputDate: String): Pair<String, String> {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM, dd", Locale.getDefault())
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())

        val date: Date = inputFormat.parse(inputDate) ?: return Pair("", "")
        val formattedDate = outputFormat.format(date)
        val dayOfWeek = dayFormat.format(date)

        return Pair(formattedDate, dayOfWeek)
    }






}
