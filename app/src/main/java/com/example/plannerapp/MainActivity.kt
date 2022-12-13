package com.example.plannerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.viewpager.widget.ViewPager
import com.example.plannerapp.adapters.PagerAdapter
import com.example.plannerapp.databinding.ActivityMainBinding
import com.example.plannerapp.fragments.NotesFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var mToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)

        val pAdapter = PagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = pAdapter

        val vp = binding.viewPager as ViewPager

        val toolBar = binding.tbMain
        setSupportActionBar(toolBar)

        //binding.viewPager.setOnTouchListener { view, motionEvent -> true }

               //binding.viewPager.performClick
        mToggle  = ActionBarDrawerToggle(this, binding.dlMain, toolBar, R.string.open, R.string.close)
        binding.dlMain.addDrawerListener(mToggle)
        mToggle.syncState()



        binding.rbNotes.setOnCheckedChangeListener { _, b ->
            if(b) binding.viewPager.currentItem = PagerAdapter.positionNotesFragment
        }
        binding.rbTodoList.setOnCheckedChangeListener { _, b ->
            if(b) binding.viewPager.currentItem = PagerAdapter.positionToDoListFragment
        }


        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if(position == PagerAdapter.positionNotesFragment){
                    binding.rbNotes.isChecked = true
                    binding.rbTodoList.isChecked = false
                } else{
                    binding.rbNotes.isChecked = false
                    binding.rbTodoList.isChecked = true
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(mToggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}