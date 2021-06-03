package ensi.model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Session {

    //id joueur et socket
    private HashMap<Joueur, Socket> joueurs = new HashMap<>();

    private Partie partie;

    public Session(){

    }

    private void startNewGame(){
        if(joueurs.size() >= 2){
            ArrayList<Joueur> j = new ArrayList<>();
            for(var entry : joueurs.entrySet()) {
                j.add(entry.getKey());
            }
            partie = new Partie(j.get(0), j.get(1));
        }
    }

    public boolean hasPlayer(Joueur j){
        return joueurs.containsKey(j);
    }

    public void addPlayer(Joueur j, Socket s){
        joueurs.put(j, s);
    }

    public void replacePlayerSocket(Joueur j, Socket s){
        if (hasPlayer(j)){
            joueurs.put(j, s);
        }
    }

    public boolean isFull(){
        return joueurs.size() >= 2;
    }

    public void userDisconnect(Joueur joueur){
        if (hasPlayer(joueur)){
            joueurs.put(joueur, null);
        }
    }

    public String toString(){
        StringBuilder display = new StringBuilder("Joueurs dans la session : ");
        for(var entry : joueurs.entrySet()) {
            String uid = entry.getKey().id;
            display.append("\n- ").append(uid).append(entry.getValue() != null ? " : en ligne" : " : hors ligne");
        }
        return display.toString();
    }

    public void request(Commande commande, Joueur joueur){
        if(commande.action == Action.NEW_GAME){
            partie = partie;
        }else if(commande.action == Action.PLAY){

        }
    }

}
