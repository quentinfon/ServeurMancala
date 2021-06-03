package ensi.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Session {

    //id joueur et socket
    private HashMap<Joueur, ObjectOutputStream> joueurs = new HashMap<>();

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

            sendGameData();
        }
    }

    public boolean hasPlayer(Joueur j){
        return joueurs.containsKey(j);
    }

    public void addPlayer(Joueur j, ObjectOutputStream stream){
        joueurs.put(j, stream);
        sendGameData();
    }

    public void replacePlayerSocket(Joueur j, ObjectOutputStream stream){
        if (hasPlayer(j)){
            j.connected = true;
            joueurs.put(j, stream);

            sendGameData();
        }
    }

    public boolean isFull(){
        return joueurs.size() >= 2;
    }

    public void userDisconnect(Joueur joueur){
        if (hasPlayer(joueur)){
            joueur.connected = false;
            joueurs.put(joueur, null);
            sendGameData();
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
            startNewGame();
        }else if(commande.action == Action.PLAY){

        }
    }

    public void sendGameData(){
        if (partie == null) return;
        for(var entry : joueurs.entrySet()) {
            try {
                entry.getValue().writeObject(partie.getGameData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
