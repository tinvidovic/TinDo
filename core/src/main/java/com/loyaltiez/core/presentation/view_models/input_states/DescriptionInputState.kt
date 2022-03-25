package com.loyaltiez.core.presentation.view_models.input_states

import android.content.Context
import com.loyaltiez.core.R

class DescriptionInputState(initialValue: String) : InputState<String>() {

    companion object Constants {

        const val MINIMUM_DESCRIPTION_LENGTH = 1
        const val MAXIMUM_DESCRIPTION_LENGTH = 200
    }

    init {

        set(initialValue)
    }

    override fun set(input: String?) {

        mInput.value = input ?: ""

        mFormattedValue.value = (mInput.value ?: "").trim()

        super.set(input)
    }

    override fun isValid(): Boolean {

        // Check if the input is of appropriate length
        return mFormattedValue.value?.let {
            it.length in MINIMUM_DESCRIPTION_LENGTH..MAXIMUM_DESCRIPTION_LENGTH
        } == true
    }

    override fun setErrors() {

        if (!isValid()) {

            if (mFormattedValue.value?.length ?: "".length < MINIMUM_DESCRIPTION_LENGTH) {
                mError.value = R.plurals.description_too_short_error
            } else {
                mError.value = R.string.description_too_long_error
            }
        }
    }

    override fun getError(context: Context): String? {

        return when (mError.value) {
            R.plurals.description_too_short_error -> {

                context.resources.getQuantityString(
                    R.plurals.description_too_short_error,
                    MINIMUM_DESCRIPTION_LENGTH,
                    MINIMUM_DESCRIPTION_LENGTH
                )
            }
            R.string.description_too_long_error -> {

                context.getString(
                    R.string.description_too_long_error
                )
            }
            else -> {
                null
            }
        }
    }
}