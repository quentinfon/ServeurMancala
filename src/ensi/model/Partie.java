package ensi.model;

public class Partie {

    private Plateau plateau;

    private Joueur playerTurn;

    private Joueur[] joueurs = new Joueur[2];

    public Partie(Joueur j1, Joueur j2){
        playerTurn = j1;
        joueurs[0] = j1;
        joueurs[1] = j2;

        plateau = new Plateau();
    }


    public boolean checkEnd(){
        return ( playerTurn.equals(joueurs[0]) && plateau.sumCaseJoueur(0) == 0 ) || ( playerTurn.equals(joueurs[1]) && plateau.sumCaseJoueur(1) == 0 );
    }




}
