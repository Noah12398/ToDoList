package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.todolist.R

class MainActivity : AppCompatActivity() {
    private lateinit var Login: Button
    private lateinit var Username: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the Login button
        Login = findViewById<Button>(R.id.button)
        Username = findViewById<EditText>(R.id.editTextText)
        val context = this

        // Set an OnClickListener on the Login button
        Login.setOnClickListener {
            val message = Username.text.toString()
            val intent = Intent(context, Second::class.java)
            intent.putExtra("Extra",message)
            context.startActivity(intent) // Start the Second activity
        }
    }
}
