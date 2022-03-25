package com.loyaltiez.feature_create_todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.loyaltiez.core.TindoApplication
import com.loyaltiez.create_edit_todo_core.di.CreateEditToDoActivityContainer

class CreateTodoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // CreateToDo flow has started. Populate CreateEditToDoActivityContainer
        (application as TindoApplication).appContainer =
            CreateEditToDoActivityContainer(
                (application as TindoApplication).toDoDAO,
            )

        setContentView(R.layout.activity_create_todo)
    }

    override fun onPause() {

        // CreateToDo flow is finishing
        // Set CreateEditToDoActivityContainer to null
        (application as TindoApplication).appContainer =
            null

        super.onPause()
    }

    override fun onResume() {

        // CreateToDo flow is resuming
        // Populate CreateEditToDoActivityContainer
        (application as TindoApplication).appContainer =
            CreateEditToDoActivityContainer(
                (application as TindoApplication).toDoDAO,
            )

        super.onResume()
    }
}