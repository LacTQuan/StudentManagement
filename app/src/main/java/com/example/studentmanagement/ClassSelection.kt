package com.example.studentmanagement

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.content.Intent

class ClassSelection : AppCompatActivity() {
    private var selectedItem: Int? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_selection)

        // List view
        val listView = findViewById<ListView>(R.id.classListView)
        listView.adapter = MyAdapter(this, Global.classes)

        listView.setOnItemClickListener {_, _, position, _ ->
            (listView.adapter as MyAdapter).selectedPosition = position
            (listView.adapter as MyAdapter).notifyDataSetChanged()
            selectedItem = position
        }

        // Save button
        val saveButton = findViewById<TextView>(R.id.classSaveBtn)
        val drawable = ContextCompat.getDrawable(this, R.drawable.button)

        drawable?.setTint(Color.parseColor("#9D21FF"))
        saveButton.background = drawable

        saveButton.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("selectedItem", selectedItem)
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }

    class MyAdapter(private val context: Context, private val classes: ArrayList<String>) : BaseAdapter() {
        var selectedPosition = -1

        override fun getCount(): Int {
            return classes.size
        }

        override fun getItem(position: Int): Any {
            return classes[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("MissingInflatedId")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = convertView ?: inflater.inflate(R.layout.class_list_view_item, parent, false)

            val itemClass = view.findViewById<TextView>(R.id.itemClass)
            itemClass.text = classes[position]

            if (position == selectedPosition) {
                // change the background color of its parent
                val parent = itemClass.parent as View
                parent.setBackgroundColor(Color.parseColor("#B2D1E8"))
            } else {
                val parent = itemClass.parent as View
                parent.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }

            return view
        }
    }
}