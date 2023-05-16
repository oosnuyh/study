package com.example.selfstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.selfstudy.databinding.ActivityMainBinding
import com.example.selfstudy.fragment.HomeFragment
import com.example.selfstudy.fragment.Test1Fragment
import com.example.selfstudy.fragment.Test2Fragment
import com.example.selfstudy.fragment.Test3Fragment
import com.example.selfstudy.login.Login
import com.kakao.sdk.common.util.Utility

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)

        val bottomNavigationView = binding.bottomBar

        supportFragmentManager.beginTransaction().add(binding.main.id, HomeFragment()).commit()

        bottomNavigationView.setOnItemSelectedListener { item ->
            changeFragment(
                when (item.itemId) {
                    R.id.menu_home -> {
                        bottomNavigationView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.color_select_menu)
                        bottomNavigationView.itemTextColor = ContextCompat.getColorStateList(this, R.color.color_select_menu)
                        HomeFragment()
                    }
                    R.id.menu_1 -> {
                        bottomNavigationView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.color_select_menu)
                        bottomNavigationView.itemTextColor = ContextCompat.getColorStateList(this, R.color.color_select_menu)
                        Test1Fragment()
                    }
                    R.id.menu_2 -> {
                        bottomNavigationView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.color_select_menu)
                        bottomNavigationView.itemTextColor = ContextCompat.getColorStateList(this, R.color.color_select_menu)
                        Test2Fragment()
                    }

                    else -> {
                        bottomNavigationView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.color_select_menu)
                        bottomNavigationView.itemTextColor = ContextCompat.getColorStateList(this, R.color.color_select_menu)
                        Test3Fragment()
                    }
                }
            )
            true
        }
        bottomNavigationView.selectedItemId = R.id.menu_home
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main, fragment)
            .commit()
    }
}