package ensi.model;

import java.io.Serializable;

public class GameData implements Serializable {

    public int playerTurn;

    public Joueur[] joueurs = new Joueur[2];

    public int[][] cases = new int[2][6];

    public int[] scores = new int[2];

    public String toString(){

        StringBuilder affichage = new StringBuilder("");

        affichage.append("Joueurs : ").append("\n");

        affichage.append(joueurs[0]).append(" : ").append(scores[0]).append("\n").append(joueurs[1]).append(" : ").append(scores[1]).append("\n\n");

        affichage.append("Plateau :\n");

        for (int j = 0; j < 2; j ++){
            for (int i = 0; i < 6; i ++){
                affichage.append(cases[j][i]).append(" ");
            }
            affichage.append("\n");
        }

        affichage.append("\n").append("Player turn : ").append(playerTurn);

        return affichage.toString();
    }

}
