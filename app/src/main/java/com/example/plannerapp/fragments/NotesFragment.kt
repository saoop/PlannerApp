package com.example.plannerapp.fragments

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.plannerapp.ColorEnum
import com.example.plannerapp.ItemNote
import com.example.plannerapp.adapters.NotesAdapter
import com.example.plannerapp.R
import com.example.plannerapp.ViewTypeNote
import com.example.plannerapp.databinding.FragmentNotesBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesFragment : Fragment() {

    private lateinit var binding: FragmentNotesBinding

    private lateinit var adapter: NotesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var notesArrayList: ArrayList<ItemNote>

    private val noteBottomSheetFragment = NoteBottomSheetFragment()

    private var mSelectingEnabled = false

    private var mNumSelectedModels = 0

    private lateinit var menu: Menu
    private var mViewTypeNote = ViewTypeNote.TwoColumns

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataInitialize()

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView = binding.rvNotes
        recyclerView.layoutManager = layoutManager
        adapter = NotesAdapter(notesArrayList, mViewTypeNote)
        recyclerView.isNestedScrollingEnabled = false
        adapter.setOnClickListener(object : NotesAdapter.OnClickListener {
            override fun onClick(position: Int, model: ItemNote, holder: NotesAdapter.ViewHolder) {
                super.onClick(position, model, holder)
                if(!mSelectingEnabled) {

                    val bundle = Bundle()
                    bundle.putString("title", model.header)
                    bundle.putString("description", model.description)
                    bundle.putString("color", model.color.toString())
                    noteBottomSheetFragment.setOnChangeColor(object : NoteBottomSheetFragment.OnChangeColor{
                        override fun onColorChanged(color: ColorEnum) {
                            super.onColorChanged(color)
                            adapter.setNewColor(color, position)
                        }
                    })
                    noteBottomSheetFragment.arguments = bundle
                    noteBottomSheetFragment.show(parentFragmentManager, "BottomSheetDialog")}

                else{
                    if(mNumSelectedModels > 0){

                        mNumSelectedModels += adapter.changeItemState(position)

                        if(mNumSelectedModels == 0){
                            exitSelectingState()
                        }
                    }
                }
            }
        })

        adapter.setOnLongClickListener(object : NotesAdapter.OnLongClickListener{
            override fun onLongClick(position: Int, model: ItemNote, holder: NotesAdapter.ViewHolder) {
                super.onLongClick(position, model, holder)

                adapter.setSelectingState()
                adapter.changeItemState(position)

                setSelectingState()
            }
        })

        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        binding.fabAddNote.setOnClickListener{
            noteBottomSheetFragment.show(parentFragmentManager, "BottomSheetDialog")
        }

        val ivStopSelecting = binding.toolbar.root.findViewById<ImageView>(R.id.ivStopSelecting)
        ivStopSelecting.setOnClickListener{
            exitSelectingState()
        }

    }

    private fun setSelectingState(){
        mSelectingEnabled = true
        mNumSelectedModels ++

        binding.fabAddNote.visibility = View.GONE
        binding.toolbar.root.visibility = View.VISIBLE

        Toast.makeText(context, "Started editing...", Toast.LENGTH_SHORT).show()
    }

    private fun exitSelectingState(){
        mSelectingEnabled = false

        mNumSelectedModels = 0

        binding.fabAddNote.visibility = View.VISIBLE
        binding.toolbar.root.visibility = View.GONE

        adapter.exitSelectingState()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)

        this.menu = menu
        changeMenuItemIcon()

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.miChangeView ->{
                changeViewTypeNote()
                changeMenuItemIcon()
                changeRecyclerViewViewType()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun changeViewTypeNote(){
        mViewTypeNote = when(mViewTypeNote){
            ViewTypeNote.OneColumn -> ViewTypeNote.TwoColumns
            ViewTypeNote.TwoColumns -> ViewTypeNote.OneColumn
        }
    }

    private fun changeMenuItemIcon(){
         when(mViewTypeNote){
             ViewTypeNote.OneColumn -> menu.findItem(R.id.miChangeView).setIcon(ContextCompat.getDrawable(requireContext(),
                 R.drawable.ic_notes_one_col_24
             ))
             ViewTypeNote.TwoColumns -> menu.findItem(R.id.miChangeView).setIcon(ContextCompat.getDrawable(requireContext(),
                 R.drawable.ic_round_grid_view_24
             ))
        }
    }

    private fun changeRecyclerViewViewType(){
        when(mViewTypeNote){
            ViewTypeNote.OneColumn -> {
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                //adapter = NotesAdapter(notesArrayList, mViewTypeNote)
                //recyclerView.adapter = adapter
            }
            ViewTypeNote.TwoColumns ->{
                recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                //adapter = NotesAdapter(notesArrayList, mViewTypeNote)
                //recyclerView.adapter = adapter
            }
        }
    }

    private fun dataInitialize(){
        notesArrayList = ArrayList<ItemNote>()
        notesArrayList.add(ItemNote("First", "Simple description... it needs to be long\n and maybe i can add a new line"))
        for(i in 1..30){
            val exampleElement = ItemNote("Header $i", "Description $i")
            notesArrayList.add(exampleElement)
        }
    }

    fun getIsSelecting(): Boolean{
        return mSelectingEnabled
    }

    companion object{
        public fun newInstance(): NotesFragment {
            return NotesFragment()
        }
    }
}