package com.example.plannerapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.text.Layout;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;


/**
 * Just copied this code from StackOverflow
 * I only changed the maximum number of Lines.
 */

public class FadingTextView extends AppCompatTextView {

    private static final float FADE_LENGTH_FACTOR = .5f;

    private final RectF drawRect = new RectF();
    private final Rect realRect = new Rect();
    private final Path selection = new Path();
    private final Matrix matrix = new Matrix();
    private final Paint paint = new Paint();
    private final Shader shader =
            new LinearGradient(0f, 0f, 1f, 0f, 0x00000000, 0xFF000000, Shader.TileMode.CLAMP);

    public FadingTextView(Context context) {
        this(context, null);
    }

    public FadingTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public FadingTextView(Context context, AttributeSet attrs, int defStyleAttribute) {
        super(context, attrs, defStyleAttribute);

        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Locals
        final RectF drawBounds = drawRect;
        final Rect realBounds = realRect;
        final Path selectionPath = selection;
        final Layout layout = getLayout();

        // Figure last line index, and text offsets there
        final int lastLineIndex = getLineCount() - 1;
        final int lastLineStart = layout.getLineStart(lastLineIndex);
        final int lastLineEnd = layout.getLineEnd(lastLineIndex);

        // Let the Layout figure a Path that'd cover the last line text
        layout.getSelectionPath(lastLineStart, lastLineEnd, selectionPath);
        // Convert that Path to a RectF, which we can more easily modify
        selectionPath.computeBounds(drawBounds, false);

        // Naive text direction determination; may need refinement
        boolean isRtl =
                layout.getParagraphDirection(lastLineIndex) == Layout.DIR_RIGHT_TO_LEFT;

        // Narrow the bounds to just the fade length
        if (isRtl) {
            drawBounds.right = drawBounds.left + drawBounds.width() * FADE_LENGTH_FACTOR;
        } else {
            drawBounds.left = drawBounds.right - drawBounds.width() * FADE_LENGTH_FACTOR;
        }
        // Adjust for drawables and paddings
        drawBounds.offset(getTotalPaddingLeft(), getTotalPaddingTop());

        // Convert drawing bounds to real bounds to determine
        // if we need to do the fade, or a regular draw
        drawBounds.round(realBounds);
        realBounds.offset(-getScrollX(), -getScrollY());
        boolean needToFade = realBounds.intersects(getTotalPaddingLeft(), getTotalPaddingTop(),
                getWidth() - getTotalPaddingRight(), getHeight() - getTotalPaddingBottom());

        if (needToFade && getLineCount() >= 5) {
            // Adjust and set the Shader Matrix
            final Matrix shaderMatrix = matrix;
            shaderMatrix.reset();
            shaderMatrix.setScale(drawBounds.width(), 1f);
            if (isRtl) {
                shaderMatrix.postRotate(180f, drawBounds.width() / 2f, 0f);
            }
            shaderMatrix.postTranslate(drawBounds.left, drawBounds.top);
            shader.setLocalMatrix(shaderMatrix);

            // Save, and start drawing to an off-screen buffer
            final int saveCount;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                saveCount = canvas.saveLayer(null, null);
            } else {
                saveCount = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
            }

            // Let TextView draw itself to the buffer
            super.onDraw(canvas);

            // Draw the fade to the buffer, over the TextView content
            canvas.drawRect(drawBounds, paint);

            // Restore, and draw the buffer back to the Canvas
            canvas.restoreToCount(saveCount);
        } else {
            // Regular draw
            super.onDraw(canvas);
        }
    }
}