package com.example.schola


import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.schola.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.schola.Class.MyClass
import com.example.schola.Fragments.ClassRoutineFragment
import com.example.schola.Fragments.HomeFragment
import com.example.schola.Fragments.MessageFragment
import com.example.schola.Fragments.ProfileFragment
import com.example.schola.ClassTestListActivity
import com.example.schola.Model.Student
import com.example.schola.Model.Teacher

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding : ActivityMainBinding
    lateinit var headerView: View
    var flag:Boolean = false

    lateinit var largeProfilePic : ImageView
    lateinit var name: TextView
    lateinit var designation: TextView
    lateinit var department: TextView

    lateinit var currentTeacher : Teacher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        headerView = binding.navView.getHeaderView(0)
        largeProfilePic = headerView.findViewById(R.id.largeProfilePic)
        name = headerView.findViewById(R.id.name)
        designation = headerView.findViewById(R.id.designation)
        department = headerView.findViewById(R.id.department)






        setUpForNavigationDrawer()
        setProfilePic()
        handleMenuIconClick()
        setupBottomNavigation()
        setFragment(HomeFragment())





    }



    private fun set2() {



    }

    private fun setupBottomNavigation() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_profile -> ProfileFragment()
                R.id.nav_class_routine -> ClassRoutineFragment()
                R.id.nav_message -> MessageFragment()
                else -> HomeFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame, selectedFragment)
                .commit()
            true
        }
    }

    private fun handleMenuIconClick() {
        binding.menuicon.setOnClickListener {
            if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }    }

    private fun setUpForNavigationDrawer() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun setProfilePic() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Student")

       // val studentId = "yourStudentId"

        MyClass().getCurrentStudentAndSection { result->
            if(result!=null){

                var (studentId,_) = result

                studentId.toString()

                databaseReference.child(studentId!!).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val student = snapshot.getValue(Student::class.java)
                            if (student != null) {
                                // Set student name and ID
                                name.text = student.name ?: "N/A"
                                designation.text = student.id ?: "N/A"
                            } else {
                                name.text = "Student not found"
                                designation.text = ""
                            }
                        } else {
                            name.text = "No student record found"
                            designation.text = ""
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        name.text = "Error loading student"
                        designation.text = ""
                    }
                })

                // Default Profile Picture
                Glide.with(this)
                    .load(R.drawable.stuprofile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profilePic)

                Glide.with(this)
                    .load(R.drawable.stuprofile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(largeProfilePic)






            }






        }










    }


    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_faculty_members -> {

               startActivity(Intent(this,FacultyActivity::class.java))

            }
            R.id.nav_academic_calendar -> {


            }
            R.id.nav_notice -> {


            }
            R.id.nav_settings -> {


            }
            R.id.nav_logout -> {

                var auth = FirebaseAuth.getInstance()

                auth.signOut()

                startActivity(Intent(this,WelcomeActivity::class.java))
                finish()

            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    fun setFragment(fragment: Fragment){
        val fragmentManager : FragmentManager = supportFragmentManager
        val frammentTransition : FragmentTransaction = fragmentManager.beginTransaction()

        if(!flag){
            frammentTransition.add(R.id.frame,fragment)
            flag = true
        }
        else{
            frammentTransition.replace(R.id.frame,fragment)
        }
        frammentTransition.commit()
    }



}


