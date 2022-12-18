package com.example.plannerapp.items

import com.example.plannerapp.enums.ColorEnum

class ItemNote(val header: String? = null, val description: String? = null,
               val dateAdded: String? = null, var color: ColorEnum = ColorEnum.GREY) {


    companion object{
        enum class Selection{
            SELECTED,
            SELECTING,
            DEFAULT
        }

    }



    public var state = Selection.DEFAULT

    fun getColorLight(): String{
        return when(this.color){
            ColorEnum.RED -> "#ed6458"
            ColorEnum.GREY -> "#F2F0F0"
            ColorEnum.BEIGE -> "#E8C3A5"
            ColorEnum.BLUE -> "#7ae3eb"
            ColorEnum.GREEN -> "#A3EAAA"
        }
    }

    fun getColorDark(): String{
        return when(this.color){
            ColorEnum.RED -> "#c25046"
            ColorEnum.GREY -> "#BBBBBB"
            ColorEnum.BEIGE -> "#D2B290"
            ColorEnum.BLUE -> "#5bbbc2"
            ColorEnum.GREEN -> "#95D0B0"
        }
    }
}