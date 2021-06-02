package ensi.model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Session {

    //id joueur et socket
    private HashMap<String, Socket> joueurs = new HashMap<>();

    public Session(){

    }

    public boolean hasPlayer(Joueur j){
        return joueurs.containsKey(j.id);
    }

    public void addPlayer(Joueur j, Socket s){
        joueurs.put(j.id, s);
    }

    public void replacePlayerSocket(Joueur j, Socket s){
        if (hasPlayer(j)){
            joueurs.put(j.id, s);
        }
    }

    public boolean isFull(){
        return joueurs.size() >= 2;
    }

    public void userDisconnect(Joueur joueur){
        if (hasPlayer(joueur)){
            joueurs.put(joueur.id, null);
        }
    }

    public String toString(){
        String display = "";
        
        return display;
    }

}
