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

        customView = findViewById(R.id.customView);
        resetButton = findViewById(R.id.resetButton);
        modeButton = findViewById(R.id.typeMode);
        modeButton.setText("Marking");
        customView.flag = findViewById(R.id.flag);
        resetButton.setBackgroundColor(Color.parseColor("#2B3A4A"));
        modeButton.setBackgroundColor(Color.parseColor("#2B3A4A"));
        GameView.uncoverMode = true;
    }

    public void ResetButton(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void ModeButton(View view) {
        if(GameView.uncoverMode) {
            modeButton.setText("Uncover");
            GameView.uncoverMode = false;

        }
        else {
            modeButton.setText("Marking");
            GameView.uncoverMode = true;
        }

    }
}