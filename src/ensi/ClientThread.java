package ensi;

import ensi.model.Joueur;

import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable {

    private Thread _threadClient;
    private Socket _socket; //Socket du client
    private PrintWriter _out;
    private InputStream _in;
    private Serveur _serveur;
    private int _numClient=0;
    private Joueur _joueur;


    ClientThread(Socket s, Serveur serveur) // le param s est donnée dans BlablaServ par ss.accept()
    {
        _serveur = serveur;
        _socket = s;
        try
        {
            // fabrication d'une variable permettant l'utilisation du flux de sortie avec des string
            _out = new PrintWriter(_socket.getOutputStream());
            // fabrication d'une variable permettant l'utilisation du flux d'entrée avec des string
            _in = _socket.getInputStream();

            //Recupération du joueur
            ObjectInputStream ois=new ObjectInputStream(_in);
            _joueur = (Joueur) ois.readObject();
            System.out.println("Infos du joueur : "+_joueur);

            _numClient = serveur.addClient(s, _joueur);
        }
        catch (IOException e){ } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        _threadClient = new Thread(this); // instanciation du thread
        _threadClient.start(); // démarrage du thread
    }


    public void run()
    {

        System.out.println("Un nouveau client s'est connecte, no "+_numClient);

        try
        {
            BufferedReader input = new BufferedReader(new InputStreamReader(_in));
            String userInput = "";
            while((userInput = input.readLine()) != null) // attente en boucle des messages provenant du client (bloquant sur _in.read())
            {
                System.out.println(userInput);
                //TODO Check si c'est le tour du client
                //TODO utiliser le num de la case comme coup du joueur

            }
        } catch (IOException e) {
            e.printStackTrace();
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
