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

        while (graines > 0){
            if (j == 0){
                c--;
                if (c < 0){
                    c = 0;
                    j = 1;
                }
            }else if (j == 1){
                c++;
                if (c > 5){
                    c = 5;
                    j = 0;
                }
            }

            if (j != joueur || c != cell){
                cases[j][c]++;
                graines--;
            }

        }


        //TODO Utilisation de la derniere case pour verifier s'il y a des graines à manger et ajouter au score du joueur
        // Manger les graines
        if(joueur != j){

            int i = c;

            while(true){

                if(cases[j][i]==2 | cases[j][i]==3){
                    int score = partie.getScore(joueur) + cases[j][i];
                    cases[j][i] =0;
                    partie.setScore(joueur, score);
                }else{
                    break;
                }


                if (j == 0){
                    i++;
                    if (i > 5){
                        break;
                    }
                }else if (j == 1){
                    i--;
                    if (i < 0){
                        break;
                    }
                }

            }

        }


    }


    public int[][] getCases(){
        return cases;
    }

}
