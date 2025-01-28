package ch.noseryoung.blj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MineSweeper extends JFrame {
    // Konstanten zur Festlegung der Spielfeldgröße und Anzahl der Minen
    private static final int SIZE = 10;
    private static final int MINES = 15;
    private static final char MINE = '*';
    private static final char EMPTY = '.';

    // Arrays für die Buttons, das Spielfeld und die aufgedeckten Felder
    private JButton[][] buttons = new JButton[SIZE][SIZE];
    private char[][] board = new char[SIZE][SIZE];
    private boolean[][] revealed = new boolean[SIZE][SIZE];

    public MineSweeper() {
        setTitle("MineSweeper"); // Titel des Fensters setzen
        setSize(500, 500); // Fenstergröße setzen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Anwendung beenden beim Schließen
        setLayout(new GridLayout(SIZE, SIZE)); // Layout für die Buttons setzen

        initializeBoard(); // Spielfeld initialisieren
        placeMines(); // Minen platzieren
        calculateNumbers(); // Zahlen basierend auf umliegenden Minen berechnen
        createButtons(); // Buttons erstellen und hinzufügen
    }

    // Spielfeld mit leeren Feldern initialisieren
    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
                revealed[i][j] = false;
            }
        }
    }

    // Zufälliges Platzieren der Minen auf dem Spielfeld
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

    // Berechnung der Zahlen für Felder, die keine Minen sind
    private void calculateNumbers() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == MINE) continue; // Minenfelder überspringen

                int minesCount = 0;
                // Überprüfung der Nachbarfelder
                for (int r = -1; r <= 1; r++) {
                    for (int c = -1; c <= 1; c++) {
                        int nr = i + r, nc = j + c;
                        if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE && board[nr][nc] == MINE) {
                            minesCount++;
                        }
                    }
                }

                // Zahl setzen, falls umliegende Minen vorhanden sind
                if (minesCount > 0) {
                    board[i][j] = (char) ('0' + minesCount);
                }
            }
        }
    }

    // Erstellung der Buttons und Hinzufügen von ActionListenern
    private void createButtons() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 14)); // Schriftart setzen
                buttons[i][j].addActionListener(new ButtonClickListener(i, j)); // Klick-Listener hinzufügen
                add(buttons[i][j]); // Button zum Layout hinzufügen
            }
        }
    }

    // Innere Klasse für die Behandlung von Button-Klicks
    private class ButtonClickListener implements ActionListener {
        private int row, col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (board[row][col] == MINE) { // Wenn auf eine Mine geklickt wurde
                revealAllMines(); // Alle Minen aufdecken
                JOptionPane.showMessageDialog(null, "Game Over! You hit a mine."); // Nachricht anzeigen
                playAgain(); // Abfrage für neues Spiel
            } else {
                reveal(row, col); // Feld aufdecken
            }
        }
    }

    // Methode zum Aufdecken eines Feldes
    private void reveal(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || revealed[row][col]) {
            return; // Abbruch, falls außerhalb des Spielfelds oder schon aufgedeckt
        }

        revealed[row][col] = true; // Feld als aufgedeckt markieren
        buttons[row][col].setText(board[row][col] == EMPTY ? "" : String.valueOf(board[row][col])); // Text setzen
        buttons[row][col].setEnabled(false); // Button deaktivieren

        // Wenn das Feld leer ist, Nachbarfelder rekursiv aufdecken
        if (board[row][col] == EMPTY) {
            for (int r = -1; r <= 1; r++) {
                for (int c = -1; c <= 1; c++) {
                    reveal(row + r, col + c);
                }
            }
        }
    }

    // Alle Minen aufdecken
    private void revealAllMines() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == MINE) {
                    buttons[i][j].setText("*");
                }
            }
        }
    }

    // Abfrage, ob ein neues Spiel gestartet werden soll
    public void playAgain() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Do you want to play again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) { // Neues Spiel starten
            dispose();
            theMineSweeperGame();
        } else { // Programm beenden oder ins Menü zurückkehren
            dispose();
            Start.menu();
        }
    }

    // Hauptmethode zum Starten des Spiels
    public static void theMineSweeperGame() {
        SwingUtilities.invokeLater(() -> {
            MineSweeper game = new MineSweeper();
            game.setVisible(true);
        });
    }
}
