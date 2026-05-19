package com.example.sudoku;

import java.util.Random;

public class Sudoku {
    private int[][] board;
    private boolean[][] fixedCells;
    private boolean[][] wrongCells;
    private int selected_row;
    private int selected_column;

    // ===============================
    // ===== INICIALIZACIJA IGRE =====
    // ===============================
    public Sudoku() {
        board = new int[9][9];
        fixedCells = new boolean[9][9];
        wrongCells = new boolean[9][9];
        selected_row = -1;
        selected_column = -1;
        generateSudoku();
    }

    public void resetGame() {
        board = new int[9][9];
        fixedCells = new boolean[9][9];
        wrongCells = new boolean[9][9];
        selected_row = -1;
        selected_column = -1;
        generateSudoku();
    }

    // ================================
    // ===== GENERIRANJE SUDOKUJA =====
    // ================================
    public void generateSudoku() {
        fillValues();
    }

    private void fillValues() {

        fillDiagonal();
        fillRemaining(0, 3);

        // Odstrani k-stevil iz boarda
        removeKDigits(40);

        // Shrani zapisane številke v celice
        saveFixedCells();
    }

    // Polnimo boxe po diagonali, ker se ne prekrivajo
    private void fillDiagonal() {
        for (int i = 0; i < 9; i += 3)
            fillBox(i, i);
    }

    // Napolni posamezen box
    private void fillBox(int row, int col) {
        int num;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                do {
                    num = randomGenerator(9);
                } while (!unUsedInBox(row, col, num));
                board[row + i][col + j] = num;
            }
        }
    }
    private int randomGenerator(int num) {
        Random rand = new Random();
        return rand.nextInt(num) + 1;
    }

    // Rekurzivno napolnimo do konca
    private boolean fillRemaining(int i, int j) {
        // Premik v naslednjo vrstico
        if (j >= 9 && i < 9 - 1) {
            i = i + 1;
            j = 0;
        }
        // Če smo prišli do konca je sudoku končan
        if (i >= 9 && j >= 9)
            return true;

        // Preskočimo diagonalne boxe
        if (i < 3) {
            if (j < 3)
                j = 3;
        } else if (i < 9 - 3) {
            if (j == (int) (i / 3) * 3)
                j = j + 3;
        } else {
            if (j == 9 - 3) {
                i = i + 1;
                j = 0;
                if (i >= 9)
                    return true;
            }
        }

        // Vstavi števila od 1 do 9
        for (int num = 1; num <= 9; num++) {
            if (isValid(i, j, num)) { // Če število ni vpisano v vrstici, stolpcu ali boxu
                board[i][j] = num;
                if (fillRemaining(i, j + 1)) // Rekurzivno nadaljuje
                    return true;
                board[i][j] = 0; // Backtracking: če rešitev ne deluje, resetiraj polje
            }
        }
        return false;
    }

    public void removeKDigits(int k) {
        int count = k;
        while (count != 0) {
            int cellId = randomGenerator(81) - 1;
            int i = (cellId / 9);
            int j = cellId % 9;

            if (board[i][j] != 0) {
                count--;
                board[i][j] = 0;
            }
        }
    }

    private void saveFixedCells() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                // Če je številka obstajala pred brisanjem
                fixedCells[r][c] = board[r][c] != 0;
            }
        }
    }


    // ==============================
    // ===== PREVERJANJE PRAVIL =====
    // ==============================

    // Preveri če je številka veljavna v board[i][j]
    public boolean isValid(int i, int j, int num) {
        return (unUsedInRow(i, num) &&
                unUsedInCol(j, num) &&
                unUsedInBox(i - i % 3, j - j % 3, num));
    }

    private boolean unUsedInRow(int i, int num) {
        for (int j = 0; j < 9; j++)
            if (board[i][j] == num)
                return false;
        return true;
    }

    private boolean unUsedInCol(int j, int num) {
        for (int i = 0; i < 9; i++)
            if (board[i][j] == num)
                return false;
        return true;
    }

    private boolean unUsedInBox(int rowStart, int colStart, int num) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[rowStart + i][colStart + j] == num)
                    return false;
        return true;
    }

    // ========================
    // ===== VNOS IGRALCA =====
    // ========================

    // Nastavi število v izbran kvadratek
    public void setNumberPos(int num) {
        if (selected_row == -1 || selected_column == -1) return;

        int row = selected_row - 1;
        int col = selected_column - 1;

        if (fixedCells[row][col]) return;

        // ERASE
        if (num == 0) {
            board[row][col] = 0;
            wrongCells[row][col] = false;
            return;
        }

        // preveri pravilnost
        boolean ok = isValid(row, col, num);

        // vrni vrednost nazaj
        board[row][col] = num;

        wrongCells[row][col] = !ok;
    }

    // =======================
    // ===== STANJE IGRE =====
    // =======================
    public boolean isSolved() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {

                if (board[r][c] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // ============================
    // ===== GETTER IN SETTER =====
    // ============================
    public boolean[][] getWrongCells() {
        return wrongCells;
    }

    public boolean[][] getFixedCells(){
        return fixedCells;
    }

    public int[][] getBoard() {
        return board;
    }

    public int getSelected_row() {
        return selected_row;
    }

    public int getSelected_column() {
        return selected_column;
    }

    public void setSelected_row(int r) {
        selected_row = r;
    }

    public void setSelected_column(int c) {
        selected_column = c;
    }
}