package com.loyaltiez.core.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.loyaltiez.core.data.roomdb_converters.RoomConverters
import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.core.domain.repository.IToDoDAO

@Database(
    entities = [ToDo::class],
    version = 2
)
@TypeConverters(RoomConverters::class)
abstract class TindoRoomDatabase : RoomDatabase() {

    abstract fun toDoDAO(): IToDoDAO

    companion object {
        @Volatile
        private var instance: TindoRoomDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                TindoRoomDatabase::class.java,
                "tindo_db.db"
            ).fallbackToDestructiveMigration().build()
    }
}