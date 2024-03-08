package com.example.studentmanagement

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.studentmanagement.models.Student
import org.jsoup.Jsoup
import java.io.File

class StudentWebView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_web_view)
        val student = Global.students[intent.getIntExtra("selectedStudent", -1)]
        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()
        Log.d("StudentWebView", "Student: $student")
        val htmlFile = Jsoup.parse(assets.open("student.html"), "UTF-8", "file:///android_asset/")
        val studentDiv = htmlFile.getElementById("student")
        studentDiv?.append("<p>Name: ${student.name}</p>")
        studentDiv?.append("<p>Class: ${student.className}</p>")
        studentDiv?.append("<p>Birthday: ${student.dob}</p>")
        studentDiv?.append("<p>Gender: ${student.gender}</p>")

        webView.loadData(htmlFile.html(), "text/html", "UTF-8")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}