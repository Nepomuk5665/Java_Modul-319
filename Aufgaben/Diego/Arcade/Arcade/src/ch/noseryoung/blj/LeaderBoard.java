package ch.noseryoung.blj;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderBoard {
    private static final String FILE_NAME = "scores.txt"; // Name der Datei, in der die Punktzahlen gespeichert werden
    private List<Integer> scores; // Liste zur Speicherung der Punktzahlen

    public LeaderBoard() {
        scores = new ArrayList<>(); // Initialisierung der Punktzahlenliste
        loadScores(); // Laden der gespeicherten Punktzahlen
    }

    // Methode zum Laden der Punktzahlen aus der Datei
    private void loadScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) { // Zeilenweise Einlesen der Datei
                scores.add(Integer.parseInt(line)); // Punktzahl in die Liste einfügen
            }
        } catch (IOException e) {
            System.out.println("No previous scores found."); // Meldung, falls keine Datei gefunden wird
        }
    }

    // Methode zum Speichern einer neuen Punktzahl
    public void saveScore(int score) {
        scores.add(score); // Neue Punktzahl zur Liste hinzufügen
        Collections.sort(scores, Collections.reverseOrder()); // Liste in absteigender Reihenfolge sortieren

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Integer s : scores) {
                writer.write(s + "\n"); // Punktzahl in die Datei schreiben
            }
        } catch (IOException e) {
            System.out.println("Error saving score."); // Fehlermeldung bei Schreibfehler
        }
    }

    // Methode zum Abrufen der Top-Punktzahlen
    public List<Integer> getTopScores(int count) {
        return scores.subList(0, Math.min(count, scores.size())); // Zurückgeben der besten Punktzahlen
    }
}
