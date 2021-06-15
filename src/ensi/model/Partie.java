package ensi.model;


import java.io.*;
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

    public void load(GameData data){
        scores = data.scores;
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
