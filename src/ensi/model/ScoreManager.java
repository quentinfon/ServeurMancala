package ensi.model;

import java.io.*;
import java.util.ArrayList;

public class ScoreManager implements Serializable {

    private ArrayList<Score> scores = new ArrayList<>();

    public static ScoreManager scoreManager;

    public static ScoreManager getScoreManager(){
        if (scoreManager == null)
            scoreManager = new ScoreManager();
        return scoreManager;
    }

    public ScoreManager(){
        loadScores();
    }

    /**
     * Add a score to the score list
     * @param pseudo the pseudo of the winner
     * @param score the score of the winner
     */
    public void addScore(String pseudo, int score){
        if(scores.size() <= 100){
            scores.add(new Score(pseudo, score));
            saveScores();
        }
    }

    /**
     * Load the score from a file
     */
    public void loadScores(){
        File file = new File("mancala_save/scores.game");

        if(file.exists()){

            FileInputStream fis = null;
            ObjectInputStream ois = null;

            try {

                fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                this.scores = ((ScoreManager) ois.readObject()).getScores();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if(ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     * Save the scores into a file
     */
    public void saveScores(){

        File dir = new File("mancala_save/");
        dir.mkdirs();

        File f = new File("mancala_save/scores.game");
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Get the list of scores
     * @return the list of scores
     */
    public ArrayList<Score> getScores() {
        return scores;
    }
}
