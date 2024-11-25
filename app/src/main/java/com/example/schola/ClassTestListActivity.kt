package com.example.schola

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schola.Adapter.ClassTestAdapter
import com.example.schola.Adapter.CourseAdapter
import com.example.schola.Class.MyClass
import com.example.schola.Model.AssignCourse
import com.example.schola.Model.ClassTest
import com.example.schola.Model.ClassTestItem
import com.example.schola.Model.Course
import com.example.schola.databinding.ActivityClassTestListBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClassTestListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassTestListBinding
    private lateinit var classTestAdapter: ClassTestAdapter
    private var classTestList = mutableListOf<ClassTestItem>()
    private lateinit var ctRef: DatabaseReference
    private lateinit var acRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassTestListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ctRef = FirebaseDatabase.getInstance().getReference("ClassTest")
        acRef = FirebaseDatabase.getInstance().getReference("AssignCourse")

        classTestList = mutableListOf()
        classTestAdapter = ClassTestAdapter(classTestList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = classTestAdapter


        setClassTest()




       binding.back.setOnClickListener{
           finish()
       }



    }

    private fun setClassTest() {
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



        ctRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                classTestList.clear()

                val tempClassTestList = mutableListOf<ClassTest>() // Temporary list to hold ClassTest data

                for (data in snapshot.children) {
                    val classTest = data.getValue(ClassTest::class.java)
                    if (classTest != null && classTest.assignCourseId in assignCourseList) {
                        tempClassTestList.add(classTest)
                    }
                }

                if (tempClassTestList.isNotEmpty()) {
                    fetchCourseDetails(tempClassTestList)
                } else {
                    println("No ClassTests found for the given AssignCourse IDs.")
                    classTestAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Database Error: ${error.message}")
            }
        })
    }

    private fun fetchCourseDetails(classTests: List<ClassTest>) {
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

                classTestList.clear()

                // Iterate over ClassTest and enrich details using AssignCourse
                for (classTest in classTests) {
                    val assignCourse = assignCourseMap[classTest.assignCourseId]


                   var (formattedDate, _) =  formatDateAndFindDay(classTest.date!!)

                    if (assignCourse != null) {
                        val classTestItem = ClassTestItem(
                            courseCode = assignCourse.course_id, // Retrieve course ID from AssignCourse
                            ctNo = classTest.ctNo,
                            date = formattedDate,
                            time = "Time: "+classTest.time,
                            topic = "Topic: "+ classTest.topic
                        )
                        classTestList.add(classTestItem)
                    }
                }

                // Notify the adapter to update the RecyclerView
                classTestAdapter.notifyDataSetChanged()
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
