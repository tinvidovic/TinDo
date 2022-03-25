package com.loyaltiez.core.domain.use_cases

import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.core.domain.repository.IToDoDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InsertToDoUseCase(private val toDoDAO: IToDoDAO) {

    suspend operator fun invoke(toDo: ToDo) {

        return withContext(Dispatchers.IO) {

            toDoDAO.insertToDo(toDo)
        }
    }
}