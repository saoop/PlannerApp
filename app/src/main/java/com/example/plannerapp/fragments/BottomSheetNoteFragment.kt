package com.example.plannerapp.fragments

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*

import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import com.example.plannerapp.enums.ColorEnum
import com.example.plannerapp.R
import com.example.plannerapp.databinding.FragmentBottomSheetNotesBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NoteBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBottomSheetNotesBinding
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private var onChangeColor: OnChangeColor? = null
    private var swipeable = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetNotesBinding.inflate(inflater, container, false)
        return binding.root
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

    interface OnChangeColor {
        public fun onColorChanged(color: ColorEnum) {

        }
    }

    /**
     * I need this interface to change the color of an item in the adapter. And to update the
     * database outside of this class.
     */

    fun setOnChangeColor(onChangeColor: OnChangeColor) {
        this.onChangeColor = onChangeColor
    }

    /**
     * Changes the color of the Background
     */

    private fun changeBackground(colorEnumString: String?) {
        val backGround = when (colorEnumString) {
            ColorEnum.GREY.toString() -> R.drawable.bottom_sheet_note_background_grey
            ColorEnum.RED.toString() -> R.drawable.bottom_sheet_note_background_red
            ColorEnum.BLUE.toString() -> R.drawable.bottom_sheet_note_background_blue
            ColorEnum.BEIGE.toString() -> R.drawable.bottom_sheet_note_background_beige
            ColorEnum.GREEN.toString() -> R.drawable.bottom_sheet_note_background_green
            else -> R.drawable.bottom_sheet_note_background_grey
        }
        binding.llBottomSheetNote.setBackgroundResource(backGround)
    }

    private fun changeAppBarBackGround(colorEnumString: String?) {
        val backGround = when (colorEnumString) {
            ColorEnum.GREY.toString() -> R.color.light_grey
            ColorEnum.RED.toString() -> R.color.red
            ColorEnum.BLUE.toString() -> R.color.blue
            ColorEnum.BEIGE.toString() -> R.color.beige
            ColorEnum.GREEN.toString() -> R.color.green
            else -> R.color.light_grey
        }
        binding.ablNotesDialog.setBackgroundResource(backGround)
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

        changeBackground(arguments?.getString("color"))
        changeAppBarBackGround(arguments?.getString("color"))

        val colorBtn = dialog?.findViewById<ImageView>(R.id.ivPalette)

        //Here we use menuHelper in order to display icons in menu

        colorBtn?.setOnClickListener { _view ->

            val menuBuilder = MenuBuilder(context)
            val inflater = MenuInflater(context)
            inflater.inflate(R.menu.menu_colors_note, menuBuilder)
            val menuHelper = MenuPopupHelper(requireContext(), menuBuilder, _view)
            menuHelper.setForceShowIcon(true)

            menuBuilder.setCallback(object : MenuBuilder.Callback {
                override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.miColorGreen -> {
                            onChangeColor?.onColorChanged(ColorEnum.GREEN)
                            changeBackground(ColorEnum.GREEN.toString())
                            changeAppBarBackGround(ColorEnum.GREEN.toString())
                            return true
                        }
                        R.id.miColorBeige -> {
                            onChangeColor?.onColorChanged(ColorEnum.BEIGE)
                            changeBackground(ColorEnum.BEIGE.toString())
                            changeAppBarBackGround(ColorEnum.BEIGE.toString())
                            return true
                        }
                        R.id.miColorBlue -> {
                            onChangeColor?.onColorChanged(ColorEnum.BLUE)
                            changeAppBarBackGround(ColorEnum.BLUE.toString())
                            changeBackground(ColorEnum.BLUE.toString())
                            return true
                        }
                        R.id.miColorRed -> {
                            onChangeColor?.onColorChanged(ColorEnum.RED)
                            changeBackground(ColorEnum.RED.toString())
                            changeAppBarBackGround(ColorEnum.RED.toString())
                            return true
                        }
                        R.id.miColorGrey -> {
                            onChangeColor?.onColorChanged(ColorEnum.GREY)
                            changeBackground(ColorEnum.GREY.toString())
                            changeAppBarBackGround(ColorEnum.GREY.toString())
                            return true
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
                            draggingLine?.visibility = View.GONE
                            swipeable = false
                            Log.i("BottomSheet", "disabling swiping...")

                            //bottomSheetBehavior.onNestedScroll()
                            Log.i("BottomSheet", "Expanded...")
                        }
                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                            Log.i("BottomSheet", "half expanded...")
                        }
                        BottomSheetBehavior.STATE_SETTLING -> {
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
                    if (slideOffset == 1f) {
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