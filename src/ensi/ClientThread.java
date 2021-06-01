package ensi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {

    private Thread _threadClient;
    private Socket _socket; //Socket du client
    private PrintWriter _out;
    private BufferedReader _in;
    private Serveur _serveur;
    private int _numClient=0;


    ClientThread(Socket s, Serveur serveur) // le param s est donnée dans BlablaServ par ss.accept()
    {
        _serveur = serveur;
        _socket = s;
        try
        {
            // fabrication d'une variable permettant l'utilisation du flux de sortie avec des string
            _out = new PrintWriter(_socket.getOutputStream());
            // fabrication d'une variable permettant l'utilisation du flux d'entrée avec des string
            _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            // ajoute le flux de sortie dans la liste et récupération de son numéro
            _numClient = serveur.addClient(_out);
        }
        catch (IOException e){ }

        _threadClient = new Thread(this); // instanciation du thread
        _threadClient.start(); // démarrage du thread
    }


    public void run()
    {

        System.out.println("Un nouveau client s'est connecte, no "+_numClient);

        try
        {
            char charCur[] = new char[1];

            while(_in.read(charCur, 0, 1)!=-1) // attente en boucle des messages provenant du client (bloquant sur _in.read())
            {
                //Si l'info recu != retour à la ligne
                if (charCur[0] != '\u0000' && charCur[0] != '\n' && charCur[0] != '\r'){
                    //TODO Check si c'est le tour du client
                    //TODO utiliser le num de la case comme coup du joueur
                }

            }
        }
        catch (Exception e){ }
        finally
        {
            try
            {
                System.out.println("Le client no "+_numClient+" s'est deconnecte");
                _serveur.delClient(_numClient);
                _socket.close();
            }
            catch (IOException e){ }
        }
    }

}
