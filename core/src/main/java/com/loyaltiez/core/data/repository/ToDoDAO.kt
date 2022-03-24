package com.loyaltiez.core.data.repository

import com.loyaltiez.core.data.data_source.TindoRoomDatabase
import com.loyaltiez.core.domain.model.todo.DailyToDo
import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.core.domain.model.todo.WeeklyToDo
import com.loyaltiez.core.domain.repository.IToDoDAO
import kotlinx.coroutines.flow.Flow

class ToDoDAO(private val db: TindoRoomDatabase) : IToDoDAO {

    override suspend fun insertToDo(toDo: ToDo): Long {

        return db.toDoDAO().insertToDo(toDo)
    }

    override fun getToDosForUserEmail(userEmail: String): Flow<List<ToDo>> {

        return db.toDoDAO().getToDosForUserEmail(userEmail)
    }

    override fun getDailyToDosForUserEmail(userEmail: String): Flow<List<DailyToDo>> {

        return db.toDoDAO().getDailyToDosForUserEmail(userEmail)
    }

    override fun getWeeklyToDosForUserEmail(userEmail: String): Flow<List<WeeklyToDo>> {

        return db.toDoDAO().getWeeklyToDosForUserEmail(userEmail)
    }

    override suspend fun deleteToDo(toDo: ToDo) {

        return db.toDoDAO().deleteToDo(toDo)
    }

}