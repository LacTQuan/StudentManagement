package com.example.studentmanagement

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.studentmanagement.models.Student

class StudentInformation : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_information)

        // Save button
        val saveButton = findViewById<Button>(R.id.infoSaveBtn)
        val drawable = ContextCompat.getDrawable(this, R.drawable.button)

        drawable?.setTint(Color.parseColor("#22B5FF"))
        saveButton.background = drawable
        saveButton.setOnClickListener {
            val selectedIdx = intent.getIntExtra("selectedStudent", -1)
            val student = Global.students[selectedIdx]
            val name = findViewById<EditText>(R.id.infoName).text.toString()
            val dob = findViewById<EditText>(R.id.infoDob).text.toString()
            val className = findViewById<Spinner>(R.id.infoClassName).selectedItem.toString()
            val genderId = findViewById<RadioGroup>(R.id.infoGender).checkedRadioButtonId
            val gender = findViewById<Button>(genderId).text.toString()

            Global.students[selectedIdx] = Student(name, className, dob, gender)
            val intent = Intent()
            intent.putExtra("reload", true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        // Delete button
        val deleteButton = findViewById<Button>(R.id.infoDeleteBtn)
        val drawable2 = ContextCompat.getDrawable(this, R.drawable.button)
        drawable2?.setTint(Color.parseColor("#FF5421"))
        deleteButton.background = drawable2
        deleteButton.setOnClickListener {
            val selectedIdx = intent.getIntExtra("selectedStudent", -1)
            Global.students.removeAt(selectedIdx)
            val intent = Intent()
            intent.putExtra("reload", true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        // Spinner
        val spinner = findViewById<Spinner>(R.id.infoClassName)

        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Global.classes).also {
            adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Get student information
        val selectedIdx = intent.getIntExtra("selectedStudent", -1)
        val student = Global.students[selectedIdx]
        findViewById<EditText>(R.id.infoName).setText(student?.name)
        findViewById<EditText>(R.id.infoDob).setText(student?.dob)

        val position = Global.classes.indexOf(student?.className)
        findViewById<Spinner>(R.id.infoClassName).setSelection(position)

        val radioGroup = findViewById<RadioGroup>(R.id.infoGender)
        val gender = student?.gender

        for (i in 0 until radioGroup.childCount) {
            val radioButton = radioGroup.getChildAt(i) as RadioButton
            if (radioButton.text == gender) {
                radioButton.isChecked = true
                break
            }
        }
    }
}