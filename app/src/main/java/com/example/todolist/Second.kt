package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Second : AppCompatActivity() {
    private lateinit var todoListView: ListView
    private lateinit var addTaskButton: Button
    private lateinit var taskInput: EditText
    private lateinit var tasks: ArrayList<String>
    private lateinit var taskIds: ArrayList<String> // To store task document IDs
    private lateinit var adapter: CustomTaskAdapter
    private lateinit var back: Button

    // Firebase Firestore reference
    private val tasksCollection = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Initialize views
        back = findViewById(R.id.button2)
        todoListView = findViewById(R.id.todoListView)
        addTaskButton = findViewById(R.id.addTaskButton)
        taskInput = findViewById(R.id.taskInput)

        // Initialize the task list and adapter
        tasks = ArrayList()
        taskIds = ArrayList()
        adapter = CustomTaskAdapter(this, tasks, taskIds)
        todoListView.adapter = adapter

        // Get the username from MainActivity
        val username = intent.getStringExtra("Extra")
        val password = intent.getStringExtra("Extrapass")

        // Retrieve tasks for the specific user
        if (username != null && password !=null) {
            loadTasksForUser(username,password)
        }

        // Navigate back to MainActivity
        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Add task button functionality
        addTaskButton.setOnClickListener {
            val newTask = taskInput.text.toString()
            if (newTask.isNotEmpty()) {
                taskInput.text.clear()  // Clear the input field

                // Save the task to Firestore
                saveTask(username ?: "Unknown", newTask,password?:"Unknown")
            }
        }
    }

    // Method to load tasks for the specific user
    private fun loadTasksForUser(username: String, password: String) {
        tasksCollection.collection("tasks").whereEqualTo("Username", username).whereEqualTo("Password",password)
            .get()
            .addOnSuccessListener { result ->
                tasks.clear()  // Clear the task list before loading new data
                taskIds.clear() // Clear task IDs

                for (document in result) {
                    val task = document.getString("Task")
                    task?.let {
                        tasks.add(it)  // Add task if it's not null
                        taskIds.add(document.id)  // Store the document ID for deletion
                        Log.d("Task", "Task loaded: $it")
                    }
                }
                adapter.notifyDataSetChanged()  // Refresh the ListView
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error loading tasks: ", exception)
            }
    }

    // Method to save task to Firestore
    private fun saveTask(username: String, newTask: String, password: String) {
        val data = hashMapOf(
            "Username" to username,
            "Task" to newTask,
            "Password" to password
        )

        tasksCollection.collection("tasks").add(data)
            .addOnSuccessListener {
                Log.d("Firestore", "Task successfully added")
                tasks.add(newTask)
                adapter.notifyDataSetChanged()  // Refresh ListView after adding task
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding task", e)
            }
    }
}