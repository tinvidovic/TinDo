package com.loyaltiez.create_edit_todo_core.di

import com.loyaltiez.core.di.AppContainer
import com.loyaltiez.core.domain.repository.IToDoDAO
import com.loyaltiez.core.domain.use_cases.InsertToDoUseCase

class CreateEditToDoActivityContainer(
    toDoDAO: IToDoDAO
) : AppContainer() {

    val insertToDoUseCase = InsertToDoUseCase(toDoDAO)
}