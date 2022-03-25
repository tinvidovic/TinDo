package com.loyaltiez.feature_home.domain.use_cases

import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.core.domain.repository.IToDoDAO
import kotlinx.coroutines.flow.Flow

class GetToDosForUserEmailUseCase(private val toDoDAO: IToDoDAO) {

    operator fun invoke(userEmail: String): Flow<List<ToDo>> =
        toDoDAO.getToDosForUserEmail(userEmail)

}