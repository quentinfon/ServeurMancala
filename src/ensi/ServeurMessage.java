package ensi;
import ensi.model.Joueur;

import java.io.*;

import java.net.ServerSocket;

import java.net.Socket;


public class ServeurMessage {

    public static void main(String[] zero)
    {
        ServerSocket socketserver;
        Socket socketduserveur;
        Joueur pers= new Joueur();

        try
        {
            socketserver = new ServerSocket(2009);
            System.out.println("Le ensi est à l'écoute du port "+socketserver.getLocalPort());
            socketduserveur = socketserver.accept();
            System.out.println("Un zéro s'est connecté");
            OutputStream os=socketduserveur.getOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(os);

            pers.setNom("Mon nom");
            pers.setPrenom("Mon prénom");
            pers.setIp("10.10.0.0");
            pers.setPort("2009");
            System.out.println("Données envoyées");
            pers.afficher();

            oos.writeObject(pers);// envoie de l'objet

            socketduserveur.close();
            socketserver.close();

        }
        catch (IOException e)
            {
                e.printStackTrace();
            }

    }

}
