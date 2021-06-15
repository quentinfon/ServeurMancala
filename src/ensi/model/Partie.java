package ensi.model;


import java.util.Random;

public class Partie {

    public Plateau plateau;

    public int playerTurn;

    public Joueur[] joueurs = new Joueur[2];
    public boolean started;

    public int[] scores = new int[2];
    private Random rand;

    public Partie(){
        rand = new Random();
        init_partie();
    }

    public void startGame(Joueur j1, Joueur j2){
        joueurs[0] = j1;
        joueurs[1] = j2;
        started = true;
    }

    public void init_partie(){
        started = false;
        playerTurn = rand.nextInt(2);
        plateau = new Plateau(this);
        scores[0] = 0;
        scores[1] = 0;
    }

    public void nextPlayer(){
        playerTurn = playerTurn == 0 ? 1 : 0;
    }


    public boolean checkEnd(){

        if (plateau.sumCaseJoueur(playerTurn) == 0){
            scores[playerTurn] += plateau.getAllPlayerGraines((playerTurn+1)%2);
            return true;
        }

        return false;
    }


    public void playTurn(Joueur j, int cell){
        if(!started) return;

        if(j.equals(joueurs[playerTurn])){
            if (plateau.isPlayable(playerTurn, cell)){

                System.out.println("Fonction jouer la case : " + cell);
                plateau.playCell(playerTurn, cell);
                nextPlayer();

                if(checkEnd()) started = false;

            }else{
                System.out.println("Impossible de jouer cette case");
            }
        }else{
            System.out.println("Ce n'est pas le tour de ce client");
        }

    }

    public GameData getGameData(){
        GameData data = new GameData();

        data.playerTurn = this.playerTurn;
        data.joueurs = this.joueurs;
        data.cases = this.plateau.getCases();
        data.scores = this.scores;

        return data;
    }

    public int getScore(int joueur){
        if(joueur >= 0 && joueur <=1){
            return scores[joueur];
        }
        return -1;
    }

    public void setScore(int joueur, int score){
        if(joueur >= 0 && joueur <=1){
            scores[joueur] = score;
        }
    }


}
