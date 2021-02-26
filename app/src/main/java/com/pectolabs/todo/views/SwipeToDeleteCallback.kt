package com.pectolabs.todo.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.pectolabs.todo.adapters.TaskAdapter

abstract class SwipeToDeleteCallback(var context: Context, dragDir: Int, swipeDir: Int) :
    ItemTouchHelper.SimpleCallback(dragDir, swipeDir) {

    //configure left swipe params
    var leftBG: Int = Color.LTGRAY
    var leftLabel: String = ""
    var leftIcon: Drawable? = null

    //configure right swipe params
    var rightBG: Int = Color.LTGRAY;
    var rightLabel: String = ""
    var rightIcon: Drawable? = null

    private lateinit var background: Drawable

    var initiated: Boolean = false

    //Setting Swipe Text
    val paint = Paint()

    fun initSwipeView(): Unit {
        paint.color = Color.WHITE
        paint.textSize = 30f
        paint.textAlign = Paint.Align.CENTER
        background = ColorDrawable();
        initiated = true;
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        /**
         * To disable "swipe" for specific item return 0 here.
         * For example:
         * if (viewHolder?.itemViewType == YourAdapter.SOME_TYPE) return 0
         * if (viewHolder?.adapterPosition == 0) return 0
         */
        //if task = completed then, make only left swipe(for delete only)
        if (viewHolder.itemViewType == TaskAdapter.TYPE_COMPLETED) return makeMovementFlags(
            0,
            ItemTouchHelper.LEFT
        )
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }


    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {


        Log.d("onChildDraw", "dx: " + dX)

        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        if (!initiated) {
            initSwipeView()
        }


        if (dX != 0.0f) {

            if (dX > 0) {
                //right swipe
                val intrinsicHeight = (rightIcon?.intrinsicHeight ?: 0)
                val intrinsicWidth = (rightIcon?.intrinsicWidth ?: 0)
                val xMarkTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                val xMarkMargin = (itemHeight - intrinsicHeight) / 2
                val xMarkLeft = itemView.left + xMarkMargin
                val xMarkRight = itemView.left + xMarkMargin + intrinsicWidth
                val xMarkBottom = xMarkTop + intrinsicHeight

                colorCanavas(
                    c,
                    rightBG,
                    itemView.left + dX.toInt(),
                    itemView.top,
                    itemView.left,
                    itemView.bottom
                )

                drawIconOnCanVas(c, rightIcon, xMarkLeft, xMarkTop, xMarkRight, xMarkBottom)
//                drawTextOnCanvas(c, rightLabel, (itemView.left + 200).toFloat(), (xMarkTop + 10).toFloat())

            } else {
                //left swipe
                val intrinsicHeight = (leftIcon?.intrinsicHeight ?: 0)
                val intrinsicWidth = (leftIcon?.intrinsicWidth ?: 0)
                val xMarkTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                val xMarkMargin = (itemHeight - intrinsicHeight) / 2
                val xMarkLeft = itemView.right - xMarkMargin - intrinsicWidth
                val xMarkRight = itemView.right - xMarkMargin
                val xMarkBottom = xMarkTop + intrinsicHeight

                colorCanavas(
                    c,
                    leftBG,
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
//                drawTextOnCanvas(c, leftLabel, (itemView.right - 200).toFloat(), (xMarkTop + 10).toFloat())
                drawIconOnCanVas(c, leftIcon, xMarkLeft, xMarkTop, xMarkRight, xMarkBottom)
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }


    fun colorCanavas(
        canvas: Canvas,
        canvasColor: Int,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ): Unit {

        (background as ColorDrawable).color = canvasColor
        background.setBounds(left, top, right, bottom)
        background.draw(canvas)

    }


    fun drawTextOnCanvas(canvas: Canvas, label: String, x: Float, y: Float) {
        canvas.drawText(label, x, y, paint)
    }

    fun drawIconOnCanVas(
        canvas: Canvas, icon: Drawable?, left: Int, top: Int, right: Int, bottom: Int
    ): Unit {
        icon?.setBounds(left, top, right, bottom)
        icon?.draw(canvas)

    }

}