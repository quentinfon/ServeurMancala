package ensi.model;

import java.util.ArrayList;

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

    public int sumBoard(){
        int count = 0;
        for (int j = 0; j < 2; j++){
            for (int i = 0; i < 6; i++){
                count += cases[j][i];
            }
        }
        return count;
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

        if(cases[joueur][cell] <= 0) return false;

        if(sumCaseJoueur((joueur+1)%2) == 0){
            boolean havePlayableCell = false;

            for (int i = 0; i < 6; i++){

                boolean landingOnOtherPlayer;

                if (joueur == 1){
                    landingOnOtherPlayer  = (i + cases[joueur][i]) >= 6;
                }else{
                    landingOnOtherPlayer  = (i - cases[joueur][i]) < 0;
                }

                if(landingOnOtherPlayer){
                    havePlayableCell = true;
                    break;
                }
            }

            if (havePlayableCell){
                if(joueur == 1){
                    return cell + cases[joueur][cell] >= 6;
                }else{
                    return cell - cases[joueur][cell] < 0;
                }
            }

        }

        return true;
    }


    /**
     * Get all the points of a player
     * @param joueur the player
     * @return the total of points
     */
    public int getAllPlayerGraines(int joueur){
        if (joueur != 0 && joueur != 1) return -1;

        int total = 0;
        for(int i = 0; i < 6; i++){
            total += cases[joueur][i];
            cases[joueur][i] = 0;
        }

        return total;
    }


    /**
     * Regle 7 : Verification de la possibilite de capturer
     * @param player le joueur
     * @param cell la case
     * @return capture possible ou non
     */
    public boolean canBeCapture(int player, int cell){
        int i = cell;
        int total= 0;

        while(true){

            if(cases[player][i]==2 || cases[player][i]==3){
                total += cases[player][i];
            }else{
                break;
            }

            if (player == 0){
                i++;
                if (i > 5){
                    break;
                }
            } else {
                i--;
                if (i < 0){
                    break;
                }
            }

        }

        return total < sumCaseJoueur(player);
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


        //Capture des graines
        //Regle 7 : : Si un coup devait prendre toutes les graines adverses, alors le coup peut être joué,
        //mais aucune capture n'est faite
        if(joueur != j && canBeCapture(j, c)){

            int i = c;


            while(true){

                if(cases[j][i]==2 || cases[j][i]==3){
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
                } else {
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

    public void setCases(int[][] cells){
        cases = cells;
    }


    public void setBoardWithMoves(ArrayList<Integer> moves, int firstPlayer){
        initPlateau();
        int player = firstPlayer;

        partie.setScore(0, 0);
        partie.setScore(1, 0);

        for(int cell : moves){
            playCell(player, cell);
            player = (player+1)%2;
        }
    }

}
