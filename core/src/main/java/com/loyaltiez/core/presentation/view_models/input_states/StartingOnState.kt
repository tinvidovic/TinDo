package com.loyaltiez.core.presentation.view_models.input_states

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyaltiez.core.R
import com.loyaltiez.core.helper.converters.convertDateToString
import com.loyaltiez.core.helper.converters.convertTimeToString
import java.sql.Date
import java.sql.Time

class StartingOnState(initialValue: Date?, val context: Context) {

    private val mInput = MutableLiveData<Date?>()
    val input: LiveData<Date?>
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
            mFormattedValue.value = convertDateToString(mInput.value)
    }

    fun set(input: Date?) {

        mInput.value = input

        if (input == null)
            mFormattedValue.value = ""
        else
            mFormattedValue.value = convertDateToString(mInput.value)

        clearError()
    }

    fun isValid() : Boolean {

        return mInput.value != null
    }

    fun setErrors() {

        if (!isValid())
            mError.value = R.string.starting_on_error
    }

    fun getError(context: Context): String? {

        return mError.value?.let { context.getString(it) }
    }

    fun clearError() {

        mError.value = null
    }
}