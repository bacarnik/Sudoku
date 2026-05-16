package com.example.sudoku;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Povezava na SudokuBoard iz XML
        SudokuBoard sudokuBoard = findViewById(R.id.sudokuBoard);

        // Gumbi
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);
        Button button7 = findViewById(R.id.button7);
        Button button8 = findViewById(R.id.button8);
        Button button9 = findViewById(R.id.button9);
        Button eraseButton = findViewById(R.id.buttonErase);

        // Click listenerji
        button1.setOnClickListener(v -> sudokuBoard.setNumberPos(1));
        button2.setOnClickListener(v -> sudokuBoard.setNumberPos(2));
        button3.setOnClickListener(v -> sudokuBoard.setNumberPos(3));
        button4.setOnClickListener(v -> sudokuBoard.setNumberPos(4));
        button5.setOnClickListener(v -> sudokuBoard.setNumberPos(5));
        button6.setOnClickListener(v -> sudokuBoard.setNumberPos(6));
        button7.setOnClickListener(v -> sudokuBoard.setNumberPos(7));
        button8.setOnClickListener(v -> sudokuBoard.setNumberPos(8));
        button9.setOnClickListener(v -> sudokuBoard.setNumberPos(9));
        eraseButton.setOnClickListener(v -> {
            sudokuBoard.setNumberPos(0);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}