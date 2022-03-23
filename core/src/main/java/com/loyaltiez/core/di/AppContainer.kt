package com.loyaltiez.core.di

import com.loyaltiez.core.domain.model.user.User

open class AppContainer(var loggedInUser: User? = null) {
}