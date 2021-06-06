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

    public boolean isPlayable(int joueur, int cell){
        if(joueur != 0 && joueur != 1) return false;
        if(cell < 0 || cell > 5) return false;

        return cases[joueur][cell] > 0;
    }

    public void playCell(int joueur, int cell){

        int graines = cases[joueur][cell];
        cases[joueur][cell] = 0;

        for (int i = cell+1; graines > 0; i++){

            int j = (joueur + i/6)%2;
            int c = j == joueur ? i%6 : 5 - i%6;

            if (j != joueur || c != cell){
                cases[j][c]++;
                graines--;
            }
        }

    }


    public int[][] getCases(){
        return cases;
    }

}
