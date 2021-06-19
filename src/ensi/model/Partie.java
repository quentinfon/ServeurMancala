package ensi.model;


import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Partie {

    public Plateau plateau;

    public int playerTurn;

    public Joueur[] joueurs = new Joueur[2];
    public boolean started;

    public int[] scores = new int[2];
    private Random rand;

    public int[] victories = new int[2];

    public Joueur roundWinner;

    private ArrayList<Integer> moves;
    private int firstPlayer;

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
        firstPlayer = playerTurn;
        moves = new ArrayList<>();
    }

    public void nextGame(){
        started = true;
        playerTurn = rand.nextInt(2);
        plateau = new Plateau(this);
        scores[0] = 0;
        scores[1] = 0;
        firstPlayer = playerTurn;
        moves = new ArrayList<>();
    }

    public void nextPlayer(){
        playerTurn = playerTurn == 0 ? 1 : 0;
    }


    public boolean checkEnd(){

        if (plateau.sumCaseJoueur(playerTurn) == 0){
            scores[playerTurn] += plateau.getAllPlayerGraines((playerTurn+1)%2);

            int gagnant = scores[playerTurn] > scores[(playerTurn+1)%2] ? playerTurn : (playerTurn+1)%2;
            roundWinner = joueurs[gagnant];
            addVictory(gagnant, scores[gagnant]);
            return true;
        }
        if (scores[playerTurn] >= 25){
            roundWinner = joueurs[playerTurn];
            addVictory(playerTurn, scores[playerTurn]);
            return true;
        }

        //match null
        if (scores[playerTurn] <= 24 && scores[(playerTurn+1)%2] <= 24 && plateau.sumBoard() < 6){
            roundWinner = null;
            return true;
        }

        return false;
    }

    public void addVictory(int player, int score){
        victories[player] += 1;
        ScoreManager.getScoreManager().addScore(joueurs[player].pseudo, score);
    }

    public boolean checkDefinitivEnd(){
        if(victories[playerTurn] == 6){
            return true;
        }
        return false;
    }


    /**
     * Function to play
     * @param j the player
     * @param cell the cell
     * @return if the play has been done
     */
    public boolean playTurn(Joueur j, int cell){
        if(!started) return false;

        if(j.equals(joueurs[playerTurn])){
            if (plateau.isPlayable(playerTurn, cell)){

                System.out.println("Fonction jouer la case : " + cell);
                plateau.playCell(playerTurn, cell);
                moves.add(cell);
                return true;

            }else{
                System.out.println("Impossible de jouer cette case");
            }
        }else{
            System.out.println("Ce n'est pas le tour de ce client");
        }

        return false;
    }

    public GameData getGameData(){
        GameData data = new GameData();

        data.playerTurn = this.playerTurn;
        data.joueurs = this.joueurs;
        data.cases = this.plateau.getCases();
        data.scores = this.scores;
        data.victories = this.victories;

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


    public int getVictories(int joueur){
        if(joueur >= 0 && joueur <=1){
            return victories[joueur];
        }
        return -1;
    }

    /**
     * Function to surrender a round
     * @param joueur the player that want surrend
     * @return true if have successfully surrender
     */
    public boolean surrender(Joueur joueur){

        if (joueurs[playerTurn].equals(joueur)){
            roundWinner = joueurs[(playerTurn+1)%2];
            addVictory((playerTurn+1)%2, scores[(playerTurn+1)%2]);
            return true;
        }

        return false;
    }


    public void load(GameData data){
        scores = data.scores;
        victories =data.victories;
        playerTurn = data.playerTurn;
        plateau.setCases(data.cases);
    }

    public void saveGame(){

        File dir = new File("mancala_save/");
        dir.mkdirs();

        File f = new File("mancala_save/save.game");
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(getGameData());
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

    public void splitLastPoints(){
        int point = plateau.sumBoard()/2;
        scores[0] += point;
        scores[1] += point;

        if(scores[0] == scores[1]){
            roundWinner = null;
        }else{
            int winner = scores[0]>scores[1] ? 0 : 1;
            roundWinner = joueurs[winner];
            addVictory(winner, scores[winner]);
        }
    }

    public void undoPlay(){
        if (moves != null && moves.size() > 0){
            moves.remove(moves.size()-1);

            plateau.setBoardWithMoves(moves, firstPlayer);
            playerTurn = (playerTurn+1)%2;
        }
    }

    public void loadLastSave(){

        File[] files;
        File dir = new File("mancala_save/");

        files = dir.listFiles();

        File to_load = null;
        for (File file : files) {

            String fileName = file.toString();
            String extension = "";

            int index = fileName.lastIndexOf('.');
            if(index > 0) {
                extension= fileName.substring(index + 1);
            }

            if (extension.equals("game")){
                to_load = file;
            }
        }

        if(to_load != null){
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                fis = new FileInputStream(to_load);
                ois = new ObjectInputStream(fis);
                GameData data = (GameData) ois.readObject();

                load(data);

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
}
