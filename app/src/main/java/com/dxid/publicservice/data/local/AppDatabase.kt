package com.dxid.publicservice.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ServiceEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serviceDao(): ServiceDao
}