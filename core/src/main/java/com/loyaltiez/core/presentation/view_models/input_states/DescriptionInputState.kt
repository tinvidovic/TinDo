package com.loyaltiez.core.presentation.view_models.input_states

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyaltiez.core.R

class DescriptionInputState(initialValue: String) {

    companion object Constants {

        const val MINIMUM_DESCRIPTION_LENGTH = 1
        const val MAXIMUM_DESCRIPTION_LENGTH = 200
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

        mFormattedValue.value = initialValue.trim()
    }

    fun set(input: String?){

        mInput.value = input?:""

        mFormattedValue.value = (mInput.value?:"").trim()

        clearError()
    }

    fun isValid(): Boolean {

        // Check if the input is of appropriate length
        return mFormattedValue.value?.let {
            it.length in MINIMUM_DESCRIPTION_LENGTH..MAXIMUM_DESCRIPTION_LENGTH
        } == true
    }

    fun setErrors() {

        if (!isValid()){

            if (mFormattedValue.value?.length ?: "".length < MINIMUM_DESCRIPTION_LENGTH){
                mError.value = R.plurals.description_too_short_error
            }else {
                mError.value = R.string.description_too_long_error
            }
        }
    }

    fun getError(context: Context): String? {

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

    fun clearError() {

        mError.value = null
    }
}