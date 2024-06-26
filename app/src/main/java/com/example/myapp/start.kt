package com.example.myapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.appcompat.widget.AppCompatTextView



class start : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var report : Button
    private lateinit var load : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        textView = findViewById(R.id.textView10)

        val label = "A document prepared by construction project teams to record and communicate the day-to-day status of construction activities. DPRs can include information such as completed work, issues, accidents, resource utilization, delays, materials required, and project member details. They are essential tools for construction firms to ensure projects stay on track and within budget. A micro planning tool that can include information such as the project's rationale, summary, user specifications, engineering designs, and technical and financial aspects. DPRs are an essential component of a project and should be prepared carefully, with proper surveys, investigations, and designs carried out before finalization."
        val stringBuilder = StringBuilder()

        Thread{
            for (letter in label){
                stringBuilder.append(letter)
                Thread.sleep(50)
                runOnUiThread {
                    textView.text = stringBuilder.toString()
                }
            }
        }.start()

        report = findViewById(R.id.button)
        load = findViewById(R.id.button2)

        report.setOnClickListener {
            val intent = Intent(this@start, MainActivity::class.java)
            startActivity(intent)
        }

        load.setOnClickListener {
            val intent = Intent(this@start, loading::class.java)
            startActivity(intent)
        }

    }
}