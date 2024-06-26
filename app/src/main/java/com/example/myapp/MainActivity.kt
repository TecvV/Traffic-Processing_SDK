//package com.example.myapp
//
//import android.os.Bundle
//import android.view.View
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.Button
//import android.widget.Spinner
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.database.*
//
//class MainActivity : AppCompatActivity() {
//    private lateinit var spinners: List<Spinner>
//    private lateinit var rootNode: FirebaseDatabase
//    private lateinit var reference: DatabaseReference
//    private lateinit var submitButton: Button
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        // Initialize Firebase database
//        rootNode = FirebaseDatabase.getInstance()
//        reference = rootNode.getReference("States")
//
//        // Initialize the spinners
//        spinners = listOf(
//            findViewById(R.id.spinner),
//            findViewById(R.id.spinner2),
//            findViewById(R.id.spinner3),
//            findViewById(R.id.spinner4),
//            findViewById(R.id.spinner5),
//            findViewById(R.id.spinner6),
//            findViewById(R.id.spinner7),
//            findViewById(R.id.spinner8)
//        )
//
//        // Initialize the submit button
//        submitButton = findViewById(R.id.buttonnn)
//
//        // Set up the first spinner
//        setupSpinner(spinners[0], reference)
//
//        // Set up dependent spinners
//        for (i in 1 until spinners.size) {
//            setupDependentSpinner(spinners[i], i)
//        }
//
//        // Set up the submit button click listener
//        submitButton.setOnClickListener {
//            if (areAllSpinnersValid()) {
//                val selectedItems = getSelectedItems()
//                Toast.makeText(this, "All spinners are valid! Selected items: $selectedItems", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Please make sure all fields have selected items.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun setupSpinner(spinner: Spinner, ref: DatabaseReference) {
//        ref.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val items = ArrayList<String>()
//                for (dataSnapshot in snapshot.children) {
//                    items.add(dataSnapshot.key.toString())
//                }
//
//                if (items.isEmpty()) {
//                    Toast.makeText(this@MainActivity, "No options available for this selection.", Toast.LENGTH_SHORT).show()
//                }
//
//                val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, items)
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                spinner.adapter = adapter
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@MainActivity, "Failed to load data.", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    private fun setupDependentSpinner(spinner: Spinner, spinnerIndex: Int) {
//        val previousSpinner = spinners[spinnerIndex - 1]
//        previousSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                val selectedItem = parent.getItemAtPosition(position)?.toString()
//                if (selectedItem == null) {
//                    Toast.makeText(this@MainActivity, "No option selected.", Toast.LENGTH_SHORT).show()
//                    return
//                }
//
//                val selectedItems = spinners.subList(0, spinnerIndex).mapNotNull { it.selectedItem?.toString() }
//                if (selectedItems.size == spinnerIndex) {
//                    val ref = referenceForSelectedItems(selectedItems)
//                    setupSpinner(spinner, ref)
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // No action needed
//                Toast.makeText(this@MainActivity, "No option selected.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun referenceForSelectedItems(selectedItems: List<String>): DatabaseReference {
//        var ref = reference
//        for (item in selectedItems) {
//            ref = ref.child(item)
//        }
//        return ref
//    }
//
//    private fun areAllSpinnersValid(): Boolean {
//        for (spinner in spinners) {
//            if (spinner.adapter == null || spinner.adapter.count == 0) {
//                // Spinner has no options available
//                return false
//            }
//            if (spinner.selectedItem == null) {
//                // No option is selected in the spinner
//                return false
//            }
//        }
//        return true
//    }
//
//    private fun getSelectedItems(): ArrayList<String> {
//        val selectedItems = ArrayList<String>()
//        for (spinner in spinners) {
//            spinner.selectedItem?.let {
//                selectedItems.add(it.toString())
//            }
//        }
//        return selectedItems
//    }
//}







package com.example.myapp

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
//import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.android.animation.SegmentType
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.firebase.database.*
//import com.itextpdf.kernel.pdf.PdfWriter
//import com.itextpdf.layout.Document
//import com.itextpdf.layout.element.Paragraph
import java.io.File
import java.io.FileOutputStream
import java.util.TreeMap

interface OnDataLoadedCallback {
    fun onDataLoaded(items: ArrayList<String>)
}


class MainActivity : AppCompatActivity() {
    private lateinit var spinners: List<Spinner>
    private lateinit var rootNode: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var submitButton: Button
    private lateinit var newReference: DatabaseReference
    private lateinit var extra : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Request storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        // Initialize Firebase database

        rootNode = FirebaseDatabase.getInstance()
//        reference = rootNode.getReference("Sheet1")
        reference = rootNode.getReference("TransformedSheet1")

        // Traverse all children of the reference and transform data
//        traverseAndTransformData(reference)

        // Initialize the spinners
        spinners = listOf(
            findViewById(R.id.spinner),
            findViewById(R.id.spinner2),
            findViewById(R.id.spinner3),
            findViewById(R.id.spinner4),
            findViewById(R.id.spinner5),
            findViewById(R.id.spinner6),
            findViewById(R.id.spinner7),
            findViewById(R.id.spinner8)
        )

        // Initialize the submit button
        submitButton = findViewById(R.id.buttonnn)

        // Set up the first spinner
        setupSpinner(spinners[0], reference)

        // Set up dependent spinners
        for (i in 1 until spinners.size) {
            setupDependentSpinner(spinners[i], i)
        }

        // Set up the submit button click listener
        submitButton.setOnClickListener {
            if (areAllSpinnersValid()) {
                val selectedItems = getSelectedItems()
                val rref = referenceForSelectedItems(selectedItems)
                val NewItems = getNewItems()
                addFinal(NewItems, rref, object : OnDataLoadedCallback {
                    override fun onDataLoaded(items: ArrayList<String>) {
                        // Combine the items with the selected items if needed
                        NewItems.addAll(items)
//                        Toast.makeText(this@MainActivity, NewItems.toString(), Toast.LENGTH_LONG).show()

                        // Now you can use the selectedItems list for further processing
                        createPdf(NewItems)

                        // Optionally, display the items in a Toast
//                        val itemsString = items.joinToString(", ")

                    }
                })
//                Toast.makeText(this, "PDF created successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please make sure all fields have selected items.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun traverseAndTransformData(reference: DatabaseReference) {
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val river = childSnapshot.child("RIVER").value.toString()
                    val state = childSnapshot.child("State").value.toString()
                    val landscape = childSnapshot.child("Landscape").value.toString()
                    val siteName = childSnapshot.child("SITE NAME").value.toString()
                    val forestDivision = childSnapshot.child("FOREST Division").value.toString()
                    val forestRange = childSnapshot.child("FOREST RANGE").value.toString()
                    val blockName = childSnapshot.child("BLOCK NAME").value.toString()
                    val forestBeat = childSnapshot.child("FOREST BEAT").value.toString()

                    val transformedData = mutableMapOf<String, Any>()
                    for (grandChildSnapshot in childSnapshot.children) {
                        if (grandChildSnapshot.key !in listOf("RIVER", "State", "Landscape", "SITE NAME", "FOREST Division", "FOREST RANGE", "BLOCK NAME", "FOREST BEAT")) {
                            transformedData[grandChildSnapshot.key!!] = grandChildSnapshot.value!!
                        }
                    }

                    // Define the path in the new structure
                    val path = "$river/$state/$landscape/$forestDivision/$forestRange/$siteName/$blockName/$forestBeat"
                    newReference.child(path).setValue(transformedData)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("FirebaseUpload", "Data uploaded successfully to $path")
                            } else {
                                Log.e("FirebaseUpload", "Error uploading data to $path", task.exception)
                            }
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("FirebaseDatabase", "Error reading data", databaseError.toException())
            }
        })
    }



    private fun addFinal(selectedItems: ArrayList<String>, rref: DatabaseReference, callback: OnDataLoadedCallback) {
        val items = ArrayList<String>()
        val coordinates = mutableMapOf<Int, Pair<String?, String?>>()

        // Initialize the map with null values for each coordinate pair
        for (i in 1..4) {
            coordinates[i] = Pair(null, null)
        }

        rref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Iterate through the snapshot children
                for (dataSnapshot in snapshot.children) {
                    val key = dataSnapshot.key.toString()
                    val value = dataSnapshot.value.toString()

                    // Check if the key matches any of the coordinates to be removed
                    when (key) {
                        "Longitude1 (DD)" -> coordinates[1]?.let { coordinates[1] = it.copy(first = value) }
                        "Latitude1 (DD)" -> coordinates[1]?.let { coordinates[1] = it.copy(second = value) }
                        "Longitude2 (DD)" -> coordinates[2]?.let { coordinates[2] = it.copy(first = value) }
                        "Latitude2 (DD)" -> coordinates[2]?.let { coordinates[2] = it.copy(second = value) }
                        "Longitude3 (DD)" -> coordinates[3]?.let { coordinates[3] = it.copy(first = value) }
                        "Latitude3 (DD)" -> coordinates[3]?.let { coordinates[3] = it.copy(second = value) }
                        "Longitude4 (DD)" -> coordinates[4]?.let { coordinates[4] = it.copy(first = value) }
                        "Latitude4 (DD)" -> coordinates[4]?.let { coordinates[4] = it.copy(second = value) }
                        else -> {
                            // Add other items to the list
                            if("$key" == "AREA") items.add("$key (ha) - $value")
                            else items.add("$key - $value")
                        }
                    }
                }

                // Add the new combined entities for each coordinate pair
                for (i in 1..4) {
                    val (longitude, latitude) = coordinates[i]!!
                    if (longitude != null && latitude != null) {
                        items.add("GPS$i - $longitude E $latitude N")
                    }
                }

                // Call the callback with the loaded data
                callback.onDataLoaded(items)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load data.", Toast.LENGTH_SHORT).show()
            }
        })
    }




    private fun setupSpinner(spinner: Spinner, ref: DatabaseReference) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = ArrayList<String>()
                for (dataSnapshot in snapshot.children) {
                    items.add(dataSnapshot.key.toString())
                }

                if (items.isEmpty()) {
                    Toast.makeText(this@MainActivity, "No options available for this selection.", Toast.LENGTH_SHORT).show()
                }

                val adapter = ArrayAdapter(this@MainActivity, R.layout.colors, items)
                adapter.setDropDownViewResource(R.layout.drop)
                spinner.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load data.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupDependentSpinner(spinner: Spinner, spinnerIndex: Int) {
        val previousSpinner = spinners[spinnerIndex - 1]
        previousSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position)?.toString()
                if (selectedItem == null) {
                    Toast.makeText(this@MainActivity, "No option selected.", Toast.LENGTH_SHORT).show()
                    return
                }

                val selectedItems = spinners.subList(0, spinnerIndex).mapNotNull { it.selectedItem?.toString() }
                if (selectedItems.size == spinnerIndex) {
                    val ref = referenceForSelectedItems(selectedItems)
                    setupSpinner(spinner, ref)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
                Toast.makeText(this@MainActivity, "No option selected.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun referenceForSelectedItems(selectedItems: List<String>): DatabaseReference {
        var ref = reference
        for (item in selectedItems) {
            ref = ref.child(item)
        }
        return ref
    }

    private fun areAllSpinnersValid(): Boolean {
        for (spinner in spinners) {
            if (spinner.adapter == null || spinner.adapter.count == 0) {
                // Spinner has no options available
                return false
            }
            if (spinner.selectedItem == null) {
                // No option is selected in the spinner
                return false
            }
        }
        return true
    }

    private fun getSelectedItems(): ArrayList<String> {
        val selectedItems = ArrayList<String>()
        var ct = 1
        for (spinner in spinners) {
            spinner.selectedItem?.let {
                selectedItems.add(it.toString())
            }

        }
        return selectedItems
    }

    private fun getNewItems(): ArrayList<String> {
        val selectedItems = ArrayList<String>()
        var ct = 1
        for (spinner in spinners) {
            spinner.selectedItem?.let {
//                selectedItems.add(it.toString())
                if(ct == 1) {
                    selectedItems.add("River - " + it.toString())
                } else if(ct == 2) {
                    selectedItems.add("State - " + it.toString())
                } else if(ct == 3) {
                    selectedItems.add("Landscape - " + it.toString())
                } else if(ct == 4) {
                    selectedItems.add("Forest Division - " + it.toString())
                } else if(ct == 5) {
                    selectedItems.add("Forest Range - " + it.toString())
                } else if(ct == 6) {
                    selectedItems.add("Site Name - " + it.toString())
                } else if(ct == 7) {
                    selectedItems.add("Forest Block - " + it.toString())
                } else {
                    selectedItems.add("Forest Beat - " + it.toString())
                }
                ct = ct + 1
            }

        }
        return selectedItems
    }

    private fun createPdf(selectedItems: ArrayList<String>) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 1000, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint().apply {
            textSize = 20F
            textAlign = Paint.Align.CENTER
        }
        val xPos = (pageInfo.pageWidth / 2).toFloat()
        var yPos = 50F
        var text = selectedItems[1] + " - Final Report"

        canvas.drawText(text, xPos, yPos, paint)
        val fontMetrics = paint.fontMetrics
        val textHeight = fontMetrics.descent - fontMetrics.ascent
        yPos += textHeight + fontMetrics.leading

        val textPaint = TextPaint().apply {
            isAntiAlias = true
            textSize = 16f
        }

        selectedItems.forEach { item ->
            val staticLayout = StaticLayout.Builder.obtain(item, 0, item.length, textPaint, canvas.width - 20)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(1.0F, 1.0F)
                .setIncludePad(false)
                .build()
            canvas.save()
            val textX = (canvas.width - staticLayout.width) / 2f
            val textY = yPos

            canvas.translate(textX, textY)
            staticLayout.draw(canvas)
            canvas.restore()

            yPos += staticLayout.height + 10f
        }

        pdfDocument.finishPage(page)

        try {
            val fileName = selectedItems[1] + " - Final Report.pdf"
            val pdfUri = savePdfToDownloads(pdfDocument, fileName)
            Toast.makeText(this@MainActivity, "Report Successfully transferred to your downloads folder", Toast.LENGTH_SHORT).show()

            // Open the PDF immediately
            openPdf(pdfUri)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this@MainActivity, "Error creating PDF: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            pdfDocument.close()
        }
    }

    private fun savePdfToDownloads(pdfDocument: PdfDocument, fileName: String): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        } else {
            // For SDK versions less than Q, save directly to the Downloads directory
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)
            val fileUri = FileProvider.getUriForFile(this@MainActivity, "${BuildConfig.APPLICATION_ID}.provider", file)
            file.outputStream().use { outputStream ->
                pdfDocument.writeTo(outputStream)
            }
            fileUri
        }

        if (uri != null) {
            contentResolver.openOutputStream(uri).use { outputStream ->
                if (outputStream != null) {
                    pdfDocument.writeTo(outputStream)
                } else {
                    throw Exception("Failed to open output stream")
                }
            }
        } else {
            throw Exception("Failed to create new MediaStore record.")
        }

        return uri
    }

    private fun openPdf(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
        }
        startActivity(intent)
    }



}
