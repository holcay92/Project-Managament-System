package com.example.projectmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectmanager.R
import com.example.projectmanager.databinding.ActivityCreateBoardBinding

class CreateBoardActivity : AppCompatActivity() {
    private var binding: ActivityCreateBoardBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBoardBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding?.toolbarCreateBoardActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.create_board)
        }
        binding?.toolbarCreateBoardActivity?.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }
}