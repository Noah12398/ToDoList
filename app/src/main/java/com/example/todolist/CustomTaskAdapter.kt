package com.example.todolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

class CustomTaskAdapter(
    context: Context,
    private val tasks: ArrayList<String>,
    private val taskIds: ArrayList<String>
) : ArrayAdapter<String>(context, 0, tasks) {

    private val db = FirebaseFirestore.getInstance().collection("tasks")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listItemView = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.list_item, parent, false
        )

        val taskTextView = listItemView.findViewById<TextView>(R.id.taskTextView)
        val deleteButton = listItemView.findViewById<Button>(R.id.deleteTaskButton)

        // Set task text
        val currentTask = tasks[position]
        taskTextView.text = currentTask

        // Delete button functionality
        deleteButton.setOnClickListener {
            val taskId = taskIds[position]
            deleteTaskFromFirestore(taskId, position)
        }

        return listItemView
    }

    // Method to delete a task from Firestore
    private fun deleteTaskFromFirestore(taskId: String, position: Int) {
        db.document(taskId)
            .delete()
            .addOnSuccessListener {
                tasks.removeAt(position)  // Remove from list
                taskIds.removeAt(position) // Remove ID from list
                notifyDataSetChanged()     // Refresh ListView
            }
            .addOnFailureListener { e ->
                // Handle failure
                e.printStackTrace()
            }
    }
}
