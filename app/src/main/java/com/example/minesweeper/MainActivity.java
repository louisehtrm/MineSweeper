package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    GameView customView;

    Button resetButton;
    Button modeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //the custom view
        customView = findViewById(R.id.customView);

        //the two buttons
        resetButton = findViewById(R.id.resetButton);
        modeButton = findViewById(R.id.typeMode);

        //color of the buttons and its text
        resetButton.setBackgroundColor(Color.parseColor("#2B3A4A"));
        modeButton.setBackgroundColor(Color.parseColor("#2B3A4A"));
        modeButton.setText("Marking");

        //TextView to display the nb of marked cells
        customView.flag = findViewById(R.id.flag);

        //the mode of the game: marking or uncover
        GameView.uncoverMode = true;

        //TextView displayed when the user wins
        customView.winner = findViewById(R.id.winner);
    }

    public void ResetButton(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void ModeButton(View view) {
        //chang to marked mode
        if(GameView.uncoverMode) {
            modeButton.setText("Uncover");
            GameView.uncoverMode = false;
        }
        //change to uncovered mode
        else {
            modeButton.setText("Marking");
            GameView.uncoverMode = true;
        }
    }
}