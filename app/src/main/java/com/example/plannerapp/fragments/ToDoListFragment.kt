package com.example.plannerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plannerapp.ItemTodoList
import com.example.plannerapp.R
import com.example.plannerapp.adapters.NotesAdapter
import com.example.plannerapp.adapters.TodoListAdapter
import com.example.plannerapp.databinding.FragmentToDoListBinding

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

        adapter.setOnExpandClickListener(object : TodoListAdapter.OnClickListener{
            override fun onClick(
                position: Int,
                model: ItemTodoList,

            ) {
                super.onClick(position, model)
                adapter.expandItem(position)
            }
        })

        rvTodoList.adapter = adapter
    }

    private fun dataInitialize(){
        val tempNotDone = ArrayList<ItemTodoList>()
        val tempDone = ArrayList<ItemTodoList>()

        for(i in 1..30){
            val temp = ItemTodoList("test title $i")
            if(i % 4 == 0){
                temp.mDone = true
                tempDone.add(temp)
            }else
                tempNotDone.add(temp)
        }
        todoListItems.addAll(tempNotDone)
        todoListItems.addAll(tempDone)

    }

}