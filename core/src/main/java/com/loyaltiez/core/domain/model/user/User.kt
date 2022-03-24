package com.loyaltiez.core.domain.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(val email: String) : Parcelable {

}