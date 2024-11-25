package com.example.schola

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schola.Model.Student
import com.example.schola.databinding.ActivityVerificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class VerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerificationBinding
    private lateinit var database: DatabaseReference
    private var studentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studentId = intent.getStringExtra("id")
        if (studentId == null) {
            Toast.makeText(this, "Invalid Student ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        database = FirebaseDatabase.getInstance().getReference("Student")

        setupOtpInputs()

        binding.verifybtn.setOnClickListener {
            verifyStudent()
        }
    }

    private fun setupOtpInputs() {
        val otpFields = listOf(binding.otp1, binding.otp2, binding.otp3, binding.otp4)
        for (i in otpFields.indices) {
            otpFields[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        if (i < otpFields.size - 1) {
                            otpFields[i + 1].requestFocus()
                        }
                    } else if (s?.isEmpty() == true) {
                        if (i > 0) {
                            otpFields[i - 1].requestFocus()
                        }
                    }
                }
            })
        }
    }

    private fun verifyStudent() {
        val otp = "${binding.otp1.text}${binding.otp2.text}${binding.otp3.text}${binding.otp4.text}"

        if (otp.length != 4) {
            Toast.makeText(this, "Please enter a 4-digit OTP", Toast.LENGTH_SHORT).show()
            return
        }

        studentId?.let { id ->
            database.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val student = snapshot.getValue(Student::class.java)
                    if (student != null && student.verificationCode == otp) {
                        snapshot.ref.child("isverify").setValue(true)
                        Toast.makeText(this@VerificationActivity, "Verification Successful", Toast.LENGTH_SHORT).show()



                        goToCreatePasswordActivity()
                    } else {
                        Toast.makeText(this@VerificationActivity, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@VerificationActivity, "Failed to verify. Please try again.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun goToCreatePasswordActivity() {







        val intent = Intent(this, CreatePasswordActivity::class.java)
        intent.putExtra("id",studentId)
        startActivity(intent)
        finish()
    }
}
