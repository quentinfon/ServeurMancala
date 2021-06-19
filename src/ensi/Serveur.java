package ensi;
import ensi.model.Joueur;
import ensi.model.Session;

import java.io.*;

import java.lang.reflect.Array;
import java.net.ServerSocket;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;


public class Serveur {

    //Liste des sessions de jeu
    private ArrayList<Session> _sessions = new ArrayList<>();

    private int _nbClients = 0;

    public static void main(String[] zero)
    {

        try
        {
            Serveur serveur = new Serveur();

            //TODO Add a game Thread

            ServerSocket socketserver = new ServerSocket(2009);
            System.out.println("Serveur mancala sur le port : "+socketserver.getLocalPort());


            while (true){
                new ClientThread(socketserver.accept(), serveur);
                serveur.displayServerStatus();
            }
        }
        catch (IOException e)
            {
                e.printStackTrace();
            }

    }

    /**
     * Remove a player
     * @param joueur the player to remove
     */
    synchronized public void delClient(Joueur joueur)
    {
        _nbClients--;
        ArrayList<Session> toShutDown = new ArrayList<>();

        for(Session session : _sessions){
            if(session.hasPlayer(joueur)){
                session.userDisconnect(joueur);

                if (session.checkSessionEnd()){
                    toShutDown.add(session);
                }
            }
        }

        for (Session s : toShutDown){
            _sessions.remove(s);
        }

    }

    /**
     * Fonction d'ajout d'un client
     * @param stream output stream du client
     * @param joueur l'objet joueur du client
     * @return  le num du client
     */
    synchronized public Session addClient(ObjectOutputStream stream, Joueur joueur)
    {
        _nbClients++;

        //Reconnexion d'un client dans sa session
        for(Session session : _sessions){
            if(session.hasPlayer(joueur)){
                session.replacePlayerSocket(joueur, stream);
                return session;
            }
        }

        //Connexion à une session non pleine
        for (Session session : _sessions){
            if (!session.isFull()){
                session.addPlayer(joueur, stream);
                return session;
            }
        }

        //Création d'une nouvelle session si aucune session libre
        Session session = new Session(this);
        session.addPlayer(joueur, stream);
        _sessions.add(session);

        return session;
    }

    /**
     * Remove a session of the server
     * @param session the session to remove
     */
    synchronized public void removeSession(Session session){
        _sessions.remove(session);
    }

    /**
     * Return the number of client on the server
     * @return number of client
     */
    synchronized public int getNbClients()
    {
        return _nbClients;
    }

    /**
     * Display the informations of the server
     */
    synchronized public void displayServerStatus(){
        System.out.println("\n\n\n#############################################");
        System.out.println(_nbClients + " connéctés");
        System.out.println(_sessions.size() + " sessions : ");
        for(Session s : _sessions){
            System.out.println(s);
        }
        System.out.println("#############################################");
    }

}
