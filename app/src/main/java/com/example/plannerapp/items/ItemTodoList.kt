package com.example.plannerapp.items

class ItemTodoList(var title: String, var description: String? = null,
                   var dateAdded: String? = null, val isChild: Boolean = false)
{
    val subList = ArrayList<ItemTodoList>()

    var mExpanded = false;
    var mDone = false

    fun getChildren() : ArrayList<ItemTodoList>?{
        return if(!isChild)
            subList
        else
            null
    }

    fun addChild(item : ItemTodoList){
        if(!isChild){
            subList.add(item)
        }
    }

    fun getChildrenCount(): Int{
        return subList.size
    }
}