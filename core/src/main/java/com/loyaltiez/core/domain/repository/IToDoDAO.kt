package com.loyaltiez.core.domain.repository

import androidx.room.*
import com.loyaltiez.core.domain.model.todo.DailyToDo
import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.core.domain.model.todo.WeeklyToDo
import kotlinx.coroutines.flow.Flow

@Dao
interface IToDoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDo(toDo: ToDo) : Long

    @Query("SELECT * from to_dos WHERE userEmail= :userEmail")
    fun getToDosForUserEmail(userEmail: String) : Flow<List<ToDo>>

    @Query("SELECT * from to_dos WHERE userEmail= :userEmail AND date IS NULL")
    fun getDailyToDosForUserEmail(userEmail: String) : Flow<List<DailyToDo>>

    @Query("SELECT * from to_dos WHERE userEmail= :userEmail AND date IS NOT NULL")
    fun getWeeklyToDosForUserEmail(userEmail: String) : Flow<List<WeeklyToDo>>

    @Delete
    suspend fun deleteToDo(toDo: ToDo)

}