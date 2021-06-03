package ensi;

import ensi.model.Commande;
import ensi.model.Joueur;
import ensi.model.Session;

import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable {

    private Thread _threadClient;
    private Socket _socket;
    private PrintWriter _out;

    private InputStream _in;
    private ObjectInputStream ois;

    private Serveur _serveur;
    private Session _session;

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

            _session = serveur.addClient(s, _joueur);
        }
        catch (IOException e){ e.printStackTrace(); } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        _threadClient = new Thread(this);
        _threadClient.start();
    }


    public void run()
    {

        try
        {

            while(true)
            {
                Commande commande = (Commande) ois.readObject();

                _session.request(commande, _joueur);

            }

        } catch (IOException | ClassNotFoundException e) {
        } finally
        {
            try
            {
                System.out.println(_joueur.pseudo+" s'est deconnecte");
                _serveur.delClient(_joueur);
                _socket.close();
            }
            catch (IOException e){ }
        }
    }

}
