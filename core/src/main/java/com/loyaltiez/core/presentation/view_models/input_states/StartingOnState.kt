package com.loyaltiez.core.presentation.view_models.input_states

import android.content.Context
import com.loyaltiez.core.R
import com.loyaltiez.core.helper.converters.convertDateToString
import java.sql.Date

class StartingOnState(initialValue: Date?, val context: Context) : InputState<Date>() {

    init {

        set(initialValue)
    }

    override fun set(input: Date?) {

        mInput.value = input

        if (input == null)
            mFormattedValue.value = ""
        else
            mFormattedValue.value = convertDateToString(mInput.value)

        super.set(input)
    }

    override fun isValid(): Boolean {

        return mInput.value != null
    }

    override fun setErrors() {

        if (!isValid())
            mError.value = R.string.starting_on_error
    }

    override fun getError(context: Context): String? {

        return mError.value?.let { context.getString(it) }
    }
}