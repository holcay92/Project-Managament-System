package com.example.projectmanager.activities

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.projectmanager.R
import com.example.projectmanager.firebase.FireStoreClass
import com.example.projectmanager.modals.Board
import com.example.projectmanager.utils.Constants

class TaskListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        var boardDocumentId = ""
        if (intent.hasExtra(Constants.DOCUMENT_ID)) {
            boardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID)!!
        }
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getBoardDetails(this, boardDocumentId)

    }

    private fun setupActionBar(title: String) {
        setSupportActionBar(findViewById(R.id.toolbar_task_list_activity))
        var toolbar = findViewById<Toolbar>(R.id.toolbar_task_list_activity)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            supportActionBar?.title = title
        }
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    fun boardDetails(board: Board) {
        hideProgressDialog()
        setupActionBar(board.name)
    }
}