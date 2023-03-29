package com.example.projectmanager.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.R
import com.example.projectmanager.databinding.ItemTaskBinding
import com.example.projectmanager.modals.Task

open class TaskListItemsAdapter(private val context: Context, private var list: ArrayList<Task>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var binding: ItemTaskBinding? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false)
        val layoutParams = LinearLayout.LayoutParams(
            (parent.width * 0.7).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        // margins order starts left top right bottom
        layoutParams.setMargins((15.toDp()).toPx(), 0, (40.toDp()).toPx(), 0)
        view.layoutParams = layoutParams
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val modal = list[position]
        binding = ItemTaskBinding.bind(holder.itemView)
        if (holder is MyViewHolder) {
            if (position == list.size - 1) {
                binding?.tvAddTaskList?.visibility = View.VISIBLE
                binding?.llTaskItem?.visibility = View.GONE
            } else {
                binding?.tvAddTaskList?.visibility = View.GONE
                binding?.llTaskItem?.visibility = View.VISIBLE
            }
        }
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    // it is for adjusting the size of the view px to dp
    private fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

    // it is for adjusting the size of the view dp to px
    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}