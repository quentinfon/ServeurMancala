package ensi.model;

public class Plateau {

    private int[][] cases = new int[2][6];

    private void initPlateau(){
        for (int i = 0; i < 6; i++){
            cases[0][i] = 4;
            cases[1][i] = 4;
        }
    }

    public Plateau(){
        initPlateau();
    }

    public int sumCaseJoueur(int numJoueur){
        int count = 0;
        for (int i = 0; i < 6; i++){
            count += cases[numJoueur][i];
        }
        return count;
    }

}
