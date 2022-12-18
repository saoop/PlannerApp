package com.example.plannerapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plannerapp.items.ItemTodoList
import com.example.plannerapp.R

class TodoListAdapter(private val list: ArrayList<ItemTodoList>) :
    RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    private var mExpandedPosition = -1
    private var onExpandClickListener: OnClickListener? = null
    private var onMenuClickListener: OnClickListener? = null


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tvTodoItemTitle)
        val description = itemView.findViewById<TextView>(R.id.tvTodoItemDescription)
        val checkBox = itemView.findViewById<ImageView>(R.id.ivCheckBox)
        val ivExpand = itemView.findViewById<ImageView>(R.id.ivExpandTodoList)
        val llBottom = itemView.findViewById<LinearLayout>(R.id.llBottomLayoutTodoList)
        val ivMore = itemView.findViewById<ImageView>(R.id.ivMoreTodoList)
    }

    interface OnClickListener {
        fun onClick(position: Int, model: ItemTodoList, holder: ViewHolder? = null) {

        }
    }

    fun setOnExpandClickListener(onClickListener: OnClickListener) {
        this.onExpandClickListener = onClickListener
    }

    fun setOnMenuClickListener(onClickListener: OnClickListener) {
        this.onMenuClickListener = onClickListener
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
        holder.ivMore.setOnClickListener {
            if (onMenuClickListener != null) {
                onMenuClickListener!!.onClick(position, list[position], holder)
            }
        }

        if (list[position].mExpanded) {
            holder.llBottom.visibility = View.VISIBLE
            holder.ivExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_grey_24)
        } else {
            holder.llBottom.visibility = View.GONE
            holder.ivExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_grey_24)
        }

    }

    fun expandItem(pos: Int) {
        var position = pos
        //close all items that were expanded
        if (!list[position].mExpanded) {
            Log.i("TodoListAdapter", "begin... $position")

            for (i in 0 until itemCount) {
                if (list[i].mExpanded) {
                    list[i].mExpanded = false
                    if (list[i].getChildrenCount() > 0) {
                        for (j in i + 1 until i + list[i].getChildrenCount() + 1) {
                            list.removeAt(i + 1)
                        }

                    }
                    Log.i("TodoListAdapter", "break... $position, ${list[i].getChildrenCount()}")
                    //position -= list[i].getChildrenCount()
                    notifyDataSetChanged()
                    break

                }
            }
            if (list[position].getChildrenCount() > 0) {
                list.addAll(position + 1, list[position].subList)
                for (i in position + 1 until position + list[position].getChildrenCount() + 1)
                    notifyItemInserted(i)
            }
            list[position].mExpanded = !list[position].mExpanded
            notifyItemChanged(position)
        }
        else{
            list[position].mExpanded = false
            if (list[position].getChildrenCount() > 0) {
                for (j in position + 1 until position + list[position].getChildrenCount() + 1) {
                    list.removeAt(position + 1)
                }
                notifyDataSetChanged()
            }
            notifyItemChanged(position)

        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
}