// MainActivity.kt
package com.example.ctamodule3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var taskDatabaseHelper: TaskDatabaseHelper
    private lateinit var todoList: MutableList<Task>
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskDatabaseHelper = TaskDatabaseHelper(this)
        todoList = taskDatabaseHelper.getAllTasks().toMutableList()
        taskAdapter = TaskAdapter(this, todoList)

        val taskListView: ListView = findViewById(R.id.taskListView)
        taskListView.adapter = taskAdapter

        val addTaskButton: Button = findViewById(R.id.addTaskButton)
        val taskEditText: EditText = findViewById(R.id.taskEditText)

        addTaskButton.setOnClickListener {
            val taskName = taskEditText.text.toString().trim()
            if (taskName.isNotEmpty()) {
                val newTask = Task(taskName)
                taskDatabaseHelper.insertTask(newTask)

                // Update UI
                todoList.add(newTask)
                taskAdapter.notifyDataSetChanged()
                taskEditText.text.clear()
            } else {
                Toast.makeText(this, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        taskListView.setOnItemClickListener { _, _, position, _ ->
            // Handle task item click (for marking as done)
            val clickedTask = todoList[position]
            if (!clickedTask.isDone) {
                clickedTask.isDone = true
                taskDatabaseHelper.updateTask(clickedTask)

                // Update UI
                taskAdapter.notifyDataSetChanged()
            }

            Toast.makeText(this, "Clicked on: ${clickedTask.name}", Toast.LENGTH_SHORT).show()
        }

        taskListView.setOnItemLongClickListener { _, _, position, _ ->
            // Handle task item long click (for deletion)
            val deletedTask = todoList[position]
            taskDatabaseHelper.deleteTask(deletedTask)

            // Update UI
            todoList.removeAt(position)
            taskAdapter.notifyDataSetChanged()

            Toast.makeText(this, "Deleted: ${deletedTask.name}", Toast.LENGTH_SHORT).show()

            true
        }
    }
}
