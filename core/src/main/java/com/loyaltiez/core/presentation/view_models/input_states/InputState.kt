package com.loyaltiez.core.presentation.view_models.input_states

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/*
 An abstract generic class, from which all specific InputState classes inherit
 */
abstract class InputState<T> {

    // The raw input state of the generic type
    protected val mInput = MutableLiveData<T?>()
    val input: LiveData<T?>
        get() = mInput

    // The string formatted value, on which all neccessary sanitization is performed
    protected val mFormattedValue = MutableLiveData<String>()
    val formattedValue: LiveData<String>
        get() = mFormattedValue

    // The error value, which is a nullable int and holds the id of the error string or null
    protected val mError = MutableLiveData<Int?>()
    val error: LiveData<Int?>
        get() = mError

    // Sets the input and formatted values to the input
    open fun set(input: T?) {

        clearError()
    }

    // Checks if the current state is valid
    abstract fun isValid(): Boolean

    // Sets the errors based on the validity of the inputState
    abstract fun setErrors()

    // Returns the string of the error message, or null if the inputState is in the valid state
    abstract fun getError(context: Context): String?

    // Clears all errors (used when a new value is inputted)
    protected fun clearError() {

        mError.value = null
    }
}