package ch.noseryoung.blj;

// Import der nötigen Pakete
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.List;

// Erstellt das Spielfeld und die Spiellogik
public class SnakeGame extends JPanel implements ActionListener {
    private final int TILE_SIZE = 25; // Größe eines Spielfeld-Tiles
    private final int WIDTH = 600; // Breite des Spielfelds
    private final int HEIGHT = 600; // Höhe des Spielfelds
    private final int TOTAL_TILES = (WIDTH * HEIGHT) / (TILE_SIZE * TILE_SIZE); // Gesamte Anzahl der Tiles

    private final int x[] = new int[TOTAL_TILES]; // X-Koordinaten der Schlange
    private final int y[] = new int[TOTAL_TILES]; // Y-Koordinaten der Schlange
    private int bodyParts = 5; // Startgröße der Schlange
    private int applesEaten; // Anzahl der gegessenen Äpfel
    private int appleX; // X-Koordinate des Apfels
    private int appleY; // Y-Koordinate des Apfels
    private static char direction = 'R'; // Startbewegungsrichtung (Rechts)
    private boolean running = false; // Gibt an, ob das Spiel läuft
    private Timer timer; // Timer für das Spiel
    private Random random; // Zufallsgenerator

    public SnakeGame() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT)); // Setzt die Größe des Spielfelds
        this.setBackground(Color.BLACK); // Hintergrundfarbe setzen
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter()); // Hinzufügen eines KeyAdapters zur Steuerung
        startGame(); // Spiel starten
    }

    // Startet das Spiel
    public void startGame() {
        spawnSnake(); // Schlange in der Mitte spawnen
        newApple(); // Erzeugt den ersten Apfel
        running = true;
        timer = new Timer(250, this); // Setzt den Timer für die Bewegungen
        timer.start();
    }

    // Platziert die Schlange in der Mitte des Spielfelds
    public void spawnSnake() {
        int startX = WIDTH / 2 - (WIDTH / 2 % TILE_SIZE);
        int startY = HEIGHT / 2 - (HEIGHT / 2 % TILE_SIZE);
        for (int i = 0; i < bodyParts; i++) {
            x[i] = startX - i * TILE_SIZE;
            y[i] = startY;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // Zeichnet das Spielfeld und die Schlange
    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.RED); // Farbe des Apfels
            g.fillOval(appleX, appleY, TILE_SIZE, TILE_SIZE); // Zeichnet den Apfel

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN); // Kopf der Schlange
                } else {
                    g.setColor(new Color(45, 180, 0)); // Körper der Schlange
                }
                g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
            }

            g.setColor(Color.WHITE); // Schriftfarbe
            g.setFont(new Font("Ink Free", Font.BOLD, 30)); // Schriftart setzen
            g.drawString("Score: " + applesEaten * 100, 10, 30); // Punktestand anzeigen
        } else {
            gameOver(g); // Spielende-Bildschirm anzeigen
        }
    }

    // Erstellt einen neuen Apfel an einer zufälligen Position
    public void newApple() {
        appleX = random.nextInt((WIDTH / TILE_SIZE)) * TILE_SIZE;
        appleY = random.nextInt((HEIGHT / TILE_SIZE)) * TILE_SIZE;
    }

    // Bewegt die Schlange basierend auf der aktuellen Richtung
    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] -= TILE_SIZE;
                break;
            case 'D':
                y[0] += TILE_SIZE;
                break;
            case 'L':
                x[0] -= TILE_SIZE;
                break;
            case 'R':
                x[0] += TILE_SIZE;
                break;
        }
    }

    // Überprüft, ob die Schlange den Apfel gefressen hat
    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
            int newDelay = Math.max(50, 150 - (applesEaten * 5)); // Reduziert die Verzögerung, wenn mehr Äpfel gegessen werden
            timer.setDelay(newDelay);
        }
    }

    // Überprüft, ob die Schlange mit sich selbst oder der Wand kollidiert
    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    // Zeigt den "Game Over"-Bildschirm an
    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.PLAIN, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);

        g.setFont(new Font("Ink Free", Font.PLAIN, 30));
        g.drawString("Score: " + applesEaten * 100, (WIDTH - metrics.stringWidth("Score: " + applesEaten * 100)) / 2, HEIGHT / 2 + 50);

        LeaderBoard leaderboard = new LeaderBoard();
        leaderboard.saveScore(applesEaten * 100);
        showHighScores();

        playAgain();
    }

    // Fragt, ob der Spieler erneut spielen möchte
    public void playAgain() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Do you want to play again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            theSnakeGame();
        } else {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            try {
                Start.menu();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    // Erweiterte Steuerung der Schlange durch Tasteneingaben
    public static class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D';
                    break;
            }
        }
    }

    // Zeigt die besten Punktzahlen an
    public void showHighScores() {
        LeaderBoard leaderboard = new LeaderBoard();
        List<Integer> topScores = leaderboard.getTopScores(5);

        StringBuilder message = new StringBuilder("Top 5 Scores:\n");
        for (int i = 0; i < topScores.size(); i++) {
            message.append((i + 1)).append(". ").append(topScores.get(i)).append("\n");
        }

        JOptionPane.showMessageDialog(this, message.toString(), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }

    // Startet das Snake-Spiel
    public static void theSnakeGame() {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
