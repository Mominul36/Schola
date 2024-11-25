package com.example.schola.Fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.schola.Class.MyClass
import com.example.schola.Model.AssignCourse
import com.example.schola.Model.ClassRoutine
import com.example.schola.Model.Teacher
import com.example.schola.databinding.FragmentClassRoutineBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class ClassRoutineFragment : Fragment() {
    lateinit var binding: FragmentClassRoutineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClassRoutineBinding.inflate(inflater, container, false)

        setCurrentDate()

        binding.date.setOnClickListener {
            showDatePicker()
        }


        setRoutine()














        return binding.root
    }

    private fun setRoutine() {

        var date = binding.date.text.toString()

        var day = getDayOfWeek(date)

        if(day=="Saturday" || day=="Friday"){
            binding.off.visibility = View.VISIBLE
            binding.routineDetails.visibility = View.GONE
            return
        }


        binding.off.visibility = View.GONE
        binding.routineDetails.visibility = View.VISIBLE



        MyClass().getCurrentStudentAndSection { result ->
            if(result!=null){
                var (studentId,sectionId) = result




                var database = FirebaseDatabase.getInstance().getReference("ClassRoutine")



                database.child(sectionId!!).addValueEventListener(object  :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            for(routineSnapshot in snapshot.children){
                                val classRoutine = routineSnapshot.getValue(ClassRoutine::class.java)
                                if(classRoutine!=null && classRoutine.day==day){

                                    setRoutineToField(classRoutine.course1, classRoutine.room1, binding.l1, binding.courseCode1, binding.room1, binding.teacherName1, binding.teacherPic1)
                                    setRoutineToField(classRoutine.course2, classRoutine.room2, binding.l2, binding.courseCode2, binding.room2, binding.teacherName2, binding.teacherPic2)
                                    setRoutineToField(classRoutine.course3, classRoutine.room3, binding.l3, binding.courseCode3, binding.room3, binding.teacherName3, binding.teacherPic3)
                                    setRoutineToField(classRoutine.course4, classRoutine.room4, binding.l4, binding.courseCode4, binding.room4, binding.teacherName4, binding.teacherPic4)
                                    setRoutineToField(classRoutine.course5, classRoutine.room5, binding.l5, binding.courseCode5, binding.room5, binding.teacherName5, binding.teacherPic5)
                                    setRoutineToField(classRoutine.course6, classRoutine.room6, binding.l6, binding.courseCode6, binding.room6, binding.teacherName6, binding.teacherPic6)
                                    setRoutineToField(classRoutine.course7, classRoutine.room7, binding.l7, binding.courseCode7, binding.room7, binding.teacherName7, binding.teacherPic7)
                                    setRoutineToField(classRoutine.course8, classRoutine.room8, binding.l8, binding.courseCode8, binding.room8, binding.teacherName8, binding.teacherPic8)
                                    setRoutineToField(classRoutine.course9, classRoutine.room9, binding.l9, binding.courseCode9, binding.room9, binding.teacherName9, binding.teacherPic9)






                                }




                            }



                        } else {
                            Log.d("bal", "No routine available for the selected day.")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("bal", "Error fetching data: ${error.message}")
                    }

                })








//
//                database.child(sectionId!!).child(day!!).addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        if (snapshot.exists()) {
//                            for (routineSnapshot in snapshot.children) {
//                                val classRoutine = routineSnapshot.getValue(ClassRoutine::class.java) // Adjust to match your model
//                                if (classRoutine != null) {
//
//
//
//
//                                 //  setRoutineToField(classRoutine.course1, classRoutine.room1, binding.l1, binding.courseCode1, binding.room1, binding.teacherName1, binding.teacherPic1)
////                                    setRoutineToField(classRoutine.course2, classRoutine.room2, binding.l2, binding.courseCode2, binding.room2, binding.teacherName2, binding.teacherPic2)
////                                    setRoutineToField(classRoutine.course3, classRoutine.room3, binding.l3, binding.courseCode3, binding.room3, binding.teacherName3, binding.teacherPic3)
////                                    setRoutineToField(classRoutine.course4, classRoutine.room4, binding.l4, binding.courseCode4, binding.room4, binding.teacherName4, binding.teacherPic4)
////                                    setRoutineToField(classRoutine.course5, classRoutine.room5, binding.l5, binding.courseCode5, binding.room5, binding.teacherName5, binding.teacherPic5)
////                                    setRoutineToField(classRoutine.course6, classRoutine.room6, binding.l6, binding.courseCode6, binding.room6, binding.teacherName6, binding.teacherPic6)
////                                    setRoutineToField(classRoutine.course7, classRoutine.room7, binding.l7, binding.courseCode7, binding.room7, binding.teacherName7, binding.teacherPic7)
////                                    setRoutineToField(classRoutine.course8, classRoutine.room8, binding.l8, binding.courseCode8, binding.room8, binding.teacherName8, binding.teacherPic8)
////                                    setRoutineToField(classRoutine.course9, classRoutine.room9, binding.l9, binding.courseCode9, binding.room9, binding.teacherName9, binding.teacherPic9)
//
//
//
//                                }
//                            }
//
//                        } else {
//                            Log.d("bal", "No routine available for the selected day.")
//                        }
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        Log.d("bal", "Error fetching data: ${error.message}")
//                    }
//                })



            }





        }






    }

    private fun setRoutineToField(
        course1: String?,
        room1: String?,
        l1: LinearLayout,
        courseCode1: TextView,
        room11: TextView,
        teacherName1: TextView,
        teacherPic1: ImageView
    ) {

        if(course1==""){
            l1.visibility = View.GONE
        }else{
            l1.visibility = View.VISIBLE


            var teacherRef = FirebaseDatabase.getInstance().getReference("Teacher")
            var acRef = FirebaseDatabase.getInstance().getReference("AssignCourse")

            acRef.child(course1!!).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        val assignCourse = snapshot.getValue(AssignCourse::class.java)
                        assignCourse?.let {
                            val teacherId = it.teacher_id
                            // Fetch teacher details using teacherId
                            teacherRef.child(teacherId!!).addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        val teacher = snapshot.getValue(Teacher::class.java)
                                        teacher?.let {


                                            room11.text = room1
                                            courseCode1.text = assignCourse.course_id
                                            teacherName1.text = teacher.name
                                            Glide.with(requireContext()).load(teacher.profilePic!!).into(teacherPic1)
                                        }
                                    } else {
                                        Log.d("bal", "Teacher not found")
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.d("bal", "Failed to fetch teacher: ${error.message}")
                                }
                            })
                        }
                    } else {
                        // AssignCourse not found
                        Log.d("bal", "AssignCourse not found")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("bal", "Failed to fetch AssignCourse: ${error.message}")

                }
            })




        }










    }


    fun getDayOfWeek(dateString: String, format: String = "dd-MM-yyyy"): String {
        return try {
            val formatter = SimpleDateFormat(format, Locale.getDefault())
            val date = formatter.parse(dateString)
            val calendar = Calendar.getInstance()
            calendar.time = date!!

            // Get day of the week
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            // Map to the name of the day
            when (dayOfWeek) {
                Calendar.SUNDAY -> "Sunday"
                Calendar.MONDAY -> "Monday"
                Calendar.TUESDAY -> "Tuesday"
                Calendar.WEDNESDAY -> "Wednesday"
                Calendar.THURSDAY -> "Thursday"
                Calendar.FRIDAY -> "Friday"
                Calendar.SATURDAY -> "Saturday"
                else -> "Invalid Day"
            }
        } catch (e: Exception) {
            "Invalid Date Format"
        }
    }



    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        binding.date.text = formatter.format(calendar.time)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // Set the selected date in the date TextView
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                binding.date.text = formatter.format(selectedDate.time)

                // Call setRoutine() to update the routine for the selected date
                setRoutine()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

}
