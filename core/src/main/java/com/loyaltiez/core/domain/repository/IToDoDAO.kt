package com.loyaltiez.core.domain.repository

import androidx.room.*
import com.loyaltiez.core.domain.model.todo.DailyToDo
import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.core.domain.model.todo.WeeklyToDo
import kotlinx.coroutines.flow.Flow

@Dao
interface IToDoDAO {

    // Inserts (or updates due to the onConflictStrategy) a todo in the Room Database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDo(toDo: ToDo): Long

    // Gets all todos for the provided userEmail
    @Query("SELECT * from to_dos WHERE userEmail= :userEmail")
    fun getToDosForUserEmail(userEmail: String): Flow<List<ToDo>>

    // Gets all daily todos for the provided userEmail
    @Query("SELECT * from to_dos WHERE userEmail= :userEmail AND date IS NULL")
    fun getDailyToDosForUserEmail(userEmail: String): Flow<List<DailyToDo>>

    // Gets all weekly todos for the provided userEmail
    @Query("SELECT * from to_dos WHERE userEmail= :userEmail AND date IS NOT NULL")
    fun getWeeklyToDosForUserEmail(userEmail: String): Flow<List<WeeklyToDo>>

    // Deletes the provided todoObject from the room database
    @Delete
    suspend fun deleteToDo(toDo: ToDo)

}