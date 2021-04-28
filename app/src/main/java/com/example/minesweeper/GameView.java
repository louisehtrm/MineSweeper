package com.example.minesweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class GameView extends View {

    //initialize attributes

    //Rect object for square
    Rect square;
    //use for color and styling
    private Paint rectPaint;

    int sideLength;

    //initialize the matrix with the cells
    Cell[][] cell = new Cell[10][10];


    //the four constructors

    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    //initialization method
    private void init(AttributeSet attrs, int defStyle) {
        //Set the background color.
        setBackgroundColor(Color.WHITE);

        //initialize the matrix
        for (int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                Cell c = new Cell();
                c.initCell();
                cell[i][j] = c;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        int x = (int)event.getX(); // get the pixel x
        int y = (int)event.getY(); // get the pixel y

        int action = event.getActionMasked(); // get the action

        if (action == MotionEvent.ACTION_DOWN)
        {
            if(x < sideLength * 10 && y < sideLength * 10 ) // check if pixel is on the window
            {
                int i = (int)event.getX()/sideLength; // get the index i
                int j = (int)event.getY()/sideLength; // get the index j
                cell[j][i].unCovered = true; // set to true
            }
        }
        postInvalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // allocations per draw cycle
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        //Bounds of squares to be drawn (10*10)
        int rectBounds = contentWidth/10;

        //Side length of the square (10 = distance between two squares)
        sideLength = rectBounds - 10 ;

        //Paint instance for drawing the squares
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //rectPaint.setColor(Color.BLACK);

        //Create a rect which is actually a square
        square = new Rect(10,10, sideLength, sideLength);

        //Draw the squares
        //draw ten lines of ten squares
        for(int i = 0; i < 10; i++) {
            //draw a line of square at the i index
            for (int j = 0; j < 10; j++) {

                //Preliminary save of the drawing origin to the stack
                canvas.save();

                //Translate to draw a row of squares a position (j * rectBound, i*rectBounds)
                canvas.translate(j * rectBounds, i * rectBounds);

                //check the state for the color
                if(cell[i][j].unCovered)
                    rectPaint.setColor(Color.GRAY);
                else
                    rectPaint.setColor(Color.BLACK);

                //Draw the square
                canvas.drawRect(square, rectPaint);

                //Restore, back to the origin
                canvas.restore();

            }
        }
    }
}

