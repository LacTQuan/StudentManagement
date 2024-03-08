package com.example.studentmanagement.models
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class Student(var name: String?, var className: String?, var dob: String?, var gender: String?) : RealmObject {
    @PrimaryKey
    var studentId: String = UUID.randomUUID().toString()
    constructor() : this(null, null, null, null)
}