package com.example.schola

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.schola.Model.Section
import com.example.schola.Model.Student
import com.example.schola.databinding.ActivityWelcomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random

class WelcomeActivity : AppCompatActivity() {
    lateinit var binding : ActivityWelcomeBinding
    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("Department")
        auth = FirebaseAuth.getInstance()

        var user=  auth.currentUser
        if(user!=null){
            startActivity(Intent(this,MainActivity::class.java))
        }


        binding.signupbtn.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        binding.loginbtn.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }



    }




}