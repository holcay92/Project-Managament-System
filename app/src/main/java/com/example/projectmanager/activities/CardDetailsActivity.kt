package com.example.projectmanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectmanager.R
import com.example.projectmanager.databinding.ActivityCardDetailsBinding
import com.example.projectmanager.modals.Board
import com.example.projectmanager.modals.Card
import com.example.projectmanager.modals.Task
import com.example.projectmanager.utils.Constants

class CardDetailsActivity : AppCompatActivity() {

    private var binding: ActivityCardDetailsBinding? = null
    private lateinit var mBoardDetails: Board
    private var mTaskList= -1
    private var mCardPosition= -1
    //private var mBoardDocumentId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        getIntentData()
        setupActionBar()

    }
    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarCardDetailsActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = mBoardDetails.taskList[mTaskList].cards[mCardPosition].name

        }
        binding?.toolbarCardDetailsActivity?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun getIntentData() {
        if (intent.hasExtra(Constants.BOARD_DETAIL)) {
            mBoardDetails = intent.getParcelableExtra(Constants.BOARD_DETAIL)!!
        }
        if (intent.hasExtra(Constants.TASK_LIST_ITEM_POSITION)) {
            mTaskList = intent.getIntExtra(Constants.TASK_LIST_ITEM_POSITION, -1)
        }
        if (intent.hasExtra(Constants.CARD_LIST_ITEM_POSITION)) {
            mCardPosition = intent.getIntExtra(Constants.CARD_LIST_ITEM_POSITION, -1)
        }
    }
}