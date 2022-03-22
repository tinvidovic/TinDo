package com.loyaltiez.core.presentation.view_models.input_states

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyaltiez.core.R
import com.loyaltiez.core.helper.converters.convertTimeToString
import java.sql.Time

class RemindMeAtState(initialValue: Time?, val context: Context) {

    private val mInput = MutableLiveData<Time?>()
    val input: LiveData<Time?>
        get() = mInput

    private val mFormattedValue = MutableLiveData<String>()
    val formattedValue: LiveData<String>
        get() = mFormattedValue

    private val mError = MutableLiveData<Int?>()
    val error: LiveData<Int?>
        get() = mError

    init {

        mInput.value = initialValue

        if (initialValue == null)
            mFormattedValue.value = ""
        else
            mFormattedValue.value = convertTimeToString(mInput.value)
    }

    fun set(input: Time?) {

        mInput.value = input

        if (input == null)
            mFormattedValue.value = ""
        else
            mFormattedValue.value = convertTimeToString(mInput.value)

        clearError()
    }

    fun isValid() : Boolean {

        return mInput.value != null
    }

    fun setErrors() {

        if (!isValid())
            mError.value = R.string.remind_me_at_error
    }

    fun getError(context: Context): String? {

        return mError.value?.let { context.getString(it) }
    }

    fun clearError() {

        mError.value = null
    }
}