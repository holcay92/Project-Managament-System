package com.example.projectmanager.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.R
import com.example.projectmanager.adapters.LabelColorListAdapter

abstract class LabelColorListDialog(
    context: Context,
    private var list: ArrayList<String>,
    private val title:String = "",
    private var mSelectedColor: String
    ) : Dialog(context) {

    private var adapter: LabelColorListAdapter? = null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.dialog_list, null)
        setContentView(view)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView(view)
    }

      @SuppressLint("CutPasteId")
      private fun setUpRecyclerView(view: View) {
          view.findViewById<TextView>(R.id.tvTitle).text = title
          //change it with Linear Layout Manager afterwards TODO
          view.findViewById<RecyclerView>(R.id.rvList).layoutManager = LinearLayoutManager(context)
            adapter = LabelColorListAdapter(context, list, mSelectedColor)
            view.findViewById<RecyclerView>(R.id.rvList).adapter = adapter
            adapter!!.onItemClickListener =
                object : LabelColorListAdapter.OnItemClickListener {
                override fun onClick(position: Int, color: String) {
                    dismiss()
                    onItemSelected(color)


                }
            }
      }
    protected abstract fun onItemSelected(color: String)
}


