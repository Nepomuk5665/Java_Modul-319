package ch.noseryoung.blj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class PongGame extends JPanel implements KeyListener, Runnable {
    // Spielfeld-Parameter
    private static final int WIDTH = 800, HEIGHT = 600;
    private static final int PADDLE_WIDTH = 20, PADDLE_HEIGHT = 100, BALL_SIZE = 20;
    private static final int SPEED = 5;

    // Spielobjekte
    private int paddle1Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int paddle2Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int ballX = WIDTH / 2, ballY = HEIGHT / 2;
    private int ballXSpeed = SPEED, ballYSpeed = SPEED;

    // Punkte
    private int score1 = 0, score2 = 0;

    public PongGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        // Thread für das Spiel
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);
    }

    private void drawGame(Graphics g) {
        // Spielfeld
        g.setColor(Color.WHITE);
        g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);

        // Schläger
        g.fillRect(10, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(WIDTH - 30, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Ball
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // Punkte
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(String.valueOf(score1), WIDTH / 4, 50);
        g.drawString(String.valueOf(score2), 3 * WIDTH / 4, 50);
    }

    @Override
    public void run() {
        while (true) {
            moveBall();
            checkCollisions();
            repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveBall() {
        ballX += ballXSpeed/2;
        ballY += ballYSpeed/2;

        // Wandkollision (oben und unten)
        if (ballY <= 0 || ballY >= HEIGHT - BALL_SIZE) {
            ballYSpeed = -ballYSpeed;
        }

        // Punkt erzielen
        if (ballX <= 0) {
            score2++;
            resetBall();
        } else if (ballX >= WIDTH - BALL_SIZE) {
            score1++;
            resetBall();
        }
    }

    private void checkCollisions() {
        // Ball trifft linken Schläger
        if (ballX <= 30 && ballY + BALL_SIZE >= paddle1Y && ballY <= paddle1Y + PADDLE_HEIGHT) {
            ballXSpeed = -ballXSpeed;
        }

        // Ball trifft rechten Schläger
        if (ballX >= WIDTH - 50 && ballY + BALL_SIZE >= paddle2Y && ballY <= paddle2Y + PADDLE_HEIGHT) {
            ballXSpeed = -ballXSpeed;
        }
    }

    private void resetBall() {
        ballX = WIDTH / 2;
        ballY = HEIGHT / 2;

        Random random = new Random();
        ballXSpeed = random.nextBoolean() ? SPEED : -SPEED;
        ballYSpeed = random.nextBoolean() ? SPEED : -SPEED;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Bewegung der Schläger
        if (e.getKeyCode() == KeyEvent.VK_W && paddle1Y > 0) {
            paddle1Y -= SPEED;
        }
        if (e.getKeyCode() == KeyEvent.VK_S && paddle1Y < HEIGHT - PADDLE_HEIGHT) {
            paddle1Y += SPEED;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && paddle2Y > 0) {
            paddle2Y -= SPEED;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && paddle2Y < HEIGHT - PADDLE_HEIGHT) {
            paddle2Y += SPEED;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void thePonggame() {
        JFrame frame = new JFrame("Pong Game");
        PongGame pong = new PongGame();

        frame.add(pong);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to exit the game?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (response == JOptionPane.YES_OPTION) {
                    frame.dispose();
                    Start.menu();
                }
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
