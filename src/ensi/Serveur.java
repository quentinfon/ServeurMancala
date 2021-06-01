package ensi;
import ensi.model.Joueur;

import java.io.*;

import java.net.ServerSocket;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;


public class Serveur {

    private Vector _clients = new Vector();
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


    synchronized public void delClient(int i)
    {
        _nbClients--;
        if (_clients.elementAt(i) != null)
        {
            _clients.removeElementAt(i);
        }
    }

    synchronized public int addClient(PrintWriter out)
    {
        _nbClients++;
        _clients.addElement(out);

        //Num du client
        return _clients.size()-1;
    }

    //** Methode : retourne le nombre de clients connectés **
    synchronized public int getNbClients()
    {
        return _nbClients; // retourne le nombre de clients connectés
    }

}
