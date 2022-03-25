package com.loyaltiez.feature_edit_todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.loyaltiez.core.TindoApplication
import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.create_edit_todo_core.di.CreateEditToDoActivityContainer

class EditTodoActivity : AppCompatActivity() {

    // Passed arguments, get them in onCreate()
    private var args: EditTodoActivityArgs? = null

    lateinit var todo: ToDo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // EditToDo flow has started. Populate CreateEditToDoActivityContainer
        (application as TindoApplication).appContainer =
            CreateEditToDoActivityContainer(
                (application as TindoApplication).toDoDAO,
            )

        // Get the passed in arguments (todoObject)
        args = intent.extras?.let { EditTodoActivityArgs.fromBundle(it) }

        todo = args?.todo!!

        setContentView(R.layout.activity_edit_todo)
    }

    override fun onPause() {

        // EditToDo flow is finishing
        // Set CreateEditToDoActivityContainer to null
        (application as TindoApplication).appContainer =
            null

        super.onPause()
    }

    override fun onResume() {

        // EditToDo flow is resuming
        // Populate CreateEditToDoActivityContainer
        (application as TindoApplication).appContainer =
            CreateEditToDoActivityContainer(
                (application as TindoApplication).toDoDAO,
            )

        super.onResume()
    }

}