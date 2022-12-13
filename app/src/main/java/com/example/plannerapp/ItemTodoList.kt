package com.example.plannerapp

class ItemTodoList(var title: String, var description: String? = null,
                   var dateAdded: String? = null )
{
    val subList = ArrayList<ItemTodoList>()
    var mDone = false
}