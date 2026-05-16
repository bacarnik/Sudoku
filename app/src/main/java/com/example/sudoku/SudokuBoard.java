package com.example.sudoku;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class SudokuBoard extends View {
    private final int boardColor;
    private final int cellFillColor;
    private final int cellsHighlightColor;
    private final Paint wrongCellPaint = new Paint();
    private final Paint boardColorPaint = new Paint();
    private final Paint cellFillColorPaint = new Paint();
    private final Paint cellsHighlightColorPaint = new Paint();
    private final Paint letterPaint = new Paint();
    private final Paint userLetterPaint = new Paint();
    private final Rect letterPaintBounds = new Rect();
    private int cellSize;
    private final Sudoku s = new Sudoku();


    public SudokuBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SudokuBoard, 0, 0);
        try {
            boardColor = a.getColor(R.styleable.SudokuBoard_boardColor, 0xFF000000);
            cellFillColor = a.getColor(R.styleable.SudokuBoard_cellFillColor, 0xFFFFFFFF);
            cellsHighlightColor = a.getColor(R.styleable.SudokuBoard_cellsHighlightColor, 0x220000FF);
        } finally {
            a.recycle();
        }

        letterPaint.setAntiAlias(true);
        letterPaint.setColor(boardColor);

        userLetterPaint.setAntiAlias(true);
        userLetterPaint.setColor(0xFF0000FF); // modra

        wrongCellPaint.setColor(0x55FF0000); // prosojno rdeča
        wrongCellPaint.setStyle(Paint.Style.FILL);

    }

    public boolean checkWin() {
        return s.isSolved();
    }

    private void showWinDialog() {
        new android.app.AlertDialog.Builder(getContext())
                .setTitle("🎉 Congratulations!")
                .setMessage("You solved the Sudoku!")
                .setCancelable(false)
                .setPositiveButton("New Game", (dialog, which) -> {

                    s.resetGame();
                    invalidate();
                })
                .show();
    }

    // Nastavi število (se izvede v MainActivity ob kliku na gumb)
    public void setNumberPos(int num) {
        s.setNumberPos(num);

        if (checkWin()) {
            showWinDialog();
        }

        // Ponovno nariše tablo
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int dimension = Math.min(width, height);

        cellSize = dimension / 9;

        setMeasuredDimension(dimension, dimension);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(16);
        boardColorPaint.setColor(boardColor);
        boardColorPaint.setAntiAlias(true);

        cellFillColorPaint.setStyle(Paint.Style.FILL);
        cellFillColorPaint.setAntiAlias(true);
        cellFillColorPaint.setColor(cellFillColor);

        cellsHighlightColorPaint.setStyle(Paint.Style.FILL);
        cellsHighlightColorPaint.setAntiAlias(true);
        cellsHighlightColorPaint.setColor(cellsHighlightColor);

        colorCell(canvas, s.getSelected_row(), s.getSelected_column());
        canvas.drawRect(0, 0, getWidth(), getHeight(), boardColorPaint);
        drawBoard(canvas);
        drawNumbers(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            s.setSelected_row((int) Math.ceil(y / cellSize));
            s.setSelected_column((int) Math.ceil(x / cellSize));
            invalidate();
            return true;
        }
        return false;
    }

    private void colorCell(Canvas canvas, int r, int c) {
        if (s.getSelected_column() != -1 && s.getSelected_row() != -1) {
            canvas.drawRect((c - 1) * cellSize, 0, c * cellSize, cellSize * 9, cellsHighlightColorPaint);
            canvas.drawRect(0, (r - 1) * cellSize, cellSize * 9, cellSize * r, cellsHighlightColorPaint);
            canvas.drawRect((c - 1) * cellSize, (r - 1) * cellSize, c * cellSize, r * cellSize, cellsHighlightColorPaint);
        }
    }

    private void drawThickLine() {
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(10);
        boardColorPaint.setColor(boardColor);
    }

    private void drawThinLine() {
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(4);
        boardColorPaint.setColor(boardColor);
    }

    private void drawBoard(Canvas canvas) {
        for (int c = 0; c < 10; c++) {
            if (c % 3 == 0) {
                drawThickLine();
            } else {
                drawThinLine();
            }
            canvas.drawLine(cellSize * c, 0, cellSize * c, getWidth(), boardColorPaint);
        }

        for (int r = 0; r < 10; r++) {
            if (r % 3 == 0) {
                drawThickLine();
            } else {
                drawThinLine();
            }
            canvas.drawLine(0, cellSize * r, getWidth(), cellSize * r, boardColorPaint);
        }
    }

    private void drawNumbers(Canvas canvas) {
        letterPaint.setTextSize(cellSize * 0.7f);
        int[][] board = s.getBoard(); // Dobi lokacije stevil
        boolean[][] wrongCells = s.getWrongCells();
        boolean[][] fixedCells = s.getFixedCells();

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] != 0) {
                    String text = Integer.toString(board[r][c]);
                    letterPaint.getTextBounds(text, 0, text.length(), letterPaintBounds);
                    float x = (c * cellSize) + (cellSize - letterPaintBounds.width()) / 2f - letterPaintBounds.left;
                    float y = (r * cellSize) + (cellSize + letterPaintBounds.height()) / 2f - letterPaintBounds.bottom;

                    if (wrongCells[r][c]) {
                        canvas.drawRect(
                                c * cellSize,
                                r * cellSize,
                                (c + 1) * cellSize,
                                (r + 1) * cellSize,
                                wrongCellPaint
                        );
                    }

                    // Originalne številke
                    if (fixedCells[r][c]) {
                        canvas.drawText(text, x, y, letterPaint);
                    } else {
                        // Uporabniško vnesene številke
                        userLetterPaint.setTextSize(cellSize * 0.7f);
                        canvas.drawText(text, x, y, userLetterPaint);
                    }
                }
            }
        }
    }
}
