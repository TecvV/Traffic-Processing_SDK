package com.example.myapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class preFinal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_final)

        // Retrieve the data from the previous Intent
        val riverName = intent.getStringExtra("RIVER_NAME")
        val landscapeType = intent.getStringExtra("LANDSCAPE_TYPE")
        val state = intent.getStringExtra("STATE")
        val beatName = intent.getStringExtra("BEAT_NAME")
        val forestBlockName = intent.getStringExtra("FOREST_BLOCK_NAME")
        val siteName = intent.getStringExtra("SITE_NAME")
        val forestRangeName = intent.getStringExtra("FOREST_RANGE_NAME")
        val forestDivisionName = intent.getStringExtra("FOREST_DIVISION_NAME")

        // Get references to the EditText fields
        val editTextText1 = findViewById<EditText>(R.id.editTextText1)
        val editTextText2 = findViewById<EditText>(R.id.editTextText2)
        val editTextText3 = findViewById<EditText>(R.id.editTextText3)
        val editTextText6 = findViewById<EditText>(R.id.editTextText6)
        val editTextText7 = findViewById<EditText>(R.id.editTextText7)
        val editTextText8 = findViewById<EditText>(R.id.editTextText8)

        // Get reference to the Button
        val button4 = findViewById<Button>(R.id.button4)

        // Set an OnClickListener on the Button
        button4.setOnClickListener {
            // Fetch the strings from the EditText fields
            val activities = editTextText1.text.toString()
            val district = editTextText2.text.toString()
            val area = editTextText3.text.toString()
            val remarks = editTextText6.text.toString()
            val tehsil = editTextText7.text.toString()
            val panchayat = editTextText8.text.toString()

            // Create an Intent to start the Final activity
            val intent = Intent(this, Final::class.java)

            // Put the fetched strings as extras in the Intent
            intent.putExtra("RIVER_NAME", riverName)
            intent.putExtra("LANDSCAPE_TYPE", landscapeType)
            intent.putExtra("STATE", state)
            intent.putExtra("BEAT_NAME", beatName)
            intent.putExtra("FOREST_BLOCK_NAME", forestBlockName)
            intent.putExtra("SITE_NAME", siteName)
            intent.putExtra("FOREST_RANGE_NAME", forestRangeName)
            intent.putExtra("FOREST_DIVISION_NAME", forestDivisionName)
            intent.putExtra("ACTIVITIES", activities)
            intent.putExtra("DISTRICT", district)
            intent.putExtra("AREA", area)
            intent.putExtra("REMARKS", remarks)
            intent.putExtra("TEHSIL", tehsil)
            intent.putExtra("PANCHAYAT", panchayat)

            // Start the Final activity
            startActivity(intent)
        }
    }
}
