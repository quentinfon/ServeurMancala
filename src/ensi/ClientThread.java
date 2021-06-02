package ensi;

import ensi.model.Commande;
import ensi.model.Joueur;

import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable {

    private Thread _threadClient;
    private Socket _socket; //Socket du client
    private PrintWriter _out;

    private InputStream _in;
    private ObjectInputStream ois;

    private Serveur _serveur;
    private int _numClient=0;
    private Joueur _joueur;


    ClientThread(Socket s, Serveur serveur)
    {
        _serveur = serveur;
        _socket = s;
        try
        {

            _out = new PrintWriter(_socket.getOutputStream());
            _in = _socket.getInputStream();

            //Recupération du joueur
            ois = new ObjectInputStream(_in);
            _joueur = (Joueur) ois.readObject();
            System.out.println("Infos du joueur : "+_joueur);

            _numClient = serveur.addClient(s, _joueur);
        }
        catch (IOException e){ } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        _threadClient = new Thread(this);
        _threadClient.start();
    }


    public void run()
    {

        System.out.println("Un nouveau client s'est connecte, numéro client : "+_numClient);

        try
        {

            while(true)
            {
                Commande commande = (Commande) ois.readObject();

                System.out.println(commande.numToPlay);
                //TODO Check si c'est le tour du client
                //TODO utiliser le num de la case comme coup du joueur

            }

        } catch (IOException | ClassNotFoundException e) {
        } finally
        {
            try
            {
                System.out.println("Le client "+_numClient+" s'est deconnecte");
                _serveur.delClient(_numClient, _joueur);
                _socket.close();
            }
            catch (IOException e){ }
        }
    }

}
