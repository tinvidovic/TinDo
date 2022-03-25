package com.loyaltiez.core.presentation.view_models.input_states

import android.content.Context
import com.loyaltiez.core.R
import com.loyaltiez.core.helper.converters.convertTimeToString
import java.sql.Time

class RemindMeAtState(initialValue: Time?, val context: Context) : InputState<Time>() {

    init {

        set(initialValue)
    }

    override fun set(input: Time?) {

        mInput.value = input

        if (input == null)
            mFormattedValue.value = ""
        else
            mFormattedValue.value = convertTimeToString(mInput.value)

        super.set(input)
    }

    override fun isValid(): Boolean {

        return mInput.value != null
    }

    override fun setErrors() {

        if (!isValid())
            mError.value = R.string.remind_me_at_error
    }

    override fun getError(context: Context): String? {

        return mError.value?.let { context.getString(it) }
    }
}