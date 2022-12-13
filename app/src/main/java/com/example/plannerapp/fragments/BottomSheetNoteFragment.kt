package com.example.plannerapp.fragments

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*

import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import com.example.plannerapp.ColorEnum
import com.example.plannerapp.R
import com.example.plannerapp.databinding.FragmentBottomSheetNotesBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NoteBottomSheetFragment : BottomSheetDialogFragment() {

    //lateinit var binding: FragmentBottomSheetNotesBinding
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private var onChangeColor: OnChangeColor? = null
    private var swipeable = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_notes, container, false)
    }

    /**
     * In order to make a bottomSheetFragment that can be expanded by dragging in upwards,
     * It is needed to create a CoordinatorLayout inside which there will be a LinearLayout
     * That allows scrolling, otherwise it is not expandable. Also, it is necessary to create a
     * "filler" view, that just takes space, so that the Layout is actually scrollable.
     *
     * Through BottomSheetBehavior we can determine the state of the Layout. When it's fully expanded,
     * the appbar is visible, otherwise it's hidden.
     *
     * It is also important to set peekHeight in LinearLayout in xml to 1000dp, so that it takes full
     * space.
     *
     * By the way I have to set the callBack to BottomSheetBehavior, otherwise it does not work properly
     * (not just add a new callback to already existing one)
     *
     */

    interface OnChangeColor{
        public fun onColorChanged(color: ColorEnum){

        }
    }

    fun setOnChangeColor(onChangeColor: OnChangeColor){
        this.onChangeColor = onChangeColor
    }


    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val viewExtraSpace = dialog?.findViewById<View>(R.id.extraSpace)
        viewExtraSpace?.minimumHeight =
            (Resources.getSystem().displayMetrics.heightPixels) / 2

        val appBar = dialog?.findViewById<AppBarLayout>(R.id.ablNotesDialog)
        val draggingLine = dialog?.findViewById<View>(R.id.vDraggingLine)

        val clearBtn = dialog?.findViewById<ImageView>(R.id.ivCloseDialog)
        clearBtn?.setOnClickListener {

            dismiss()
        }

        //TODO create background for every color, need a lighter color anyway + a new color for appbar
        //TODO check with when which color we get from arguments

        val llBottomSheet = dialog?.findViewById<LinearLayout>(R.id.llBottomSheetNote)
        llBottomSheet?.setBackgroundResource(R.drawable.bottom_sheet_note_background_red)
        //llBottomSheet?.setBackgroundColor(Color.parseColor(arguments?.getString("color")))

        val colorBtn = dialog?.findViewById<ImageView>(R.id.ivPalette)

        //Here we use menuHelper in order to display icons in menu

        colorBtn?.setOnClickListener { _view ->

            val menuBuilder = MenuBuilder(context)
            val inflater = MenuInflater(context)
            inflater.inflate(R.menu.menu_colors_note, menuBuilder)
            val menuHelper = MenuPopupHelper(requireContext(), menuBuilder, _view)
            menuHelper.setForceShowIcon(true)

            menuBuilder.setCallback(object : MenuBuilder.Callback{
                override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                    when(item.itemId){
                        R.id.miColorGreen ->{
                            onChangeColor?.onColorChanged(ColorEnum.GREEN)
                            return true
                        }
                        R.id.miColorBeige->{
                            onChangeColor?.onColorChanged(ColorEnum.BEIGE)
                            return true
                        }
                        R.id.miColorBlue -> {
                            onChangeColor?.onColorChanged(ColorEnum.BLUE)
                        }
                        R.id.miColorRed -> {
                            onChangeColor?.onColorChanged(ColorEnum.RED)
                        }
                        R.id.miColorGrey -> {
                            onChangeColor?.onColorChanged(ColorEnum.GREY)
                        }
                    }

                    return false
                }

                override fun onMenuModeChange(menu: MenuBuilder) {}
            })
            menuHelper.show()
        }

        dialog?.setOnShowListener {
            val d = it as BottomSheetDialog
            d.setContentView(view)
            val title = arguments?.getString("title")
            val description = arguments?.getString("description")

            d.findViewById<EditText>(R.id.etTitle)?.setText(title)
            d.findViewById<EditText>(R.id.etDescription)?.setText(description)

            val bottomSheetBehavior = d.behavior

            Log.i("BottomSheet", "dialog created...")

            bottomSheetBehavior.setBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {

                    when (newState) {
                        BottomSheetBehavior.STATE_DRAGGING -> {
                            Log.i("BottomSheet", "Dragging...")

                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                           appBar?.visibility = View.VISIBLE
                            draggingLine?.visibility  = View.GONE
                            swipeable = false
                            Log.i("BottomSheet", "disabling swiping...")

                            //bottomSheetBehavior.onNestedScroll()
                            Log.i("BottomSheet", "Expanded...")
                        }
                        BottomSheetBehavior.STATE_HALF_EXPANDED ->{
                            Log.i("BottomSheet", "half expanded...")
                        }
                        BottomSheetBehavior.STATE_SETTLING ->{
                            Log.i("BottomSheet", "settling...")

                        }
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            Log.i("BottomSheet", "Hidden...")
                            dismiss();
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            Log.i("BottomSheet", "Collapsed...")
                            appBar?.visibility = View.GONE
                            draggingLine?.visibility = View.VISIBLE

                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    if(slideOffset == 1f){
                        //Log.i("BottomSheet", "slided up")
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

            })

        }
    }
    /**
     * This makes the BottomSheet with rounded corners.
     *
     * Need to override this function since it is not possible to set the style of the BottomSheet
     * from the xml file. Also the default style is overridden.
     */

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

}