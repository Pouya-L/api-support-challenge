package com.android.support.exercise.bd.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val image: String
)

//When trying to compile this we get an error related to to the Room Database in Android: An entity must have at least 1 field annotated with @PrimaryKey


/*
@PrimaryKey Annotation:
The @PrimaryKey annotation tells Room that the id field is the primary key for the User table.
A primary key must be unique for each row in the table. In this case, the id field is used to uniquely identify each user.

Why id?:
The id field is typically used as a primary key because it is unique for each user. If your API or database already provides a unique id for each user, it makes sense to use it as the primary key.

Auto-Generated Primary Key:
@PrimaryKey(autoGenerate = true) val id: Int
This is useful if you are inserting data locally and want Room to handle the uniqueness of the id.
 */
