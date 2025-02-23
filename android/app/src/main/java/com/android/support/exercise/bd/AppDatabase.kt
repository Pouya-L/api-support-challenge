package com.android.support.exercise.bd

import android.content.Context
import androidx.activity.result.launch
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration // Import the Migration class
import androidx.sqlite.db.SupportSQLiteDatabase // Import SupportSQLiteDatabase
import com.android.support.exercise.bd.dao.UserDao
import com.android.support.exercise.bd.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class], version = 2)// Increment the version to 2
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private const val DATABASE_NAME = "user-database" // added this line
        private var INSTANCE: AppDatabase? = null

        // Define the migration from version 1 to 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    // No SQL needed here because the schema itself hasn't changed.
                    // We are only adding the schema export directory.
                }
        }

        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2) // Add the migration here
                    //.fallbackToDestructiveMigration() // Use this if you don't need to preserve data
                    .build()
                INSTANCE = instance
                initDatabase(context)
                // return instance
                instance
            }
        }

        private fun initDatabase(context: Context) {
            CoroutineScope(Dispatchers.IO).launch {
                val database = getDatabase(context)
                if (database.userDao().getUserCount() == 0) {
                    database.userDao().insertUser(
                        User(
                            1,
                            "John Doe",
                            "https://upload.wikimedia.org/wikipedia/commons/thumb/7/76/Barack_Obama_profile.jpg/446px-Barack_Obama_profile.jpg"
                        )
                    )
                }
            }
        }

        /*Database Operations on the Main Thread: The initDatabase() function is performing database operations (checking the user count and inserting a user) on the main thread.
        This is a major issue because database operations can take time, and if they happen on the main thread, they will freeze your UI, making your app unresponsive
        private fun initDatabase(context: Context) {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE =
                        Room.databaseBuilder(context, AppDatabase::class.java, "user-database")
                            .build()

                    if (INSTANCE!!.userDao().getUserCount() == 0) {
                        INSTANCE!!.userDao().insertUser(
                            User(
                                1,
                                "John Doe",
                                "https://upload.wikimedia.org/wikipedia/commons/thumb/7/76/Barack_Obama_profile.jpg/446px-Barack_Obama_profile.jpg"
                            )
                        )
                    }
                }
            }
        }

         */
    }
}