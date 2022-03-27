package com.loyaltiez.feature_home.di

import com.loyaltiez.core.di.AppContainer
import com.loyaltiez.core.domain.repository.IToDoDAO
import com.loyaltiez.core.domain.use_cases.InsertToDoUseCase
import com.loyaltiez.feature_home.domain.use_cases.DeleteToDoUseCase
import com.loyaltiez.feature_home.domain.use_cases.GetToDosForUserEmailUseCase

class HomeActivityContainer(
    toDoDAO: IToDoDAO
) : AppContainer() {

    val getToDosForUserEmailUseCase = GetToDosForUserEmailUseCase(toDoDAO)

    val deleteToDoUseCase = DeleteToDoUseCase(toDoDAO)

    val insertToDoUseCase = InsertToDoUseCase(toDoDAO)
}