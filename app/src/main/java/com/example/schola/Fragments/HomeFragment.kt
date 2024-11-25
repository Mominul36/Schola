package com.example.schola.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.schola.AssignmentActivity
import com.example.schola.AttendenceActivity
import com.example.schola.ClassTestListActivity
import com.example.schola.LabReportActivity
import com.example.schola.NoticeActivity
import com.example.schola.QuestionActivity
import com.example.schola.R
import com.example.schola.SubjectActivity
import com.example.schola.TempActivity
import com.example.schola.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)





        binding.btnSubjects.setOnClickListener{

            startActivity(Intent(requireContext(), SubjectActivity::class.java))
        }

        binding.assignment.setOnClickListener{
            startActivity(Intent(requireContext(), AssignmentActivity::class.java))

        }


        binding.notice.setOnClickListener{
            startActivity(Intent(requireContext(), NoticeActivity::class.java))

        }


        binding.question.setOnClickListener{
            startActivity(Intent(requireContext(), QuestionActivity::class.java))
        }



        binding.attendence.setOnClickListener{

            startActivity(Intent(requireContext(), AttendenceActivity::class.java))
        }

        binding.btnClassTest.setOnClickListener{

            startActivity(Intent(requireContext(), ClassTestListActivity::class.java))
        }


        binding.lr.setOnClickListener{

            startActivity(Intent(requireContext(), LabReportActivity::class.java))
        }



















        return binding.root








    }


}