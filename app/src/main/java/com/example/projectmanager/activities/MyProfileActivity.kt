package com.example.projectmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectmanager.R
import com.example.projectmanager.databinding.ActivityMyProfileBinding

class MyProfileActivity : BaseActivity() {
    private var binding : ActivityMyProfileBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_my_profile)
        setupActionBar()
    }
    private fun setupActionBar() {

        setSupportActionBar(binding?.toolbarMyProfileActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.my_profile)
        }

        binding?.toolbarMyProfileActivity?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed() }
    }
}