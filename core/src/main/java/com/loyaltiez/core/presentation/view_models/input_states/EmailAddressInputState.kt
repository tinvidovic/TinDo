package com.loyaltiez.core.presentation.view_models.input_states

import android.content.Context
import android.util.Patterns
import com.loyaltiez.core.R

class EmailAddressInputState(initialValue: String) : InputState<String>() {

    init {

        set(initialValue)
    }

    override fun set(input: String?) {

        mInput.value = input ?: ""

        mFormattedValue.value = (mInput.value ?: "").trim()

        super.set(input)
    }

    override fun isValid(): Boolean {

        // Check if the input matches the e-mail pattern
        return Patterns.EMAIL_ADDRESS.matcher(mFormattedValue.value.toString()).matches()
    }

    override fun setErrors() {

        if (!isValid())
            mError.value = R.string.invalid_email_error
    }

    override fun getError(context: Context): String? {

        return mError.value?.let { context.getString(it) }
    }

    fun setError(errorResourceId: Int) {

        mError.value = errorResourceId
    }
}