// TaskAdapter.kt
package com.example.ctamodule3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

class TaskAdapter(private val context: Context, private val tasks: MutableList<Task>) : BaseAdapter() {

    override fun getCount(): Int {
        return tasks.size
    }

    override fun getItem(position: Int): Any {
        return tasks[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val task = getItem(position) as Task

        val view: View = convertView
            ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)

        val taskTextView: TextView = view.findViewById(android.R.id.text1)
        taskTextView.text = task.name

        // Update background color based on task completion status
        if (task.isDone) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.doneTaskColor))
        } else {
            // Set default color or another color for incomplete tasks
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.defaultTaskColor))
        }

        return view
    }
}
