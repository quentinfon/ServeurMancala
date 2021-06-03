package ensi.model;

import java.io.Serializable;

public class GameData implements Serializable {

    public int playerTurn;

    public Joueur[] joueurs = new Joueur[2];

    public int[][] cases = new int[2][6];


}
