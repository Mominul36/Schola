
package com.example.schola

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schola.Model.Dept
import com.example.schola.Model.Section
import com.example.schola.Model.Student
import com.example.schola.databinding.ActivitySignUpBinding
import com.google.firebase.database.*
import kotlin.random.Random

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var database: DatabaseReference
    private val departmentList = mutableListOf<Dept>()
    private val departmentNames = mutableListOf<String>()
    lateinit var selectedDepartment: Dept

    private var bal: String = ""  // This will hold the Section ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("Department")

        loadDepartments()
        setupBatchSpinner()
        setupSectionSpinner()

        binding.signupbtn.setOnClickListener {
            signUpStudent()
        }
    }

    private fun loadDepartments() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                departmentList.clear()
                departmentNames.clear()

                for (data in snapshot.children) {
                    val department = data.getValue(Dept::class.java)
                    if (department != null) {
                        departmentList.add(department)
                        department.dept_short_name?.let {
                            departmentNames.add(it)
                        }
                    }
                }

                // Populate the Spinner with department names
                val adapter = ArrayAdapter(
                    this@SignUpActivity,
                    android.R.layout.simple_spinner_item,
                    departmentNames
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.editdept.adapter = adapter

                // Handle item selection
                binding.editdept.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        // Get the selected department
                        selectedDepartment = departmentList[position]

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Optional: Handle no selection state if needed
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
                Toast.makeText(
                    this@SignUpActivity,
                    "Failed to load departments: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setupBatchSpinner() {
        val batches = (12..21).map { it.toString() }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, batches)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editbatch.adapter = adapter
    }

    private fun setupSectionSpinner() {
        val sections = listOf("A", "B", "C")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sections)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editsection.adapter = adapter
    }

    private fun signUpStudent() {
        val name = binding.editname.text.toString()
        val id = binding.editid.text.toString()
        val email = binding.editemail.text.toString()
        val phone = binding.editphone.text.toString()
        val deptCode = selectedDepartment.dept_code
        val batch = binding.editbatch.selectedItem?.toString()
        val section = binding.editsection.selectedItem?.toString()

        if (name.isEmpty() || id.isEmpty() || email.isEmpty() || phone.isEmpty() || batch == null || section == null) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        fetchSectionsByDepartment(deptCode!!) { sections ->
            if (sections.isEmpty()) {
                Toast.makeText(this, "No sections found for the ${selectedDepartment.dept_short_name} department", Toast.LENGTH_SHORT).show()
                return@fetchSectionsByDepartment
            }

            var flag:Boolean = true

            for (s in sections) {
                if(s.batch==batch && s.section==section){
                    flag = false

                    val verificationCode = Random.nextInt(1000, 9999).toString()
                    val student = Student(
                        id = id,
                        name = name,
                        email = email,
                        phone = phone,
                        dept = deptCode,
                        batch = batch,
                        section = section,
                        password = "",
                        verificationCode = verificationCode,
                        isverify = false,
                        profilePic = "",
                        sectionId = s.id
                    )

                    saveStudentToDatabase(student)





                    break
                }
            }

            if(flag){
                Toast.makeText(this, "${selectedDepartment.dept_short_name} Don't Have Batch ${batch} or Section ${section}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchSectionsByDepartment(department: String, callback: (List<Section>) -> Unit) {
        // Query the Section table by department code
        val sectionRef = FirebaseDatabase.getInstance().getReference("Section")

        // Query the Section table for the given department
        val query = sectionRef.orderByChild("dept").equalTo(department)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sections = mutableListOf<Section>()
                if (snapshot.exists()) {
                    for (sectionData in snapshot.children) {
                        val section = sectionData.getValue(Section::class.java)
                        if (section != null) {
                            sections.add(section)
                        }
                    }
                    // Return the list of sections related to the department
                    callback(sections)
                } else {
                    // No sections found for the department
                    callback(emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseQuery", "Error fetching sections: ${error.message}")
                callback(emptyList()) // Return empty list in case of error
            }
        })
    }


    private fun saveStudentToDatabase(student: Student) {
        val studentDatabase = FirebaseDatabase.getInstance().getReference("Student")

        studentDatabase.child(student.id!!).setValue(student)
            .addOnSuccessListener {
                Toast.makeText(this, "Student signed up successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, VerificationActivity::class.java)
                intent.putExtra("id", student.id)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to sign up student", Toast.LENGTH_SHORT).show()
                Log.e("FirebaseQuery", "Error signing up student", it)
            }
    }

}









