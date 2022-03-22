package com.loyaltiez.core.presentation.view_models.input_states

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyaltiez.core.R

class PasswordInputState(initialValue: String) {

    companion object Constants {

        const val MINIMUM_PASSWORD_LENGTH = 1
    }

    private val mInput = MutableLiveData<String>()
    val input: LiveData<String>
        get() = mInput

    private val mFormattedValue = MutableLiveData<String>()
    val formattedValue: LiveData<String>
        get() = mFormattedValue

    private val mError = MutableLiveData<Int?>()
    val error: LiveData<Int?>
        get() = mError

    init {

        mInput.value = initialValue

        mFormattedValue.value = initialValue
    }

    fun set(input: String?){

        mInput.value = input?:""

        mFormattedValue.value = (mInput.value?:"")

        clearError()
    }

    fun isValid(): Boolean {

        // Check if the input is of appropriate length
        return mFormattedValue.value?.let {
            it.length >= MINIMUM_PASSWORD_LENGTH
        } == true
    }

    fun setErrors() {

        if (!isValid())
            mError.value = R.plurals.password_too_short_error
    }

    fun getError(context: Context): String? {

        return mError.value?.let {
            context.resources.getQuantityString(it,
            MINIMUM_PASSWORD_LENGTH,
            MINIMUM_PASSWORD_LENGTH
        ) }
    }

    fun clearError() {

        mError.value = null
    }
}