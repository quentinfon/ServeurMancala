package ensi;

import ensi.model.Commande;
import ensi.model.Joueur;
import ensi.model.Session;

import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable {

    private Thread _threadClient;
    private Socket _socket;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private Serveur _serveur;
    private Session _session;

    private Joueur _joueur;


    ClientThread(Socket s, Serveur serveur)
    {
        _serveur = serveur;
        _socket = s;
        try
        {
            oos = new ObjectOutputStream(s.getOutputStream());

            //Recupération du joueur
            ois = new ObjectInputStream(_socket.getInputStream());

            _joueur = (Joueur) ois.readObject();

            System.out.println("Infos du joueur : "+_joueur);


            _session = serveur.addClient(oos, _joueur);
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
            e.printStackTrace();
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
