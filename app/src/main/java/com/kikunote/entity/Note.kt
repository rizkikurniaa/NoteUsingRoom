package com.kikunote.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//Entity annotation to specify the table's name
@Entity(tableName = "notes")
//Parcelable annotation to make parcelable object
@Parcelize
data class Note(
    //PrimaryKey annotation to declare primary key with auto increment value
    //ColumnInfo annotation to specify the column's name
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "label") var label: String = "",
    @ColumnInfo(name = "date") var date: String = "",
    @ColumnInfo(name = "time") var time: String = "",
    @ColumnInfo(name = "updatedDate") var updatedDate: String = "",
    @ColumnInfo(name = "updatedTime") var updatedTime: String = "",
    @ColumnInfo(name = "body") var body: String = ""
) : Parcelable
