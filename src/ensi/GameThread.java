package ensi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class GameThread implements Runnable {

    private Serveur _serveur;
    BufferedReader _in;
    String _strCommande="";
    private Thread _t;

    GameThread(Serveur serveur)
    {
        _serveur = serveur;

        _in = new BufferedReader(new InputStreamReader(System.in));

        _t = new Thread(this);
        _t.start();
    }

    public void run()
    {

        System.out.println("Démarage thread de jeu :D");

        try
        {
            while ((_strCommande=_in.readLine())!=null)
            {
                if (_strCommande.equalsIgnoreCase("quit")) // commande "quit" detectée ...
                    System.exit(0); // ... on ferme alors le serveur
                else if(_strCommande.equalsIgnoreCase("total")) // commande "total" detectée ...
                {
                    // ... on affiche le nombre de clients actuellement connectés
                    System.out.println("Nombre de connectes : "+_serveur.getNbClients());
                    System.out.println("--------");
                }
                System.out.flush();
            }
        }
        catch (IOException e) {}

    }
}
