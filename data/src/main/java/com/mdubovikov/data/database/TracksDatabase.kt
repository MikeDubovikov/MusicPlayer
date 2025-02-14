package com.mdubovikov.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mdubovikov.data.database.dao.TracksDao
import com.mdubovikov.data.database.entity.TrackDb

@Database(entities = [TrackDb::class], version = 1, exportSchema = false)
abstract class TracksDatabase : RoomDatabase() {

    abstract fun downloadsDao(): TracksDao

    companion object {

        private const val DB_NAME = "CoursesDatabase"
        private var INSTANCE: TracksDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): TracksDatabase {
            INSTANCE?.let { return it }

            synchronized(LOCK) {
                INSTANCE?.let { return it }

                val database = Room.databaseBuilder(
                    context = context,
                    klass = TracksDatabase::class.java,
                    name = DB_NAME
                ).build()

                INSTANCE = database
                return database
            }
        }
    }
}