package com.loyaltiez.core.domain.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// The class modeling the current user, currently only stores the user's e-mail to differentiate between the users
@Parcelize
data class User(val email: String) : Parcelable {

}