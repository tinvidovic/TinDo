package com.loyaltiez.core.presentation.view_models.input_states

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyaltiez.core.R

class TitleInputState(initialValue: String) {

    companion object Constants {

        const val MINIMUM_TITLE_LENGTH = 1
        const val MAXIMUM_TITLE_LENGTH = 50
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
            it.length in MINIMUM_TITLE_LENGTH..MAXIMUM_TITLE_LENGTH
        } == true
    }

    fun setErrors() {

        if (!isValid()){

            if (mFormattedValue.value?.length ?: "".length < MINIMUM_TITLE_LENGTH){
                mError.value = R.plurals.title_too_short_error
            }else {
                mError.value = R.string.title_too_long_error
            }
        }
    }

    fun getError(context: Context): String? {

        return when (mError.value) {
            R.plurals.title_too_short_error -> {

                context.resources.getQuantityString(R.plurals.title_too_short_error,
                    MINIMUM_TITLE_LENGTH,
                    MINIMUM_TITLE_LENGTH
                )
            }
            R.string.title_too_long_error -> {

                context.getString(
                    R.string.title_too_long_error
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