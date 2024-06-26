package com.example.myapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class loading : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        // Get references to the EditText fields
        val editTextText1 = findViewById<EditText>(R.id.editTextText1)
        val editTextText2 = findViewById<EditText>(R.id.editTextText2)
        val editTextText3 = findViewById<EditText>(R.id.editTextText3)
        val editTextText4 = findViewById<EditText>(R.id.editTextText4)
        val editTextText5 = findViewById<EditText>(R.id.editTextText5)
        val editTextText6 = findViewById<EditText>(R.id.editTextText6)
        val editTextText7 = findViewById<EditText>(R.id.editTextText7)
        val editTextText8 = findViewById<EditText>(R.id.editTextText8)

        // Get reference to the Button
        val button4 = findViewById<Button>(R.id.button4)

        // Set an OnClickListener on the Button
        button4.setOnClickListener {
            // Fetch the strings from the EditText fields
            val riverName = editTextText1.text.toString()
            val landscapeType = editTextText2.text.toString()
            val state = editTextText3.text.toString()
            val beatName = editTextText4.text.toString()
            val forestBlockName = editTextText5.text.toString()
            val siteName = editTextText6.text.toString()
            val forestRangeName = editTextText7.text.toString()
            val forestDivisionName = editTextText8.text.toString()

            // Check if any field is empty
            if (riverName.isEmpty() || landscapeType.isEmpty() || state.isEmpty() || beatName.isEmpty() ||
                forestBlockName.isEmpty() || siteName.isEmpty() || forestRangeName.isEmpty() || forestDivisionName.isEmpty()) {
                // Show a Toast message to prompt the user to fill in all details
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            } else {
                // Create an Intent to start the PreFinal activity
                val intent = Intent(this, preFinal::class.java)

                // Put the fetched strings as extras in the Intent
                intent.putExtra("RIVER_NAME", riverName)
                intent.putExtra("LANDSCAPE_TYPE", landscapeType)
                intent.putExtra("STATE", state)
                intent.putExtra("BEAT_NAME", beatName)
                intent.putExtra("FOREST_BLOCK_NAME", forestBlockName)
                intent.putExtra("SITE_NAME", siteName)
                intent.putExtra("FOREST_RANGE_NAME", forestRangeName)
                intent.putExtra("FOREST_DIVISION_NAME", forestDivisionName)

                // Start the PreFinal activity
                startActivity(intent)
            }
        }
    }
}
