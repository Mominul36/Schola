package com.example.schola

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schola.Adapter.MessageAdapter
import com.example.schola.Class.ControlImage
import com.example.schola.Class.MyClass
import com.example.schola.Model.Message
import com.example.schola.databinding.ActivityMessageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MessageActivity : AppCompatActivity() {
    lateinit var binding: ActivityMessageBinding
    lateinit var opId : String
    lateinit var messageAdapter: MessageAdapter
    lateinit var messageList : MutableList<Message>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adjustMessageLayout()

        opId = intent.getStringExtra("opId").toString()

        setPicAndName()





        MyClass().getCurrentStudentAndSection { result->

            if(result!=null){


                var(myId,_) = result
                myId = myId.toString()

                var messageId = generateConsistentString(myId!!,opId)

                binding.recyclerView.layoutManager = LinearLayoutManager(this)

                messageList = mutableListOf()
                messageAdapter = MessageAdapter(this,myId, messageList)
                binding.recyclerView.adapter = messageAdapter
                // binding.recyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)


                var database = FirebaseDatabase.getInstance().getReference("Message").child(messageId)


                database.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            messageList.clear()
                            for (messageSnapshot in snapshot.children) {
                                val message = messageSnapshot.getValue(Message::class.java)
                                message?.let {
                                    messageList.add(it)
                                }
                            }
                            // Update the adapter with the fetched messages
                            //messageAdapter.submitList(messages)
                            messageAdapter.notifyDataSetChanged()

                            binding.recyclerView.post {
                                binding.recyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })





            }








        }



        binding.sendbtn.setOnClickListener{
            MyClass().getCurrentStudentAndSection { result->
                if(result!=null){

                    var (myId,_) = result
                    myId = myId.toString()

                    var messageId = generateConsistentString(myId!!,opId)
                    var text = binding.messageedit.text.toString()
                    val (date, time, dateTime) = getFormattedDateTime()

                    var database = FirebaseDatabase.getInstance().getReference("Message").child(messageId)
                    var id = database.push().key

                    var message = Message(id,myId,opId,text,date,time,dateTime)

                    database.child(id!!).setValue(message)
                        .addOnSuccessListener {
                            binding.messageedit.text.clear()
                        }
                        .addOnFailureListener{
                        }
                }

            }
        }



        binding.back.setOnClickListener{
            finish()
        }


















    }





    private fun setPicAndName() {
        var database = FirebaseDatabase.getInstance().getReference("User")
        database.child(opId).get().addOnSuccessListener {
            if(it.exists()){
                binding.name.text = it.child("name").value.toString()
                var imageURL = it.child("pic").value.toString()

                if(imageURL==""){
                    binding.pic.setImageResource(R.drawable.profile)
                }else{
                    ControlImage(this, this.activityResultRegistry,
                        "imagePickerKey").setImageByURl(imageURL,binding.pic)
                }
            }
        }



    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormattedDateTime(): Triple<String, String, String> {
        val currentDate = LocalDate.now()
        val currentTime = LocalTime.now()
        val currentDateTime = LocalDateTime.now()

        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

        val formattedDate = currentDate.format(dateFormatter)
        val formattedTime = currentTime.format(timeFormatter)
        val formattedDateTime = currentDateTime.format(dateTimeFormatter)

        return Triple(formattedDate, formattedTime, formattedDateTime)
    }

    fun generateConsistentString(string1: String, string2: String): String {
        val sortedStrings = listOf(string1, string2).sorted() // Sort the strings
        return sortedStrings.joinToString("-") // Join with a delimiter (optional)
    }


    private fun adjustMessageLayout() {
        val rootView = findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)

            val screenHeight = rootView.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is open
                binding.recyclerView.layoutParams = (binding.recyclerView.layoutParams as RelativeLayout.LayoutParams).apply {
                    topMargin = 0 // No margin below the toolbar
                    bottomMargin = keypadHeight // Adjust bottom margin for keyboard
                }
                binding.messagelayout.translationY = -keypadHeight.toFloat()
            } else {
                // Keyboard is closed
                binding.recyclerView.layoutParams = (binding.recyclerView.layoutParams as RelativeLayout.LayoutParams).apply {
                    topMargin = 0 // No margin below the toolbar
                    bottomMargin = 0 // Reset margin when keyboard is closed
                }
                binding.messagelayout.translationY = 0f

                // Adjust message layout for system gesture navigation
                val systemBarsInsets = ViewCompat.getRootWindowInsets(rootView)?.getInsets(WindowInsetsCompat.Type.systemBars())
                val bottomBarHeight = systemBarsInsets?.bottom ?: 0
                binding.messagelayout.setPadding(
                    binding.messagelayout.paddingLeft,
                    binding.messagelayout.paddingTop,
                    binding.messagelayout.paddingRight,
                    bottomBarHeight
                )
            }

            // Request layout update
            binding.recyclerView.requestLayout()
        }
    }




}
