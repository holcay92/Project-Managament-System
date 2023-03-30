package com.example.projectmanager.activities

import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanager.R
import com.example.projectmanager.adapters.MemberListItemAdapter
import com.example.projectmanager.databinding.ActivityMemberBinding
import com.example.projectmanager.firebase.FireStoreClass
import com.example.projectmanager.modals.Board
import com.example.projectmanager.modals.User
import com.example.projectmanager.utils.Constants

class MemberActivity : BaseActivity() {
    private lateinit var mBoardDetails: Board
    private var binding: ActivityMemberBinding? = null
    private lateinit var mAssignedMemberDetailList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (intent.hasExtra(Constants.BOARD_DETAIL)) {
            mBoardDetails = intent.getParcelableExtra(Constants.BOARD_DETAIL)!!
        }
        setupActionBar()
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getAssignedMembersListDetails(this, mBoardDetails.assignedTo)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarMembersActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.members)
        }
        binding?.toolbarMembersActivity?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun memberDetails(user: User) {
        mBoardDetails.assignedTo.add(user.id)
        FireStoreClass().assignMemberToBoard(this, mBoardDetails, user)
    }
        fun setUpMembersList(list: ArrayList<User>) {
            mAssignedMemberDetailList = list
            hideProgressDialog()
            binding?.rvMembersList?.layoutManager = LinearLayoutManager(this)
            binding?.rvMembersList?.setHasFixedSize(true)
            val adapter = MemberListItemAdapter(this, list)
            binding?.rvMembersList?.adapter = adapter
        }

        // whenever we use the menu in the activity
        // we need to override the onCreateOptionsMenu and onOptionsItemSelected!!
        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu_add_member, menu)
            return super.onCreateOptionsMenu(menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.action_add_member -> {
                    dialogSearchMember()
                    return true
                }
            }
            return super.onOptionsItemSelected(item)
        }

        private fun dialogSearchMember() {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_search_member)
            dialog.findViewById<TextView>(R.id.tv_add).setOnClickListener {
                val email =
                    dialog.findViewById<TextView>(R.id.et_email_search_member).text.toString()
                if (email.isNotEmpty()) {
                    dialog.dismiss()
                    showProgressDialog(resources.getString(R.string.please_wait))
                    FireStoreClass().getMemberDetails(this, email)
                } else {
                    Toast.makeText(this, "Please enter a email", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

    fun memberAssignSuccess(user: User) {
        hideProgressDialog()
        mAssignedMemberDetailList.add(user)
        setUpMembersList(mAssignedMemberDetailList)
    }
}