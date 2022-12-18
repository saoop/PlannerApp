package com.example.plannerapp.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plannerapp.items.ItemTodoList
import com.example.plannerapp.R
import com.example.plannerapp.adapters.TodoListAdapter

class ToDoListFragment : Fragment() {

    private lateinit var rvTodoList: RecyclerView
    private lateinit var adapter: TodoListAdapter
    private val todoListItems = ArrayList<ItemTodoList>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_to_do_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataInitialize()

        rvTodoList = view.findViewById(R.id.rvTodoList)
        rvTodoList.layoutManager = LinearLayoutManager(context)
        adapter = TodoListAdapter(todoListItems)

        adapter.setOnExpandClickListener(object : TodoListAdapter.OnClickListener {
            override fun onClick(
                position: Int,
                model: ItemTodoList,
                holder: TodoListAdapter.ViewHolder?
            ) {
                super.onClick(position, model, null)
                adapter.expandItem(position)
            }
        })

        adapter.setOnMenuClickListener(object : TodoListAdapter.OnClickListener {
            //So that I can use a MenuBuilder
            @SuppressLint("RestrictedApi")
            override fun onClick(
                position: Int,
                model: ItemTodoList,
                holder: TodoListAdapter.ViewHolder?
            ) {
                super.onClick(position, model, holder)

                val menuBuilder = MenuBuilder(context)
                val inflater = MenuInflater(context)
                inflater.inflate(R.menu.menu_todo_list, menuBuilder)
                val menuHelper = MenuPopupHelper(requireContext(), menuBuilder, holder!!.ivMore)
                menuHelper.setForceShowIcon(true)
                menuBuilder.setCallback(object : MenuBuilder.Callback{
                    override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {

                        when(item.itemId){
                            R.id.miEditTodoList -> {
                                return true
                            }
                            R.id.miNotificationsTodoList -> {
                                return true
                            }
                            R.id.miDeleteTodoList -> {
                                return true
                            }
                        }

                        return false
                    }

                    override fun onMenuModeChange(menu: MenuBuilder) {}
                })

                menuHelper.show()

            }
        })

        rvTodoList.adapter = adapter

    }

    private fun dataInitialize() {
        val tempNotDone = ArrayList<ItemTodoList>()
        val tempDone = ArrayList<ItemTodoList>()

        val testItem = ItemTodoList("title test nested view", "description")
        for(i in 1..5){
            val temp = ItemTodoList("nested item $i", isChild = true)
            testItem.subList.add(temp)
        }

        todoListItems.add(testItem)

        for (i in 2..30) {
            val temp = ItemTodoList("test title $i")
            if (i % 4 == 0) {
                temp.mDone = true
                tempDone.add(temp)
            } else
                tempNotDone.add(temp)
        }
        todoListItems.addAll(tempNotDone)
        todoListItems.addAll(tempDone)

    }

}