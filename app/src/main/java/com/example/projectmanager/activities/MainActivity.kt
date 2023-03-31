package com.example.projectmanager.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectmanager.R
import com.example.projectmanager.adapters.BoardItemsAdapter
import com.example.projectmanager.databinding.ActivityMainBinding
import com.example.projectmanager.firebase.FireStoreClass
import com.example.projectmanager.modals.Board
import com.example.projectmanager.modals.User
import com.example.projectmanager.utils.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var mUserName: String

    private var myRV : RecyclerView? = null
    companion object {
        const val MY_PROFILE_REQUEST_CODE: Int = 11
        const val CREATE_BOARD_REQUEST_CODE: Int = 12
        const val TAG = "MainActivity"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        setupActionBar()
       binding.navView.setNavigationItemSelectedListener(this)
        myRV = findViewById(R.id.rv_boards_list)

        FireStoreClass().loadUserData(this, true)

        findViewById<FloatingActionButton>(R.id.fab_create_board).setOnClickListener {
            val intent = Intent(this, CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME, mUserName)
            startActivityForResult(intent, CREATE_BOARD_REQUEST_CODE)
        }
    }

    fun populateBoardListToUI(boardList: ArrayList<Board>) {
        hideProgressDialog()
        if (boardList.size > 0) {
            myRV?.visibility = android.view.View.VISIBLE
            findViewById<TextView>(R.id.tv_no_boards_available).visibility = android.view.View.GONE
            myRV?.layoutManager = LinearLayoutManager(this)
            findViewById<RecyclerView>(R.id.rv_boards_list).setHasFixedSize(true)
            val adapter = BoardItemsAdapter(this, boardList)
            myRV?.adapter = adapter
            adapter.setOnClickListener(object : BoardItemsAdapter.OnItemClickListener {
                override fun onClick(position: Int, model: Board) {
                    val intent = Intent(this@MainActivity, TaskListActivity::class.java)
                    intent.putExtra(Constants.DOCUMENT_ID, model.documentId)
                    startActivity(intent)
                }
            })

        } else {
          myRV?.visibility = android.view.View.GONE
          findViewById<TextView>(R.id.tv_no_boards_available).visibility = android.view.View.VISIBLE
        }
    }

    private fun setupActionBar() {
        var toolbar = findViewById<Toolbar>(R.id.toolbar_main_activity)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        toolbar.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer() {

        binding.drawerLayout.apply {
            if (isDrawerOpen(GravityCompat.START)) {
                closeDrawer(GravityCompat.START)
            } else {
                openDrawer(GravityCompat.START)
            }
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_my_profile -> {
                startActivityForResult(
                    Intent(this@MainActivity, MyProfileActivity::class.java),
                    MY_PROFILE_REQUEST_CODE
                )

            }
            R.id.nav_sign_out -> {
                Toast.makeText(this@MainActivity, "Sign Out", Toast.LENGTH_SHORT).show()
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@MainActivity, IntroActivity::class.java)
                // Clear all the activities and start new task or else it will open the previous activity.
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateNavigationUserDetails(user: User, readBoardList:Boolean) {
        mUserName = user.name
        Glide.with(this@MainActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(findViewById<CircleImageView>(R.id.iv_user_image))

        findViewById<TextView>(R.id.tv_username).text = user.name

        if(readBoardList){
            showProgressDialog(resources.getString(R.string.please_wait))
            FireStoreClass().getBoardList(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MY_PROFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            FireStoreClass().loadUserData(this)
        }/*else{
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
        }*/else if(requestCode == CREATE_BOARD_REQUEST_CODE && resultCode == RESULT_OK){
            FireStoreClass().getBoardList(this)
        }
    }
}