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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Random;

public class GameView extends View {

    //initialize attributes

    //Rect object for square
    Rect square;
    //use for color and styling
    private Paint rectPaint;
    //use to add text
    private TextPaint rectText;

    //stop the game when bomb is found
    boolean disablingTouch = false;

    //the mode in which we are
    public static boolean uncoverMode = true;

    //initialize the matrix with the cells
    Cell[][] cell = new Cell[10][10];
    int sideLength;

    //flag number
    int flagNb = 0;

    TextView flag;

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
        initCell();

        //place the mines
        randomMine();

    }

    //initialize the matrix
    public void initCell() {
        for (int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                Cell c = new Cell();
                c.initCell();
                cell[i][j] = c;
            }
        }
    }

    //choose 20 mines randomly
    public void randomMine() {
        Random rand = new Random();
        int i, j;
        int k = 0;
        while(k < 20) {
            i = rand.nextInt(10);
            j = rand.nextInt(10);
            if(!cell[i][j].mine) {
                cell[i][j].mine = true;
                k++;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if(!disablingTouch) {

            int x = (int) event.getX(); // get the pixel x
            int y = (int) event.getY(); // get the pixel y

            int action = event.getActionMasked(); // get the action

            if (action == MotionEvent.ACTION_DOWN) {
                if (x < sideLength * 10 && y < sideLength * 10) // check if pixel is on the window
                {
                    int i = (int) event.getX() / sideLength; // get the index i
                    int j = (int) event.getY() / sideLength; // get the index j

                    //Uncover Mode
                    if(uncoverMode) {
                        if(!cell[j][i].marked) {
                            cell[j][i].unCovered = true; // set to true
                            if (cell[j][i].mine) {
                                disablingTouch = true;
                            }
                        }
                    }

                    //Marking Mode
                    else {
                        if(!cell[j][i].marked && !cell[j][i].unCovered) {
                            cell[j][i].marked = true; // set to true
                            flagNb++;
                            flag.setText("⚑  " + toString().valueOf(flagNb));
                        }
                        else {
                            cell[j][i].marked = false; // set to false
                            flagNb--;
                            flag.setText("⚑  " + toString().valueOf(flagNb));
                        }
                    }
                }
            }
            postInvalidate();
            return true;
        }
        return false;
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

        //Side length of the square (2 = distance between two squares)
        sideLength = rectBounds - 2 ;

        //Paint instance for drawing the squares
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //Create a rect which is actually a square
        square = new Rect(10,10, sideLength, sideLength);

        rectText = new TextPaint();

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
                if(cell[i][j].marked) {
                    rectPaint.setColor(Color.YELLOW);
                    canvas.drawRect(square, rectPaint);
                }
                else if(!cell[i][j].unCovered) {
                    rectPaint.setColor(Color.BLACK);
                    canvas.drawRect(square, rectPaint);
                }
                else if(cell[i][j].mine) {
                    rectPaint.setColor(Color.RED);
                    rectText.setTextSize(70);
                    rectText.setStyle(TextPaint.Style.FILL);
                    rectText.setColor(Color.BLACK);

                    //Draw the square
                    canvas.drawRect(square, rectPaint);

                    //Draw the text
                    canvas.drawText("M", j + sideLength * 1/4, i + sideLength * 3/4, rectText);

                }
                else {
                    rectPaint.setColor(Color.GRAY);
                    canvas.drawRect(square, rectPaint);
                }

                //Restore, back to the origin
                canvas.restore();

            }
        }
    }
}

