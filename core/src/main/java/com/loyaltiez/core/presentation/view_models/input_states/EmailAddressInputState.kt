package com.loyaltiez.core.presentation.view_models.input_states

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyaltiez.core.R

class EmailAddressInputState(initialValue: String) {

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

        mFormattedValue.value = initialValue.trim()
    }

    fun set(input: String?){

        mInput.value = input?:""

        mFormattedValue.value = (mInput.value?:"").trim()

        clearError()
    }

    fun isValid(): Boolean {

        // Check if the input matches the e-mail pattern
        return Patterns.EMAIL_ADDRESS.matcher(mFormattedValue.value.toString()).matches()
    }

    fun setErrors() {

        if (!isValid())
            mError.value = R.string.invalid_email_error
    }

    fun getError(context: Context): String? {

        return mError.value?.let { context.getString(it) }
    }

    fun clearError() {

        mError.value = null
    }

    fun setError(errorResourceId: Int) {

        mError.value = errorResourceId
    }
}