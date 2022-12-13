package com.example.plannerapp.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plannerapp.ItemNote
import com.example.plannerapp.ItemTodoList
import com.example.plannerapp.R

class TodoListAdapter(private val list: ArrayList<ItemTodoList>):RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) :  RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.tvTodoItemTitle)
        val description = itemView.findViewById<TextView>(R.id.tvTodoItemDescription)
        val checkBox = itemView.findViewById<ImageView>(R.id.ivCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_todo_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = list[position].title
        holder.description.text = list[position].description
        if(list[position].mDone){
            holder.checkBox.setImageResource(R.drawable.ic_baseline_check_box_24)
        } else holder.checkBox.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24)

    }

    override fun getItemCount(): Int {
        return list.size
    }
}