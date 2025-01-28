package ch.noseryoung.blj;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderBoard {
    private static final String FILE_NAME = "scores.txt";
    private List<Integer> scores;

    public LeaderBoard() {
        scores = new ArrayList<>();
        loadScores();
    }

    private void loadScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scores.add(Integer.parseInt(line));
            }
        } catch (IOException e) {
            System.out.println("No previous scores found.");
        }
    }

    public void saveScore(int score) {
        scores.add(score);
        Collections.sort(scores, Collections.reverseOrder()); // Sort descending

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Integer s : scores) {
                writer.write(s + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving score.");
        }
    }

    public List<Integer> getTopScores(int count) {
        return scores.subList(0, Math.min(count, scores.size()));
    }
}
