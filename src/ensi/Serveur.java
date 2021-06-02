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

    //Liste des out stream de tous les clients
    private Vector _clients = new Vector();

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

            new GameThread(serveur);

            while (true){
                new ClientThread(socketserver.accept(), serveur);
            }
        }
        catch (IOException e)
            {
                e.printStackTrace();
            }

    }


    synchronized public void sendGame()
    {
        //TODO passer un objet de modelisation de la partie (Cases et nombre de pions dans chaque / score / tour du joueur)

        PrintWriter out;
        for (int i = 0; i < _clients.size(); i++)
        {
            out = (PrintWriter) _clients.elementAt(i);
            if (out != null)
            {
                //TODO envoie de l'objet
                out.print("Plateau du jeu");
                out.flush();
            }
        }
    }


    synchronized public void delClient(int i, Joueur joueur)
    {
        _nbClients--;
        if (_clients.elementAt(i) != null)
        {
            _clients.removeElementAt(i);
        }

        for(Session session : _sessions){
            if(session.hasPlayer(joueur)){
                session.userDisconnect(joueur);
            }
        }
    }

    /**
     * Fonction d'ajout d'un client
     * @param socket socket du client
     * @param joueur l'objet joueur du client
     * @return  le num du client
     */
    synchronized public int addClient(Socket socket, Joueur joueur)
    {
        _nbClients++;

        //Ajout du client dans le broadcast du serveur
        try {
            _clients.addElement(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Reconnexion d'un client dans sa session
        for(Session session : _sessions){
            if(session.hasPlayer(joueur)){
                session.replacePlayerSocket(joueur, socket);
                return _clients.size()-1;
            }
        }

        //Connexion à une session non pleine
        for (Session session : _sessions){
            if (!session.isFull()){
                session.addPlayer(joueur, socket);
            }
        }

        return _clients.size()-1;
    }


    synchronized public int getNbClients()
    {
        return _nbClients;
    }

}
