package ensi.model;

import java.io.Serializable;

public class Score implements Serializable {

    public int score;
    public String winnerName;

    public Score(String pseudo, int score){
        this.score = score;
        this.winnerName = pseudo;
    }
}
