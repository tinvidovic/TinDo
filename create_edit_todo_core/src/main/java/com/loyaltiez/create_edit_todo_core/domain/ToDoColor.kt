package com.loyaltiez.create_edit_todo_core.domain

import com.loyaltiez.create_edit_todo_core.R

enum class ToDoColor(val color: Int) {

    RED(R.color.tinDoRed),
    YELLOW(R.color.tinDoYellow),
    BLUE(R.color.tinDoBlue),
    GREEN(R.color.tinDoGreen),
    PINK(R.color.tinDoPink),
    ORANGE(R.color.tinDoOrange),
    TEAL(R.color.tinDoTeal),
    WHITE(R.color.tinDoWhite);
}

fun getToDoColorFromColorValue(colorValue: Int) : ToDoColor {

    return when(colorValue){

        R.color.tinDoRed -> ToDoColor.RED
        R.color.tinDoYellow -> ToDoColor.YELLOW
        R.color.tinDoBlue -> ToDoColor.BLUE
        R.color.tinDoGreen -> ToDoColor.GREEN
        R.color.tinDoPink -> ToDoColor.PINK
        R.color.tinDoOrange -> ToDoColor.ORANGE
        R.color.tinDoTeal -> ToDoColor.TEAL
        R.color.tinDoWhite -> ToDoColor.WHITE
        else -> ToDoColor.RED
    }
}