package com.example.schola

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schola.Adapter.LabReportAdapter
import com.example.schola.Class.MyClass
import com.example.schola.Model.AssignCourse
import com.example.schola.Model.LabReport
import com.example.schola.Model.LabReportItem
import com.example.schola.databinding.ActivityLabReportBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LabReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLabReportBinding
    private lateinit var labReportAdapter: LabReportAdapter
    private var labReportList = mutableListOf<LabReportItem>()
    private lateinit var lrRef: DatabaseReference
    private lateinit var acRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLabReportBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lrRef = FirebaseDatabase.getInstance().getReference("LabReport")
        acRef = FirebaseDatabase.getInstance().getReference("AssignCourse")

        labReportList = mutableListOf()
        labReportAdapter = LabReportAdapter(labReportList)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = labReportAdapter


        setLabReport()




        binding.back.setOnClickListener{
            finish()
        }





    }



    private fun setLabReport() {
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



        lrRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                labReportList.clear()

                val tempLabReportList = mutableListOf<LabReport>()

                for (data in snapshot.children) {
                    val labReport = data.getValue(LabReport::class.java)
                    if (labReport != null && labReport.assignCourseId in assignCourseList) {
                        tempLabReportList.add(labReport)
                    }
                }

                if (tempLabReportList.isNotEmpty()) {
                    fetchCourseDetails(tempLabReportList)
                } else {
                    println("No ClassTests found for the given AssignCourse IDs.")
                    labReportAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Database Error: ${error.message}")
            }
        })
    }

    private fun fetchCourseDetails(labReports: List<LabReport>) {
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

                labReportList.clear()

                // Iterate over ClassTest and enrich details using AssignCourse
                for (labReport in labReports) {
                    val assignCourse = assignCourseMap[labReport.assignCourseId]


                    var (formattedDate, _) =  formatDateAndFindDay(labReport.date!!)

                    if (assignCourse != null) {
                        val labReportItem = LabReportItem(
                            courseCode = assignCourse.course_id, // Retrieve course ID from AssignCourse
                            lrNo = labReport.lrNo,
                            date = formattedDate,
                            topic = "Topic: "+ labReport.topic
                        )
                        labReportList.add(labReportItem)
                    }
                }

                // Notify the adapter to update the RecyclerView
                labReportAdapter.notifyDataSetChanged()
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