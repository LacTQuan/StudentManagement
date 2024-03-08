package com.example.studentmanagement

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.studentmanagement.models.Student
import kotlin.math.log

class StudentAdding : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_adding)

        // Save button
        val saveButton = findViewById<Button>(R.id.addSaveBtn)
        val drawable = ContextCompat.getDrawable(this, R.drawable.button)

        drawable?.setTint(Color.parseColor("#22B5FF"))
        saveButton.background = drawable

        saveButton.setOnClickListener {
            val name = findViewById<TextView>(R.id.addName).text.toString()
            val className = findViewById<Spinner>(R.id.addClassName).selectedItem.toString()
            val dob = findViewById<TextView>(R.id.addDob).text.toString()
            val genderId = findViewById<RadioGroup>(R.id.addGender).checkedRadioButtonId

            if (name.isEmpty() || className.isEmpty() || dob.isEmpty() || genderId == -1) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val gender = findViewById<Button>(genderId).text.toString()

            Global.students.add(Student(name, className, dob, gender))

            val intent = Intent()
            intent.putExtra("reload", true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        // Spinner
        val spinner = findViewById<Spinner>(R.id.addClassName)

        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, Global.classes)

        spinner.setOnTouchListener() { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                Log.d("Spinner", "Spinner was touched")
                val intent = Intent(this, ClassSelection::class.java)
                resultLauncher.launch(intent)
            }
            true
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val message = data?.getIntExtra("selectedItem", -1)

            Log.d("MainActivity", "result message: $message")
            if (message != null) {
                findViewById<Spinner>(R.id.addClassName).setSelection(message)
            }
        }
    }
}