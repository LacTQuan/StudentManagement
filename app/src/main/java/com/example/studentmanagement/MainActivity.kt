package com.example.studentmanagement

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentmanagement.models.Student

class MainActivity : ComponentActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var studentAdapter: StudentAdapter
    var isLinearLayout: Boolean = true

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.students)

        // List view
        Global.students = Repo.getInstance(this).loadStudents()
        studentAdapter = StudentAdapter(this, ArrayList(Global.students), object : StudentAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@MainActivity, StudentInformation::class.java)
                intent.putExtra("selectedStudent", position)
                resultLauncher.launch(intent)
            }
        })
        val studentRV = findViewById<RecyclerView>(R.id.studentList)
        studentRV!!.adapter = studentAdapter
        studentRV.layoutManager = LinearLayoutManager(this)

        val itemDecoration = DividerItemDecoration(studentRV.context, LinearLayoutManager(this).orientation)
        studentRV.addItemDecoration(itemDecoration)

        // Add button
        val addButton = findViewById<ImageButton>(R.id.addBtn)

        addButton.setOnClickListener {
            val intent = Intent(this, StudentAdding::class.java)
            resultLauncher.launch(intent)
        }

        // Auto complete text
        val autoComplete = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        autoComplete.setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, Global.students.map { it.name }))
        autoComplete.threshold = 1
        autoComplete.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (autoComplete.text.isEmpty()) {
                    studentAdapter.updateData(ArrayList(Global.students))
                }
            }
        })

        autoComplete.setOnItemClickListener { _, _, position, _ ->
            val filteredList = Global.students.filter { it.name!!.contains(autoComplete.text.toString()) }
            Log.d("Selected", Global.students[0].toString())
            studentAdapter.updateData(filteredList as ArrayList<Student>)
        }

        // Change layout button
        val changeLayoutButton = findViewById<ImageButton>(R.id.changeLayoutBtn)
        changeLayoutButton.setOnClickListener {
            if (isLinearLayout) {
                studentRV.layoutManager = GridLayoutManager(this, 2)
            } else {
                studentRV.layoutManager = LinearLayoutManager(this)
            }

            isLinearLayout = !isLinearLayout
            studentRV.recycledViewPool.clear()
            studentRV.adapter?.notifyDataSetChanged()
        }

        // Activity result
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val message = data?.getStringExtra("state")

                // print out the type of message
//                Log.d("Type", message.toString())

                if (message == "update") {
                    val idx = intent.getIntExtra("idx", 0)
                    Repo.getInstance(this).updateStudent(Global.students[idx])
                    studentAdapter.updateData(ArrayList(Global.students))
                } else if (message == "delete") {
                    val idx = intent.getIntExtra("idx", 0)
                    Repo.getInstance(this).deleteStudent(Global.students[idx])
                    Global.students.removeAt(idx)
                    studentAdapter.updateData(ArrayList(Global.students))
                } else {
                    Repo.getInstance(this).addStudent(Global.students.last())
                    studentAdapter.updateData(ArrayList(Global.students))
                }
            }
        }
    }
}
