package ch.noseryoung.blj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MineSweeper extends JFrame {
    private static final int SIZE = 10;
    private static final int MINES = 15;
    private static final char MINE = '*';
    private static final char EMPTY = '.';
    private JButton[][] buttons = new JButton[SIZE][SIZE];
    private char[][] board = new char[SIZE][SIZE];
    private boolean[][] revealed = new boolean[SIZE][SIZE];

    public MineSweeper() {
        setTitle("MineSweeper");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(SIZE, SIZE));

        initializeBoard();
        placeMines();
        calculateNumbers();
        createButtons();
    }

    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
                revealed[i][j] = false;
            }
        }
    }

    private void placeMines() {
        Random rand = new Random();
        int count = 0;
        while (count < MINES) {
            int row = rand.nextInt(SIZE);
            int col = rand.nextInt(SIZE);
            if (board[row][col] != MINE) {
                board[row][col] = MINE;
                count++;
            }
        }
    }

    private void calculateNumbers() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == MINE) continue;
                int minesCount = 0;
                for (int r = -1; r <= 1; r++) {
                    for (int c = -1; c <= 1; c++) {
                        int nr = i + r, nc = j + c;
                        if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE && board[nr][nc] == MINE) {
                            minesCount++;
                        }
                    }
                }
                if (minesCount > 0) {
                    board[i][j] = (char) ('0' + minesCount);
                }
            }
        }
    }

    private void createButtons() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 14));
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                add(buttons[i][j]);
            }
        }
    }

    private class ButtonClickListener implements ActionListener {
        private int row, col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (board[row][col] == MINE) {
                revealAllMines();
                JOptionPane.showMessageDialog(null, "Game Over! You hit a mine.");
                playAgain();
            } else {
                reveal(row, col);
            }
        }
    }

    private void reveal(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || revealed[row][col]) {
            return;
        }
        revealed[row][col] = true;
        buttons[row][col].setText(board[row][col] == EMPTY ? "" : String.valueOf(board[row][col]));
        buttons[row][col].setEnabled(false);

        if (board[row][col] == EMPTY) {
            for (int r = -1; r <= 1; r++) {
                for (int c = -1; c <= 1; c++) {
                    reveal(row + r, col + c);
                }
            }
        }
    }

    private void revealAllMines() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == MINE) {
                    buttons[i][j].setText("*");
                }
            }
        }
    }

    public void playAgain() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Do you want to play again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {
            dispose();
            theMineSweeperGame();
        } else {
            dispose();
            Start.menu();
        }
    }

    public static void theMineSweeperGame() {
        SwingUtilities.invokeLater(() -> {
            MineSweeper game = new MineSweeper();
            game.setVisible(true);
        });
    }

}
