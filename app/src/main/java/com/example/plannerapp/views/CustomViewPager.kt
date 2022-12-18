package com.example.plannerapp.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import com.example.plannerapp.adapters.PagerAdapter
import com.example.plannerapp.fragments.NotesFragment

/**
 * A custom ViewPager
 * This class was created to disable swiping, when needed. (for example when user is editing notes)
 * It overrides methods responsible for swiping.
 *
 */

class CustomViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    private var onTouchChanger: OnTouchChanger? = null


    /**
     * I don't need this interface anymore, but maybe it will be useful in the future
     */
    interface OnTouchChanger{
        fun onTouch(){

        }
    }

    fun setOnTouchChanger(onTouchChanger: OnTouchChanger){
        this.onTouchChanger = onTouchChanger
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (adapter != null) {
            val pa = adapter as PagerAdapter
            val fr = pa.getItem(PagerAdapter.positionNotesFragment) as NotesFragment
            if(!fr.getIsSelecting()){
                return super.onTouchEvent(ev)
            }
        }
        return false
    }

    override fun performClick(): Boolean {
        if (adapter != null) {
            val pa = adapter as PagerAdapter
            val fr = pa.getItem(PagerAdapter.positionNotesFragment) as NotesFragment
            if(!fr.getIsSelecting()){

                return super.performClick()
            }
        }
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (adapter != null) {
            val pa = adapter as PagerAdapter
            val fr = pa.getItem(PagerAdapter.positionNotesFragment) as NotesFragment
            if(!fr.getIsSelecting()){
                return super.onInterceptTouchEvent(ev)

            }
        }
        return false
    }

}