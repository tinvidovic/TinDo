package com.loyaltiez.core.presentation.view_models.input_states

import android.content.Context
import com.loyaltiez.core.R

class PasswordInputState(initialValue: String) : InputState<String>() {

    companion object Constants {

        const val MINIMUM_PASSWORD_LENGTH = 1
    }

    init {

        set(initialValue)
    }

    override fun set(input: String?) {

        mInput.value = input ?: ""

        mFormattedValue.value = (mInput.value ?: "")

        super.set(input)
    }

    override fun isValid(): Boolean {

        // Check if the input is of appropriate length
        return mFormattedValue.value?.let {
            it.length >= MINIMUM_PASSWORD_LENGTH
        } == true
    }

    override fun setErrors() {

        if (!isValid())
            mError.value = R.plurals.password_too_short_error
    }

    override fun getError(context: Context): String? {

        return mError.value?.let {
            context.resources.getQuantityString(
                it,
                MINIMUM_PASSWORD_LENGTH,
                MINIMUM_PASSWORD_LENGTH
            )
        }
    }
}