package ensi.model;

public class Plateau {

    private int[][] cases = new int[2][6];
    private Partie partie;

    private void initPlateau(){
        for (int i = 0; i < 6; i++){
            cases[0][i] = 4;
            cases[1][i] = 4;
        }
    }

    public Plateau(Partie p){

        initPlateau();
        this.partie = p;

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

        //Case et joueur actuel
        int j = joueur;
        int c = cell;

        for (int i = cell+1; graines > 0; i++){

            j = (joueur + i/6)%2;
            c = j == joueur ? i%6 : 5 - i%6;

            if (j != joueur || c != cell){
                cases[j][c]++;
                graines--;
            }
        }

        //TODO Utilisation de la derniere case pour verifier s'il y a des graines à manger et ajouter au score du joueur
        // Manger les graines
        if(joueur != j){
            for(int i = c; i<6; i++){
                if(cases[j][i]==2 | cases[j][i]==3){
                    int score = partie.getScore(joueur) + cases[j][i];
                    cases[j][i] =0;
                    partie.setScore(joueur, score);
                }else{
                    break;
                }
            }
        }


    }


    public int[][] getCases(){
        return cases;
    }

}
