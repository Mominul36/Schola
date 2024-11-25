package com.example.schola

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schola.Adapter.QuestionAdapter
import com.example.schola.Class.ControlImage
import com.example.schola.Model.Question
import com.example.schola.databinding.ActivityQuestionBinding
import com.google.firebase.database.*

class QuestionActivity : AppCompatActivity() {

    lateinit var binding: ActivityQuestionBinding
    private lateinit var database: DatabaseReference
    private lateinit var questionList: MutableList<Question>
    private lateinit var questionAdapter: QuestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupSpinners()


        binding.back.setOnClickListener {
            finish()
        }
        binding.search.setOnClickListener {
            fetchQuestionsFromFirebase()
        }
        binding.dots.setOnClickListener {
            showPopupMenu(it)
        }



        questionList = mutableListOf()
        questionAdapter = QuestionAdapter(this, questionList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = questionAdapter


    }

    private fun setupSpinners() {
        // Set up spinners with values
        val deptList = listOf("CSE", "EEE", "ME", "ICT", "IPE", "CE", "BBA", "ENGLISH")
        val ltList = listOf("1-I", "1-II", "2-I", "2-II", "3-I", "3-II", "4-I", "4-II")
        val semesterList = listOf("Win 2020", "Sum 2020", "Win 2021", "Sum 2021", "Win 2022", "Sum 2022", "Win 2023", "Sum 2023", "Win 2024", "Sum 2024")

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

    private fun fetchQuestionsFromFirebase() {
        val selectedDept = binding.dept.selectedItem.toString()
        val selectedLt = binding.lt.selectedItem.toString()
        val selectedSemester = binding.semester.selectedItem.toString()

        // Initialize Firebase reference
        database = FirebaseDatabase.getInstance().reference.child("Question")

        database.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    questionList.clear()
                    for (questionSnapshot in snapshot.children) {
                        val question = questionSnapshot.getValue(Question::class.java)

                        if(question!=null && question.dept==selectedDept && question.lt==selectedLt && question.semester==selectedSemester){
                            questionList.add(question)
                        }
                    }
                    questionList.reverse()
                    questionAdapter.notifyDataSetChanged()


                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun showPopupMenu(view: android.view.View) {
        val popupMenu = PopupMenu(this, view)
        menuInflater.inflate(R.menu.menu_question, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.add_question -> {
                    startActivity(Intent(this, AddQuestionActivity::class.java))
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}
