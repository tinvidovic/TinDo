package com.loyaltiez.feature_edit_todo

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.loyaltiez.core.domain.ToDo

class EditTodoActivity : AppCompatActivity() {

    // Passed arguments, get them in onCreate()
    private var args: EditTodoActivityArgs? = null

    lateinit var todo: ToDo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the passed in arguments (todoObject)
        args = intent.extras?.let { EditTodoActivityArgs.fromBundle(it) }

        todo = args?.todo!!

        setContentView(R.layout.activity_edit_todo)
    }

}