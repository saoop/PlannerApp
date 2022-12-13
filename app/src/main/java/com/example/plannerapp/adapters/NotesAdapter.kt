package com.example.plannerapp.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.Transliterator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.IntegerRes
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.RecyclerView
import com.example.plannerapp.*
import com.google.android.material.internal.ContextUtils.getActivity

class NotesAdapter(private val list: ArrayList<ItemNote>, private val viewType: ViewTypeNote): RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null
    private var onLongClickListener: NotesAdapter.OnLongClickListener? = null


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val layout: LinearLayout = itemView.findViewById(R.id.llItemNote)
        val header: TextView = itemView.findViewById(R.id.tvItemNoteHeader)
        val description: TextView = itemView.findViewById(R.id.tvItemNoteDescription)
        val ivSelected: ImageView = itemView.findViewById(R.id.ivSelected)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = when(this.viewType){
            ViewTypeNote.OneColumn -> {
                LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
            }
            ViewTypeNote.TwoColumns -> {
                LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
            }
        }
        return ViewHolder(itemView)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    fun setOnLongClickListener(onLongClickListener: OnLongClickListener){
        this.onLongClickListener = onLongClickListener
    }

    interface OnClickListener{
        fun onClick (position: Int, model: ItemNote, holder: ViewHolder){

        }
    }

    interface OnLongClickListener {
        fun onLongClick(position: Int, model: ItemNote, holder: ViewHolder){
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.header.setText(currentItem.header)
        holder.description.setText(currentItem.description)

        holder.ivSelected.visibility = View.VISIBLE

        when(currentItem.state){
            ItemNote.Companion.Selection.SELECTED -> {
                holder.layout.setBackgroundColor(Color.parseColor(currentItem.getColorDark()))
                holder.ivSelected.setImageResource(R.drawable.ic_filled_circle_24)
            }
            ItemNote.Companion.Selection.DEFAULT -> {
                holder.ivSelected.visibility = View.INVISIBLE
                holder.layout.setBackgroundColor(Color.parseColor(currentItem.getColorLight()))
            }
            ItemNote.Companion.Selection.SELECTING -> {
                holder.layout.setBackgroundColor(Color.parseColor(currentItem.getColorLight()))
                holder.ivSelected.setImageResource(R.drawable.ic_outline_circle_24)
            }
        }



        holder.itemView.setOnClickListener{
            if(onClickListener != null){
                onClickListener!!.onClick(position, currentItem, holder)
            }

        }

        holder.itemView.setOnLongClickListener{
            if(onLongClickListener != null){
                onLongClickListener!!.onLongClick(position, currentItem, holder)
            }
            true
        }

    }

    /**
     * @return Int -1 or 1 if the state was changed to selecting or selected and 0 otherwise
     */

    fun changeItemState(position: Int): Int{
        if (list[position].state == ItemNote.Companion.Selection.SELECTING){
            list[position].state = ItemNote.Companion.Selection.SELECTED
            notifyItemChanged(position)
            return 1
        }
        else if(list[position].state == ItemNote.Companion.Selection.SELECTED){
            list[position].state = ItemNote.Companion.Selection.SELECTING
            notifyItemChanged(position)
            return -1
        }
        return 0
    }

    fun setNewColor(colorEnum: ColorEnum, position: Int){
        list[position].color = colorEnum
        notifyItemChanged(position)
    }

    fun setSelectingState(){
        for(model in list){
            model.state = ItemNote.Companion.Selection.SELECTING
        }
        notifyDataSetChanged()
    }


    fun exitSelectingState(){
        for(model in list){
            model.state = ItemNote.Companion.Selection.DEFAULT
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}