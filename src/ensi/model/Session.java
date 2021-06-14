package ensi.model;

import ensi.Serveur;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Session {

    //id joueur et socket
    private HashMap<Joueur, ObjectOutputStream> joueurs = new HashMap<>();

    private Partie partie;

    public Session(Serveur serveur){
        partie = new Partie();
    }

    /**
     * Start a new game
     */
    private void startNewGame(){
        if(joueurs.size() >= 2){
            ArrayList<Joueur> j = new ArrayList<>();
            for(var entry : joueurs.entrySet()) {
                j.add(entry.getKey());
            }

            partie.startGame(j.get(0), j.get(1));
            sendGameData();
        }
    }

    /**
     * Check if a player is in a session
     * @param j the player
     * @return true if player is in the session
     */
    public boolean hasPlayer(Joueur j){
        return joueurs.containsKey(j);
    }


    /**
     * Function to add a player in a session
     * @param j the player to add
     * @param stream the player object output stream
     */
    public void addPlayer(Joueur j, ObjectOutputStream stream){
        j.connected = true;
        joueurs.put(j, stream);
        if (joueurs.size() == 2){
            startNewGame();
        }
        sendGameData();
    }


    /**
     * Replace the player object output stream
     * @param j the player
     * @param stream the stream
     */
    public void replacePlayerSocket(Joueur j, ObjectOutputStream stream){
        if (hasPlayer(j)){
            j.connected = true;
            joueurs.put(j, stream);

            sendGameData();
        }
    }

    /**
     * Check if a session is full
     * @return true if full
     */
    public boolean isFull(){
        return joueurs.size() >= 2;
    }

    /**
     * Return the opponent
     * @param j the player
     * @return opponent
     */
    public Map.Entry<Joueur, ObjectOutputStream> getOpponent(Joueur j){
        if(joueurs.size() >= 2){
            for(var entry : joueurs.entrySet()) {
                if(!entry.getKey().equals(j)){
                    return entry;
                }
            }
        }
        return null;
    }

    /**
     * Check if a session have to be shut down
     * @return if the seesion have to be delete
     */
    public boolean checkSessionEnd(){
        boolean toDelete = true;

        for(var entry : joueurs.entrySet()) {
            if(entry.getValue() != null){
                toDelete = false;
                break;
            }
        }

        return toDelete;
    }

    /**
     * Disconnect a player from a session
     * @param joueur the player to disconnect
     */
    public void userDisconnect(Joueur joueur){
        if (hasPlayer(joueur)){
            joueur.connected = false;
            joueurs.put(joueur, null);

            checkSessionEnd();
            sendGameData();
        }
    }

    /**
     * Handle Client request
     * @param commande the commande
     * @param joueur the player who request the commande
     */
    public void request(Commande commande, Joueur joueur){
        if(commande.action == Action.NEW_GAME){
            if(isFull()){

                var opponent = getOpponent(joueur);

                try {
                    opponent.getValue().reset();
                    opponent.getValue().writeObject(new InstructionModel(Instruction.NEW_GAME));

                    //TODO Thread de commande type new game pour une session


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            startNewGame();

        }else if(commande.action == Action.PLAY){

            if (partie != null){
                partie.playTurn(joueur, commande.numToPlay);
                sendGameData();
            }

        }
    }


    /**
     * Set session player if game is not started
     * @return the GameData
     */
    public GameData getData(){
        GameData data = partie.getGameData();

        if (!partie.started){
            ArrayList<Joueur> j = new ArrayList<>();
            for(var player : joueurs.entrySet()) {
                j.add(player.getKey());
            }
            if (j.size() >= 1)
                data.joueurs[0] = j.get(0);
            if(j.size() >= 2)
                data.joueurs[1] = j.get(1);
        }
        return data;
    }

    /**
     * Send the game data to all the client of the session
     */
    public void sendGameData(){
        if (partie == null) return;
        for(var entry : joueurs.entrySet()) {
            try {
                entry.getValue().reset();
                entry.getValue().writeObject(getData());
            } catch (NullPointerException | IOException e) {

            }
        }
    }


    /**
     * Display of a session
     * @return the string to display
     */
    public String toString(){
        StringBuilder display = new StringBuilder("Joueurs dans la session : ");
        for(var entry : joueurs.entrySet()) {
            String uid = entry.getKey().id;
            display.append("\n- ").append(uid).append(entry.getValue() != null ? " : en ligne" : " : hors ligne");
        }
        return display.toString();
    }

}
