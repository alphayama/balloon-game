package alphayama.example.highscoreapp;

import java.util.Date;

// class for High Score with 3 members and respective getters and setters
public class HighScore {
    private String playerName;
    private int score;
    private String date;

    public HighScore(String playerName, int score, String date) {
        this.playerName = playerName;
        this.score = score;
        this.date = date;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
