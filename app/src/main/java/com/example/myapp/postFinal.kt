package com.example.myapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.firebase.database.*
import jxl.Workbook
import jxl.WorkbookSettings
import jxl.write.Label
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook
import jxl.write.WriteException
import java.io.File
import java.io.IOException
import java.util.Locale

class postFinal : AppCompatActivity() {
    private lateinit var rootNode: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private val PERMISSION_REQUEST_CODE = 1

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_final)

        // Request permissions
        if (!checkPermissions()) {
            requestPermissions()
        }

        // Retrieve the data passed from Final activity
        val riverName = intent.getStringExtra("RIVER_NAME") ?: "NULL"
        val landscapeType = intent.getStringExtra("LANDSCAPE_TYPE") ?: "NULL"
        val state = intent.getStringExtra("STATE") ?: "NULL"
        val beatName = intent.getStringExtra("BEAT_NAME") ?: "NULL"
        val forestBlockName = intent.getStringExtra("FOREST_BLOCK_NAME") ?: "NULL"
        val siteName = intent.getStringExtra("SITE_NAME") ?: "NULL"
        val forestRangeName = intent.getStringExtra("FOREST_RANGE_NAME") ?: "NULL"
        val forestDivisionName = intent.getStringExtra("FOREST_DIVISION_NAME") ?: "NULL"
        val activities = intent.getStringExtra("ACTIVITIES") ?: "NULL"
        val district = intent.getStringExtra("DISTRICT") ?: "NULL"
        val area = intent.getStringExtra("AREA") ?: "NULL"
        val remarks = intent.getStringExtra("REMARKS") ?: "NULL"
        val tehsil = intent.getStringExtra("TEHSIL") ?: "NULL"
        val panchayat = intent.getStringExtra("PANCHAYAT") ?: "NULL"
        val lat1 = intent.getStringExtra("LAT_1") ?: "NULL"
        val lat2 = intent.getStringExtra("LAT_2") ?: "NULL"
        val long1 = intent.getStringExtra("LONG_1") ?: "NULL"
        val long2 = intent.getStringExtra("LONG_2") ?: "NULL"
        val lat3 = intent.getStringExtra("LAT_3") ?: "NULL"
        val long3 = intent.getStringExtra("LONG_3") ?: "NULL"
        val lat4 = intent.getStringExtra("LAT_4") ?: "NULL"
        val long4 = intent.getStringExtra("LONG_4") ?: "NULL"
        val year1Rs = intent.getStringExtra("YEAR_1_RS") ?: "NULL"
        val year2Rs = intent.getStringExtra("YEAR_2_RS") ?: "NULL"
        val year3Rs = intent.getStringExtra("YEAR_3_RS") ?: "NULL"
        val year4Rs = intent.getStringExtra("YEAR_4_RS") ?: "NULL"
        val year5Rs = intent.getStringExtra("YEAR_5_RS") ?: "NULL"

        // Get references to the EditText fields in postFinal layout
        val modelName = findViewById<EditText>(R.id.editTextText1)
        val modelId = findViewById<EditText>(R.id.editTextText3)
        val modelDescription = findViewById<EditText>(R.id.editTextText6)

        // Get reference to the Button
        val button = findViewById<Button>(R.id.button4)

        // Set an OnClickListener on the Button
        button.setOnClickListener {
            // Fetch the strings from the EditText fields
            val modelNameText = modelName.text.toString()
            val modelIdText = modelId.text.toString()
            val modelDescriptionText = modelDescription.text.toString()

            // Create an ArrayList to store all the data
            val dataList = ArrayList<String>()

            // Add the previous data to the ArrayList
            dataList.add(riverName)
            dataList.add(state)
            dataList.add(landscapeType)
            dataList.add(forestDivisionName)
            dataList.add(forestRangeName)
            dataList.add(siteName)
            dataList.add(forestBlockName)
            dataList.add(beatName)
            dataList.add(activities)
            dataList.add(district)
            dataList.add(area)
            dataList.add(remarks)
            dataList.add(tehsil)
            dataList.add(panchayat)
            dataList.add(lat1)
            dataList.add(lat2)
            dataList.add(long1)
            dataList.add(long2)
            dataList.add(lat3)
            dataList.add(long3)
            dataList.add(lat4)
            dataList.add(long4)
            dataList.add(year1Rs)
            dataList.add(year2Rs)
            dataList.add(year3Rs)
            dataList.add(year4Rs)
            dataList.add(year5Rs)

            // Add the new data to the ArrayList
            dataList.add(modelNameText)
            dataList.add(modelIdText)
            dataList.add(modelDescriptionText)

            addToDB(dataList)
            writeToExcel(this@postFinal, dataList)

            Toast.makeText(this@postFinal, "Details uploaded successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissions(): Boolean {
        val writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return writePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
    }

    fun addToDB(arr: ArrayList<String>) {
        rootNode = FirebaseDatabase.getInstance()
        reference = rootNode.getReference("TransformedSheet1")

        val r1 = reference.child(arr[0])

        r1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot1: DataSnapshot) {
                if (snapshot1.exists()) {
                    checkAndProceed(r1, arr, 1)
                } else {
                    createRemaining(r1, arr, 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun checkAndProceed(reference: DatabaseReference, arr: ArrayList<String>, level: Int) {
        if (level > 7) {
            // Once we reach the 8th level, we write the data
            val keys = arrayListOf(
                "ACTIVITIES", "District", "AREA", "REMARKS", "TEHSIL", "PANCHAYAT",
                "LAT_1", "LAT_2", "LONG_1", "LONG_2", "LAT_3", "LONG_3", "LAT_4",
                "LONG_4", "YEAR1 RS(L)", "YEAR2 RS(L)", "YEAR3 RS(L)", "YEAR4 RS(L)",
                "YEAR5 RS(L)", "Model Name", "Model ID", "Model Description"
            )

            for (i in keys.indices) {
                reference.child(keys[i]).setValue(arr[i + 8])
            }
        } else {
            val nextReference = reference.child(arr[level])
            nextReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        checkAndProceed(nextReference, arr, level + 1)
                    } else {
                        createRemaining(nextReference, arr, level + 1)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    fun createRemaining(reference: DatabaseReference, arr: ArrayList<String>, level: Int) {
        var currentReference = reference
        for (i in level..7) {
            currentReference = currentReference.child(arr[i])
        }

        val keys = arrayListOf(
            "ACTIVITIES", "District", "AREA", "REMARKS", "TEHSIL", "PANCHAYAT",
            "LAT_1", "LAT_2", "LONG_1", "LONG_2", "LAT_3", "LONG_3", "LAT_4",
            "LONG_4", "YEAR1 RS(L)", "YEAR2 RS(L)", "YEAR3 RS(L)", "YEAR4 RS(L)",
            "YEAR5 RS(L)", "Model Name", "Model ID", "Model Description"
        )

        for (i in keys.indices) {
            currentReference.child(keys[i]).setValue(arr[i + 8])
        }
    }

    fun writeToExcel(context: Context, dataList: ArrayList<String>) {
        val fileName = "DPR_Data.xls"
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "MyAppFolder")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, fileName)

        try {
            val workbook: WritableWorkbook
            val sheet: WritableSheet

            if (file.exists()) {
                val existingWorkbook = Workbook.getWorkbook(file)
                workbook = Workbook.createWorkbook(file, existingWorkbook)
                sheet = workbook.getSheet(0)
            } else {
                workbook = Workbook.createWorkbook(file)
                val ws = WorkbookSettings()
                ws.locale = Locale.getDefault()
                sheet = workbook.createSheet("Sheet1", 0)

                // Write column headers
                val keys = listOf(
                    "RIVER_NAME", "STATE", "LANDSCAPE_TYPE", "FOREST_DIVISION_NAME", "FOREST_RANGE_NAME", "SITE_NAME",
                    "FOREST_BLOCK_NAME", "BEAT_NAME", "ACTIVITIES", "District", "AREA", "REMARKS", "TEHSIL", "PANCHAYAT",
                    "LAT_1", "LAT_2", "LONG_1", "LONG_2", "LAT_3", "LONG_3", "LAT_4", "LONG_4", "YEAR1 RS(L)", "YEAR2 RS(L)",
                    "YEAR3 RS(L)", "YEAR4 RS(L)", "YEAR5 RS(L)", "Model Name", "Model ID", "Model Description"
                )

                for (i in keys.indices) {
                    val label = Label(i, 0, keys[i])
                    sheet.addCell(label)
                }
            }

            // Write data to the next empty row
            val row = sheet.rows
            for (i in dataList.indices) {
                val label = Label(i, row, dataList[i])
                sheet.addCell(label)
            }

            workbook.write()
            workbook.close()

            // Log the file path
            Log.d("ExcelFile", "File saved at: ${file.absolutePath}")
            Toast.makeText(context, "Details saved to DPR_Data.xls successfully. You can access/delete this file from MS Excel app on your device", Toast.LENGTH_SHORT).show()

            // Open the file
            openFile(context, file)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "IOException: ${e.message}", Toast.LENGTH_SHORT).show()
        } catch (e: WriteException) {
            e.printStackTrace()
            Toast.makeText(context, "WriteException: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun openFile(context: Context, file: File) {
        val uri: Uri = FileProvider.getUriForFile(context, "${context.applicationContext.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/vnd.ms-excel")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val resInfoList = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(intent)
    }

}
