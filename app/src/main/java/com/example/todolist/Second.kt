package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class Second : AppCompatActivity() {
    private lateinit var todoListView: ListView
    private lateinit var addTaskButton: Button
    private lateinit var taskInput: EditText
    private lateinit var tasks: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var back: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second) // Corrected layout reference

        // Initialize views
        back=findViewById(R.id.button2)
        todoListView = findViewById(R.id.todoListView)
        addTaskButton = findViewById(R.id.addTaskButton)
        taskInput = findViewById(R.id.taskInput)

        // Initialize the task list and adapter
        tasks = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks)
        todoListView.adapter = adapter

        // Get the username from MainActivity
        val username = intent.getStringExtra("Extra")

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

        // Add task button functionality
        addTaskButton.setOnClickListener {
            val newTask = taskInput.text.toString()
            if (newTask.isNotEmpty()) {
                tasks.add("$username: $newTask") // Include the username with the task
                adapter.notifyDataSetChanged()  // Refresh the ListView
                taskInput.text.clear()  // Clear the input field
            }
        }

        // Remove task when clicking on it
        todoListView.setOnItemClickListener { _, _, position, _ ->
            tasks.removeAt(position)
            adapter.notifyDataSetChanged()  // Refresh the ListView
        }
    }
}
