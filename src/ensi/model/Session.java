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

    private boolean waitingAnswer;
    private Joueur waitingPlayerResponse;
    private Instruction waitingInstruction;


    public Session(Serveur serveur){
        partie = new Partie();
        waitingAnswer = false;
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

            partie.init_partie();
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

        //Cancel request if requested player don't answer and play
        if (waitingAnswer){
            if(joueur.equals(waitingPlayerResponse))
                waitingAnswer = false;
            else
                return;
        }

        if(commande.action == Action.PLAY){

            if (partie != null){

                boolean havePlayed = partie.playTurn(joueur, commande.numToPlay);

                if(havePlayed) {
                    //Envoie du gagnant de la partie et du match
                    if (partie.checkEnd()) {
                        sendEndGameInformation();
                    }

                    partie.nextPlayer();

                    sendGameData();
                }
            }

        }else {

            Instruction demande = null;

            switch (commande.action){
                case NEW_GAME:
                    demande = Instruction.NEW_GAME;
                    break;
                case SAVE_GAME:
                    demande = Instruction.SAVE_GAME;
                    break;
                case LOAD_GAME:
                    demande = Instruction.LOAD_GAME;
                    break;
                case SPLIT_LAST_POINTS:
                    demande = Instruction.SPLIT_LAST_POINTS;
                    break;
                default:
                    break;
            }

            if(demande != null){
                //If two player are connected
                if (isFull()){
                    var opponent = getOpponent(joueur);
                    try {
                        opponent.getValue().reset();
                        opponent.getValue().writeObject(new InstructionModel(demande));

                        waitingAnswer = true;
                        waitingPlayerResponse = opponent.getKey();
                        waitingInstruction = demande;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    if (demande == Instruction.NEW_GAME){
                        startNewGame();
                    } else if (demande == Instruction.SAVE_GAME){
                        partie.saveGame();
                    }

                }
            }

        }
    }

    /**
     * Execute the waiting instruction
     */
    public void execute_instruction(){
        if (waitingInstruction == Instruction.NEW_GAME){
            startNewGame();
        }else if(waitingInstruction == Instruction.LOAD_GAME){
            partie.loadLastSave();
        } else if(waitingInstruction == Instruction.SAVE_GAME){
            partie.saveGame();
        } else if(waitingInstruction == Instruction.SPLIT_LAST_POINTS && partie.plateau.sumBoard()<10){
            partie.splitLastPoints();
            sendEndGameInformation();
        }
    }

    public void sendEndGameInformation() {
        if (partie.checkDefinitivEnd()) {
            partie.started = false;

            if (partie.roundWinner != null)
                sendInfoToClients(new InstructionModel(Instruction.END_OF_MATCH, partie.roundWinner.id));
        } else {
            partie.nextGame();

            if (partie.roundWinner != null){
                sendInfoToClients(new InstructionModel(Instruction.END_OF_GAME, partie.roundWinner.id));
            }else{
                //match null
                sendInfoToClients(new InstructionModel(Instruction.END_OF_GAME, ""));
            }

        }
    }

    /**
     * Client response to a question (new game ...)
     * @param reponse client response
     * @param joueur client
     */
    public void answer_request(Instruction reponse, Joueur joueur){
        if(waitingAnswer){

            if (joueur.equals(waitingPlayerResponse)) {
                if (reponse == Instruction.YES) {
                    execute_instruction();
                }
                waitingAnswer = false;
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


    public void sendInfoToClients(InstructionModel info){
        if (partie == null) return;
        for(var entry : joueurs.entrySet()) {
            try {
                entry.getValue().reset();
                entry.getValue().writeObject(info);
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
