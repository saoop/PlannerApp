package com.example.plannerapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.plannerapp.fragments.NotesFragment
import com.example.plannerapp.fragments.ToDoListFragment

class PagerAdapter(fm: FragmentManager):FragmentPagerAdapter(fm) {

    companion object{
        const val positionToDoListFragment = 0
        const val positionNotesFragment = 1
    }

    private val mFragments = listOf(ToDoListFragment(), NotesFragment())

    override fun getCount(): Int {
        return mFragments.size
    }


    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

}