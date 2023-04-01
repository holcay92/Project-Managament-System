package com.example.projectmanager.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.projectmanager.R
import com.example.projectmanager.databinding.ActivityCardDetailsBinding
import com.example.projectmanager.firebase.FireStoreClass
import com.example.projectmanager.modals.Board
import com.example.projectmanager.modals.Card
import com.example.projectmanager.modals.Task
import com.example.projectmanager.utils.Constants

class CardDetailsActivity : BaseActivity() {

    private var binding: ActivityCardDetailsBinding? = null
    private lateinit var mBoardDetails: Board
    private var mTaskList = -1
    private var mCardPosition = -1

    //private var mBoardDocumentId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        getIntentData()
        setupActionBar()
        binding?.apply {
            etNameCardDetails.setText(mBoardDetails.taskList[mTaskList].cards[mCardPosition].name)
            // this is to set the cursor at the end of the text
            etNameCardDetails.setSelection(etNameCardDetails.text.toString().length)
            btnUpdateCardDetails.setOnClickListener {
                if (etNameCardDetails.text.toString().isNotEmpty()) {
                    updateCardDetails()
                } else {
                    showErrorSnackBar("Please enter a card name.")
                }
            }
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarCardDetailsActivity)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            title = mBoardDetails.taskList[mTaskList].cards[mCardPosition].name
        }
        binding?.toolbarCardDetailsActivity?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_card, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_delete_card -> {
                alertDialogForDeleteCard(mBoardDetails.taskList[mTaskList].cards[mCardPosition].name)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

    fun addUpdateTaskListSuccess() {
        hideProgressDialog()
        setResult(RESULT_OK)
        finish()
    }

    private fun updateCardDetails() {
        val card = Card(
            binding?.etNameCardDetails?.text.toString(),
            mBoardDetails.taskList[mTaskList].cards[mCardPosition].createdBy,
            mBoardDetails.taskList[mTaskList].cards[mCardPosition].assignedTo
        )
        mBoardDetails.taskList[mTaskList].cards[mCardPosition] = card
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addUpdateTaskList(this, mBoardDetails)

    }

    private fun deleteCard() {
        val cardsList: ArrayList<Card> = mBoardDetails.taskList[mTaskList].cards
        cardsList.removeAt(mCardPosition)
        var taskList: ArrayList<Task> = mBoardDetails.taskList
        taskList.removeAt(taskList.size - 1)
        taskList[mTaskList].cards = cardsList
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addUpdateTaskList(this, mBoardDetails)
    }
    private fun alertDialogForDeleteCard(cardName: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.alert))
        builder.setMessage(resources.getString(R.string.confirmation_message_to_delete_card, cardName))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, which ->
            dialogInterface.dismiss()
            deleteCard()
        }
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}