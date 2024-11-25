package com.example.schola

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schola.Class.MyClass
import com.example.schola.Model.AssignCourse
import com.example.schola.Model.TheoryAttendance
import com.example.schola.databinding.ActivityAttendenceBinding
import com.google.firebase.database.*

class AttendenceActivity : AppCompatActivity() {
    lateinit var binding: ActivityAttendenceBinding
    private lateinit var database: DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("AssignCourse")

        setSpinnerValue()

        binding.course.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCourseId = parent.getItemAtPosition(position).toString()
                findAssignCourseId(selectedCourseId)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(this@AttendenceActivity, "No course selected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun setSpinnerValue() {
        MyClass().getCurrentStudentAndSection { result ->
            if (result != null) {
                val (_, sectionId) = result

                database.orderByChild("section_id").equalTo(sectionId)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val courseIds = ArrayList<String>()
                            for (data in snapshot.children) {
                                val course = data.getValue(AssignCourse::class.java)
                                course?.course_id?.let { courseIds.add(it) }
                            }

                            if (courseIds.isNotEmpty()) {
                                val adapter = ArrayAdapter(
                                    this@AttendenceActivity,
                                    android.R.layout.simple_spinner_item,
                                    courseIds
                                )
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                binding.course.adapter = adapter
                            } else {
                                Toast.makeText(this@AttendenceActivity, "No courses found", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@AttendenceActivity, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }

    private fun findAssignCourseId(courseId: String) {
        database.orderByChild("course_id").equalTo(courseId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (data in snapshot.children) {
                            val assignCourse = data.getValue(AssignCourse::class.java)
                            val assignCourseId = assignCourse?.id


                            MyClass().getCurrentStudentAndSection { result->

                                if(result!=null){

                                    val (studentId, sectionId) = result


                                    findTheoryAttendance(assignCourseId!!, studentId!!)


                                }

                            }


                        }
                    } else {
                        Toast.makeText(this@AttendenceActivity, "No match found for courseId: $courseId", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AttendenceActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }



    private fun findTheoryAttendance(assignCourseId: String, studentId: String) {
        val database = FirebaseDatabase.getInstance().getReference("TheoryAttendance")

        // Query by assignCourseId
        database.orderByChild("assignCourseId").equalTo(assignCourseId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Filter by studentId
                        for (data in snapshot.children) {
                            val theoryAttendance = data.getValue(TheoryAttendance::class.java)
                            if (theoryAttendance?.studentId == studentId) {


                                //Modify here
                                updateTextViews(theoryAttendance)





                            }
                        }
                    } else {
                        Toast.makeText(this@AttendenceActivity, "No attendance found for course", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@AttendenceActivity,
                        "Error: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }





    private fun updateTextViews(theoryAttendance: TheoryAttendance) {



        val textViewList = mutableListOf<TextView>(
            binding.a1, binding.a2, binding.a3, binding.a4, binding.a5, binding.a6,
            binding.a7, binding.a8, binding.a9, binding.a10, binding.a11, binding.a12,
            binding.a13, binding.a14, binding.a15, binding.a16, binding.a17, binding.a18,
            binding.a19, binding.a20, binding.a21, binding.a22, binding.a23, binding.a24,
            binding.a25, binding.a26, binding.a27, binding.a28, binding.a29, binding.a30,
            binding.a31, binding.a32, binding.a33, binding.a34, binding.a35, binding.a36,
            binding.a37, binding.a38, binding.a39, binding.a40, binding.a41, binding.a42
        )



        val attendanceList = listOf(
            theoryAttendance.class1, theoryAttendance.class2, theoryAttendance.class3,
            theoryAttendance.class4, theoryAttendance.class5, theoryAttendance.class6,
            theoryAttendance.class7, theoryAttendance.class8, theoryAttendance.class9,
            theoryAttendance.class10, theoryAttendance.class11, theoryAttendance.class12,
            theoryAttendance.class13, theoryAttendance.class14, theoryAttendance.class15,
            theoryAttendance.class16, theoryAttendance.class17, theoryAttendance.class18,
            theoryAttendance.class19, theoryAttendance.class20, theoryAttendance.class21,
            theoryAttendance.class22, theoryAttendance.class23, theoryAttendance.class24,
            theoryAttendance.class25, theoryAttendance.class26, theoryAttendance.class27,
            theoryAttendance.class28, theoryAttendance.class29, theoryAttendance.class30,
            theoryAttendance.class31, theoryAttendance.class32, theoryAttendance.class33,
            theoryAttendance.class34, theoryAttendance.class35, theoryAttendance.class36,
            theoryAttendance.class37, theoryAttendance.class38, theoryAttendance.class39,
            theoryAttendance.class40, theoryAttendance.class41, theoryAttendance.class42
        )

        // Iterate through each class attendance and update the corresponding TextView
        attendanceList.forEachIndexed { index, attendance ->
            val textView = textViewList.getOrNull(index)

            textView?.let {
                when (attendance) {
                    "0" -> {
                        it.setTextColor(getColor(android.R.color.black))
                        it.setBackgroundColor(getColor(android.R.color.white))
                    }
                    "1" -> {
                        it.setTextColor(getColor(android.R.color.white))
                        it.setBackgroundColor(getColor(R.color.green))

                    }
                    "2" -> {
                        it.setTextColor(getColor(android.R.color.black))
                        it.setBackgroundColor(getColor(R.color.yellow))

                    }
                    "3" -> {
                        it.setTextColor(getColor(android.R.color.black))
                        it.setBackgroundColor(getColor(R.color.red))

                    }
                    else -> {
                        it.setTextColor(getColor(android.R.color.black))
                        it.setBackgroundColor(getColor(android.R.color.white))

                    }
                }
            }
        }
    }








}
