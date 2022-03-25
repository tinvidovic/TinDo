package com.loyaltiez.core.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.loyaltiez.core.data.roomdb_converters.RoomConverters
import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.core.domain.repository.IToDoDAO

// Abstract class which when invoked provides a singleton instance of the RoomDatabase (thread safe)
@Database(
    entities = [ToDo::class],
    version = 3
)
@TypeConverters(RoomConverters::class)
abstract class TindoRoomDatabase : RoomDatabase() {

    abstract fun toDoDAO(): IToDoDAO

    companion object {
        @Volatile
        private var instance: TindoRoomDatabase? = null

        // The lock used to provide thread safety
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                TindoRoomDatabase::class.java,
                "tindo_db.db"
            )
                .addMigrations(MIGRATION_2_3)
                .build()
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE to_dos ADD COLUMN favourite INTEGER DEFAULT 0 NOT NULL")
    }
}