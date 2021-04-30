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

    //Object, style and text on square
    Rect square;
    private Paint rectPaint;
    private TextPaint rectText;

    //create the matrix with the cells
    Cell[][] cell = new Cell[10][10];

    //side length of the square
    int sideLength;

    //the mode in which we are
    public static boolean uncoverMode = true;

    //the flag = means the marked cell
    TextView flag;
    int flagNb = 0;

    //number of cell that have been marked or uncovered
    int uncoveredCell = 0;

    //stop the game when bomb is found
    TextView winner;
    boolean disablingTouch = false;
    boolean gameOver = false;

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

        //Set the background color
        setBackgroundColor(Color.WHITE);

        //initialize the matrix
        initCell();

        //place the mines into the matrix
        randomMine();
    }

    //initialization method of the matrix
    public void initCell() {
        for (int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                Cell c = new Cell();
                c.initCell();
                cell[i][j] = c;
            }
        }
    }

    //place 20 mines randomly in the matrix
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

    //check how many mines is there around one cell
    public int minesCount(int i, int j) {
        int count = 0;

        if(i < 9 && j < 9 && cell[i+1][j+1].mine)
            count++;
        if(j < 9 && cell[i][j+1].mine)
            count++;
        if(i > 0 && j < 9 && cell[i-1][j+1].mine)
            count++;
        if(i > 0 && cell[i-1][j].mine)
            count++;
        if(i > 0 && j > 0 && cell[i-1][j-1].mine)
            count++;
        if(j > 0 && cell[i][j-1].mine)
            count++;
        if(i < 9 && j > 0 && cell[i+1][j-1].mine)
            count++;
        if(i < 9 && cell[i+1][j].mine)
            count++;

        return count;
    }

    //stop when the game is won
    public void endGame() {
        //enabled the user to touch other cells
        disablingTouch = true;
        //print the winner message
        winner.setVisibility(VISIBLE);
    }

    //know the state of a cell
    //change the color of the cell
    //add test when needed
    //count also the number of marked and uncovered cells
    public String cellState(int i, int j, String str) {
        str = "";

        //marked cell
        if(cell[i][j].marked) {
            rectPaint.setColor(Color.YELLOW);
            uncoveredCell++;
        }
        //covered cell
        else if(!cell[i][j].unCovered) {
            rectPaint.setColor(Color.BLACK);
        }
        //uncovered cell with mine
        else if(cell[i][j].mine) {
            rectPaint.setColor(Color.RED);
            str = "M";
        }
        //uncover cell
        else {
            rectPaint.setColor(Color.LTGRAY);
            rectText.setColor(Color.DKGRAY);
            int count = minesCount(i, j);
            if(count != 0)
                str = toString().valueOf(count);
            uncoveredCell++;
        }
        return str;
    }

    //cell bursting
    public void bursting(int i, int j) {
        //test for each neighbour
        if(i < 9 && j < 9 && !cell[i+1][j+1].unCovered) {
            //if the cell nearby as no mine around it, the method is called recursively on its own neighbour
            if(minesCount(i + 1, j + 1) == 0) {
                cell[i + 1][j + 1].unCovered = true;
                bursting(i + 1, j + 1);
            }
            //if not, we just uncover the cell and stop there
            else
                cell[i + 1][j + 1].unCovered = true;
        }
        if(j < 9 && !cell[i][j+1].unCovered) {
            if(minesCount(i , j + 1) == 0) {
                cell[i][j + 1].unCovered = true;
                bursting(i, j + 1);
            }
            else
                cell[i][j + 1].unCovered = true;
        }
        if(i > 0 && j < 9 && !cell[i-1][j+1].unCovered) {
            if(minesCount(i - 1, j + 1) == 0) {
                cell[i - 1][j + 1].unCovered = true;
                bursting(i - 1, j + 1);
            }
            else
                cell[i - 1][j + 1].unCovered = true;
        }
        if(i > 0 && !cell[i-1][j].unCovered) {
            if(minesCount(i - 1, j) == 0) {
                cell[i - 1][j].unCovered = true;
                bursting(i - 1, j);
            }
            else
                cell[i - 1][j].unCovered = true;
        }
        if(i > 0 && j > 0 && !cell[i-1][j-1].unCovered) {
            if(minesCount(i - 1, j - 1) == 0) {
                cell[i - 1][j - 1].unCovered = true;
                bursting(i - 1, j - 1);
            }
            else
                cell[i - 1][j - 1].unCovered = true;
        }
        if(j > 0 && !cell[i][j-1].unCovered) {
            if(minesCount(i, j - 1) == 0) {
                cell[i][j - 1].unCovered = true;
                bursting(i, j - 1);
            }
            else
                cell[i][j - 1].unCovered = true;
        }
        if(i < 9 && j > 0 && !cell[i+1][j-1].unCovered) {
            if(minesCount(i + 1, j - 1) == 0) {
                cell[i + 1][j - 1].unCovered = true;
                bursting(i + 1, j - 1);
            }
            else
                cell[i + 1][j - 1].unCovered = true;
        }
        if(i < 9 && !cell[i+1][j].unCovered) {
            if(minesCount(i + 1, j) == 0) {
                cell[i + 1][j].unCovered = true;
                bursting(i + 1, j);
            }
            else
                cell[i + 1][j].unCovered = true;
        }
    }

    //method that handle the user clicks
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        //if it's possible to play
        if(!disablingTouch) {

            //get the pixels where the user clicked
            int x = (int) event.getX();
            int y = (int) event.getY();

            //get the action
            int action = event.getActionMasked();

            if (action == MotionEvent.ACTION_DOWN) {
                //check if the pixel is not out of range
                if (x < sideLength * 10 && y < sideLength * 10)
                {
                    //get the index for inside the matrix
                    int i = (int) event.getY() / sideLength;
                    int j = (int) event.getX() / sideLength;

                    //Uncover Mode
                    if(uncoverMode) {
                        //if not marked, the cell is uncovered
                        if(!cell[i][j].marked) {
                            cell[i][j].unCovered = true;
                            //if it's a mine, the game stops
                            if (cell[i][j].mine) {
                                disablingTouch = true;
                                gameOver = true;
                            }
                            //call the bursting method if there is no mines around
                            else if(minesCount(i, j) == 0 ) {
                                bursting(i, j);
                            }
                        }
                    }

                    //Marking Mode
                    else {
                        //if the cell is not marked
                        if(!cell[i][j].marked && !cell[i][j].unCovered) {
                            cell[i][j].marked = true; // set to true
                            flagNb++;
                        }
                        //if has to be de-marked
                        else {
                            cell[i][j].marked = false; // set to false
                            flagNb--;
                        }
                        //change the number of marked cells
                        flag.setText("⚑  " + toString().valueOf(flagNb));
                    }
                }
            }

            //refresh the draw
            postInvalidate();
            return true;
        }
        return false;
    }

    //draw the different square of the game
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // allocations per draw cycle
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        int contentWidth = getWidth() - paddingLeft - paddingRight;

        //bounds of squares to be drawn (10*10)
        int rectBounds = contentWidth/10;

        sideLength = rectBounds - 2 ;

        //paint instance for drawing the squares
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //create a square
        square = new Rect(10,10, sideLength, sideLength);

        //create a text on the square
        rectText = new TextPaint();

        rectText.setTextSize(70);
        rectText.setStyle(TextPaint.Style.FILL);
        //content of the text
        String str = "";

        //initialize the nb of uncovered and marked cells at each call
        uncoveredCell = 0;

        //draw the squares
        //draw ten lines
        for(int i = 0; i < 10; i++) {
            //draw a line of square at the i index
            for (int j = 0; j < 10; j++) {

                //save of the drawing origin to the stack
                canvas.save();

                //translate to draw a row of squares a position (j * rectBound, i*rectBounds)
                canvas.translate(j * rectBounds, i * rectBounds);

                //set the color of the text to black by default
                rectText.setColor(Color.BLACK);

                //case game over, display all the bombs
                if(gameOver && cell[i][j].mine) {
                    rectPaint.setColor(Color.RED);
                    str = "M";
                }
                //call the cellState method to know how to draw the square
                else
                    str = cellState(i, j, str);

                //draw the square
                canvas.drawRect(square, rectPaint);

                //write the text
                canvas.drawText(str, j + sideLength * 1/4, i + sideLength * 3/4, rectText);

                //restore, back to the origin
                canvas.restore();
            }
        }

        //if all the cells have been either marked or uncovered and that there is 20 flags
        //it means for sure that the winner has win
        //then stop the game by calling the endGame method
        if(uncoveredCell == 100 && flagNb == 20)
            endGame();
    }
}