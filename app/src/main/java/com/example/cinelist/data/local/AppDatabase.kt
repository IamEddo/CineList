package com.example.cinelist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Watchlist::class, MovieEntry::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cineListDao(): CineListDao
}