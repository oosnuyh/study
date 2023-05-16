package com.example.selfstudy.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.selfstudy.databinding.FragmentHomeBinding
import com.example.selfstudy.login.Login


class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.loginBtn.setOnClickListener(){
            val intent = Intent(context, Login::class.java)
            startActivity(intent)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

}