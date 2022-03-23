package com.loyaltiez.core.domain.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(private val email: String) : Parcelable {

}