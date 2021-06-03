package ensi.model;

import jdk.jshell.ImportSnippet;

import java.io.Serializable;

public class Partie implements Serializable {

    private Plateau plateau;

    private int playerTurn;

    private Joueur[] joueurs = new Joueur[2];

    public Partie(Joueur j1, Joueur j2){
        playerTurn = 0;

        joueurs[0] = j1;
        joueurs[1] = j2;

        plateau = new Plateau();
    }


    public void nextPlayer(){
        playerTurn = playerTurn == 0 ? 1 : 0;
    }


    public boolean checkEnd(){
        return plateau.sumCaseJoueur(playerTurn) == 0;
    }


    public void playTurn(Joueur j, int cell){

        if(j.equals(joueurs[playerTurn])){
            if (plateau.isPlayable(playerTurn, cell)){

                System.out.println("Fonction jouer");
                nextPlayer();

            }else{
                System.out.println("Impossible de jouer cette case");
            }
        }else{
            System.out.println("Ce n'est pas le tour de ce client");
        }

    }



}
