package com.example.studentmanagement

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentmanagement.models.Student

class StudentAdapter(private val context: Context, private val students: ArrayList<Student>, private val listener: OnItemClickListener) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.itemName)
        val className: TextView = itemView.findViewById(R.id.itemClassName)
        val other: TextView = itemView.findViewById(R.id.itemOther)
        val avatar: View = itemView.findViewById(R.id.itemAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_list_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return students.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, postition: Int) {
        val student = students[postition]
        holder.name.text = student.name
        holder.className.text = student.className
        holder.other.text = student.dob + " - " + student.gender
        holder.avatar.setBackgroundResource(R.drawable.avatar)

        holder.itemView.setOnClickListener {
            listener.onItemClick(postition)
        }

        // set the layout
        val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
        if (holder.itemView.context is MainActivity) {
            val mainActivity = holder.itemView.context as MainActivity
            if (mainActivity.isLinearLayout) {
                layoutParams.width = RecyclerView.LayoutParams.MATCH_PARENT
                layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT
            } else {
                val displayMetrics = mainActivity.resources.displayMetrics
                val width = displayMetrics.widthPixels / 2
                layoutParams.width = width
                layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT
            }
            holder.itemView.layoutParams = layoutParams
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newStudents: ArrayList<Student>) {
        Log.d("StudentAdapter", "updateData: $newStudents")
        students.clear()
        students.addAll(newStudents)
        Log.d("StudentAdapter", "updateData: $students")
        notifyDataSetChanged()
    }

}