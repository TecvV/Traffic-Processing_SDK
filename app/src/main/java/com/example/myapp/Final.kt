package com.example.myapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class Final : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)

        // Retrieve the data passed from PreFinal activity
        val riverName = intent.getStringExtra("RIVER_NAME")
        val landscapeType = intent.getStringExtra("LANDSCAPE_TYPE")
        val state = intent.getStringExtra("STATE")
        val beatName = intent.getStringExtra("BEAT_NAME")
        val forestBlockName = intent.getStringExtra("FOREST_BLOCK_NAME")
        val siteName = intent.getStringExtra("SITE_NAME")
        val forestRangeName = intent.getStringExtra("FOREST_RANGE_NAME")
        val forestDivisionName = intent.getStringExtra("FOREST_DIVISION_NAME")
        val activities = intent.getStringExtra("ACTIVITIES")
        val district = intent.getStringExtra("DISTRICT")
        val area = intent.getStringExtra("AREA")
        val remarks = intent.getStringExtra("REMARKS")
        val tehsil = intent.getStringExtra("TEHSIL")
        val panchayat = intent.getStringExtra("PANCHAYAT")

        // Get references to the EditText fields
        val lat1 = findViewById<EditText>(R.id.editTextText1)
        val lat2 = findViewById<EditText>(R.id.editTextText2)
        val long1 = findViewById<EditText>(R.id.editTextText3)
        val long2 = findViewById<EditText>(R.id.editTextText8)
        val lat3 = findViewById<EditText>(R.id.editTextText7)
        val long3 = findViewById<EditText>(R.id.editTextText6)
        val lat4 = findViewById<EditText>(R.id.editTextText5)
        val long4 = findViewById<EditText>(R.id.editTextText4)
        val year1 = findViewById<EditText>(R.id.editTextNumber)
        val year2 = findViewById<EditText>(R.id.editTextNumber2)
        val year3 = findViewById<EditText>(R.id.editTextNumber3)
        val year4 = findViewById<EditText>(R.id.editTextNumber4)
        val year5 = findViewById<EditText>(R.id.editTextNumber5)

        // Get reference to the Button
        val button = findViewById<Button>(R.id.button4)

        // Set an OnClickListener on the Button
        button.setOnClickListener {
            // Fetch the strings from the EditText fields
            val lat1Text = lat1.text.toString()
            val lat2Text = lat2.text.toString()
            val long1Text = long1.text.toString()
            val long2Text = long2.text.toString()
            val lat3Text = lat3.text.toString()
            val long3Text = long3.text.toString()
            val lat4Text = lat4.text.toString()
            val long4Text = long4.text.toString()
            val year1Text = year1.text.toString()
            val year2Text = year2.text.toString()
            val year3Text = year3.text.toString()
            val year4Text = year4.text.toString()
            val year5Text = year5.text.toString()

            // Create an Intent to start the PostFinal activity
            val intent = Intent(this, postFinal::class.java)

            // Put the previous and current fetched strings as extras in the Intent
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
            intent.putExtra("LAT_1", lat1Text)
            intent.putExtra("LAT_2", lat2Text)
            intent.putExtra("LONG_1", long1Text)
            intent.putExtra("LONG_2", long2Text)
            intent.putExtra("LAT_3", lat3Text)
            intent.putExtra("LONG_3", long3Text)
            intent.putExtra("LAT_4", lat4Text)
            intent.putExtra("LONG_4", long4Text)
            intent.putExtra("YEAR_1_RS", year1Text)
            intent.putExtra("YEAR_2_RS", year2Text)
            intent.putExtra("YEAR_3_RS", year3Text)
            intent.putExtra("YEAR_4_RS", year4Text)
            intent.putExtra("YEAR_5_RS", year5Text)

            // Start the PostFinal activity
            startActivity(intent)
        }
    }
}
