package com.example.studentmanagement

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.studentmanagement.models.Student
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import java.io.IOException

class Repo private constructor(private val context: Context) {

    private val REALM_NAME = "student.realm"
    lateinit var realm : Realm

    fun openDatabase(){
        val configuration = RealmConfiguration.Builder(
            schema = setOf(Student::class)
        ).name(REALM_NAME)
            .deleteRealmIfMigrationNeeded()
            .build()

        realm = Realm.open(configuration)
        Log.i("hdlog",realm.configuration.path)
    }

    fun loadStudents(): ArrayList<Student> {
        val students = ArrayList<Student>()
        try {
            val all = realm.query<Student>().find()
            Log.i("hdlog", "loadStudents: ${all.size}")
            Log.i("hdlog", "loadStudents: ${all}")
//            students.addAll(all)
            return students
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return students
    }

    fun addStudent(student: Student) {
        try {
            realm.writeBlocking {
                this.copyToRealm(student)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun updateStudent(student: Student) {
        try {
            realm.writeBlocking{
                val studentToUpdate = query<Student>("studentId == $0", student.studentId).find().first()
                studentToUpdate.name = student.name
                studentToUpdate.className = student.className
                studentToUpdate.dob = student.dob
                studentToUpdate.gender = student.gender
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun deleteStudent(student: Student) {
        try {
            realm.writeBlocking {
                val studentToDelete = query<Student>("studentId == $0", student.studentId).find().first()
                delete(studentToDelete)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: Repo? = null

        fun getInstance(context: Context): Repo {
            return INSTANCE ?: synchronized(this) {
                val instance = Repo(context)
                instance.openDatabase()
                INSTANCE = instance
                instance
            }
        }
    }
}