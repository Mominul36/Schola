package com.example.schola.Class


import com.example.schola.Model.Student
import com.example.schola.Model.Teacher
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyClass {

    private lateinit var auth: FirebaseAuth



    fun getCurrentStudentAndSection(onComplete: (Pair<String?, String?>?) -> Unit) {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user == null) {
            onComplete(null) // User not logged in
            return
        }

        val studentId = user.email?.substringBefore("@")
        if (studentId.isNullOrEmpty()) {
            onComplete(null) // Invalid student ID
            return
        }

        val studentRef = FirebaseDatabase.getInstance().getReference("Student")

        // Query the student with the derived student ID
        studentRef.child(studentId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val student = snapshot.getValue(Student::class.java)
                    if (student != null) {
                        // Return studentId and sectionId
                        onComplete(Pair(student.id, student.sectionId))
                    } else {
                        onComplete(null) // Student data is invalid
                    }
                } else {
                    onComplete(null) // Student not found
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(null) // Error occurred
            }
        })
    }



}
