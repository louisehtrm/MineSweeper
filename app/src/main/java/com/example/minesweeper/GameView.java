package com.example.minesweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class GameView extends View {

    //initialize attributes
    private Paint rectPaint;
    Rect square;

    //the four constructors

    public GameView(Context context) {
        super(context);
        init(null);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);

    }

    //initialization method
    private void init(AttributeSet attrs) {
        //Set the background color.
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        //Bounds of squares to be drawn (10*10)
        int rectBounds = contentWidth/10;

        //Side length of the square (5 = distance between two squares)
        int sideLength = rectBounds -5 ;

        //Paint instance for drawing the squares.
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(Color.BLACK);

        //Create a rect which is actually a square.
        square = new Rect(10,10, sideLength, sideLength);

        //Draw the squares
        //draw ten lines of ten squares
        for(int i = 0; i < 10; i++) {
            //draw a line of square at the i index
            for (int j = 0; j < 10; j++) {

                //Preliminary save of the drawing origin to the stack.
                canvas.save();

                //Translate to draw a row of squares. Firs will be at (0,0).
                canvas.translate(j * rectBounds, i * rectBounds);
                //Draw it.
                canvas.drawRect(square, rectPaint);

                //Restore. Back to the origin.
                canvas.restore();

            }
        }
    }
}

