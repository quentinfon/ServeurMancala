package ensi.model;

public class Partie {

    private Plateau plateau;

    private Joueur playerTurn;
    private Joueur joueur1;
    private Joueur joueur2;

    public Partie(Joueur j1, Joueur j2){
        playerTurn = j1;
        this.joueur1 = j1;
        this.joueur2 = j2;

        plateau = new Plateau(j1, j2);
    }


    public boolean checkEnd(){
        return ( playerTurn.equals(joueur1) && plateau.sumCaseJ1() == 0 ) || ( playerTurn.equals(joueur2) && plateau.sumCaseJ2() == 0 );
    }




}
