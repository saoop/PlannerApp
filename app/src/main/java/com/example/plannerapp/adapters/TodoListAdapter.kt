package com.example.plannerapp.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plannerapp.ItemNote
import com.example.plannerapp.ItemTodoList
import com.example.plannerapp.R

class TodoListAdapter(private val list: ArrayList<ItemTodoList>) :
    RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    private var onExpandClickListener: OnClickListener? = null


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tvTodoItemTitle)
        val description = itemView.findViewById<TextView>(R.id.tvTodoItemDescription)
        val checkBox = itemView.findViewById<ImageView>(R.id.ivCheckBox)
        val ivExpand = itemView.findViewById<ImageView>(R.id.ivExpandTodoList)
        val llBottom = itemView.findViewById<LinearLayout>(R.id.llBottomLayoutTodoList)
    }

    interface OnClickListener {
        fun onClick(position: Int, model: ItemTodoList) {

        }
    }

    fun setOnExpandClickListener(onClickListener: OnClickListener) {
        this.onExpandClickListener = onClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_todo_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = list[position].title
        holder.description.text = list[position].description

        if (list[position].mDone) {
            holder.checkBox.setImageResource(R.drawable.ic_baseline_check_box_24)
        } else holder.checkBox.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24)

        holder.ivExpand.setOnClickListener {
            if (onExpandClickListener != null) {
                onExpandClickListener!!.onClick(position, list[position])
            }
        }

        if (list[position].mExpanded) {
            holder.llBottom.visibility = View.VISIBLE
            holder.ivExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
        } else {
            holder.llBottom.visibility = View.GONE
            holder.ivExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
        }

    }

    fun expandItem(position: Int) {
        //close all items that were expanded
        if (!list[position].mExpanded) {
            for (i in 0 until itemCount) {
                if (list[i].mExpanded) {
                    list[i].mExpanded = false
                    notifyItemChanged(i)
                }
            }
        }
        list[position].mExpanded = !list[position].mExpanded
        notifyItemChanged(position)

    }

    override fun getItemCount(): Int {
        return list.size
    }
}