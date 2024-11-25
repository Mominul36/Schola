package com.example.schola


import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.schola.Class.ControlImage
import com.example.schola.Class.MyClass
import com.example.schola.Model.Question
import com.example.schola.databinding.ActivityAddQuestionBinding
import com.google.firebase.database.FirebaseDatabase

class AddQuestionActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddQuestionBinding
    private lateinit var controlImage: ControlImage
    private var isImageSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controlImage = ControlImage(this, this.activityResultRegistry, "imagePickerKey")
        setupSpinners()

        binding.set.setOnClickListener {
            controlImage.setImageView(binding.image)
            controlImage.selectImage()
        }



        binding.upload.setOnClickListener {
            if (!isImageSelected) {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            uploadQuestion()
        }

        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun uploadQuestion() {
        controlImage.uploadImageToFirebaseStorage { isSuccess, message ->
            if (isSuccess) {
                val imageURL = message
                saveImageUriToDatabase(imageURL)
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImageUriToDatabase(imageURL: String) {
        val database = FirebaseDatabase.getInstance().getReference("Question")
        val id = database.push().key

        val question = Question(
            id,
            binding.dept.selectedItem.toString(),
            binding.lt.selectedItem.toString(),
            binding.semester.selectedItem.toString(),
            imageURL
        )

        database.child(id!!).setValue(question)
            .addOnSuccessListener {
                Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupSpinners() {
        // Data for the spinners
        val deptList = listOf("CSE", "EEE", "ME", "ICT", "IPE", "CE", "BBA", "ENGLISH")
        val ltList = listOf("1-I", "1-II", "2-I", "2-II", "3-I", "3-II", "4-I", "4-II")
        val semesterList = listOf("Win 2020", "Sum 2020","Win 2021", "Sum 2021", "Win 2022", "Sum 2022", "Win 2023", "Sum 2023",  "Win 2024", "Sum 2024")

        val deptAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, deptList)
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.dept.adapter = deptAdapter

        val ltAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ltList)
        ltAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.lt.adapter = ltAdapter

        val semesterAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, semesterList)
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.semester.adapter = semesterAdapter
    }


    fun setImageUriSelected() {
        isImageSelected = true
    }


    override fun onResume() {
        super.onResume()
        isImageSelected = controlImage.imageUri != null
    }
}
