package ensi.model;

public class Plateau {


    private int[] caseJoueur1 = new int[6];
    private int[] caseJoueur2 = new int[6];


    private void initPlateau(){
        for (int i = 0; i < 6; i++){
            caseJoueur1[i] = 4;
            caseJoueur2[i] = 4;
        }
    }

    public Plateau(Joueur j1, Joueur j2){
        initPlateau();
    }

    public int sumCaseJ1(){
        int count = 0;
        for (int i = 0; i < 6; i++){
            count += caseJoueur1[i];
        }
        return count;
    }

    public int sumCaseJ2(){
        int count = 0;
        for (int i = 0; i < 6; i++){
            count += caseJoueur2[i];
        }
        return count;
    }

}
