package com.example.studentmanagement

import android.annotation.SuppressLint
import android.content.Context
import com.example.studentmanagement.models.Student
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class Repo private constructor(private val context: Context) {
    fun loadStudents(): ArrayList<Student> {
        val students = ArrayList<Student>()
        try {
            val fis: FileInputStream = context.openFileInput("students.json")
            val br = fis.bufferedReader()

            // array of students
            val content = JSONArray(br.use { it.readText() })
            for (i in 0 until content.length()) {
                val student = content.getJSONObject(i)
                students.add(Student(student.getString("name"), student.getString("className"), student.getString("dob"), student.getString("gender")))
            }

            fis.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return students
    }

    fun saveStudent() {
        try {
            val jsonArr = JSONArray()
            for (student in Global.students) {
                val jsonObj = JSONObject()
                jsonObj.put("name", student.name)
                jsonObj.put("className", student.className)
                jsonObj.put("dob", student.dob)
                jsonObj.put("gender", student.gender)
                jsonArr.put(jsonObj)
            }

            val fos: FileOutputStream = context.openFileOutput("students.json", Context.MODE_PRIVATE)
            fos.write(jsonArr.toString().toByteArray())
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: Repo? = null

        fun getInstance(context: Context): Repo =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Repo(context).also { INSTANCE = it }
            }
    }
}