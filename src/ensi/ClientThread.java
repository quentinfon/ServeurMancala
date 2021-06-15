package ensi;

import ensi.model.*;

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

            ois = new ObjectInputStream(s.getInputStream());
            oos = new ObjectOutputStream(s.getOutputStream());


            //Recupération du joueur
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
                Object data = ois.readObject();

                if(data instanceof Commande){
                    _session.request((Commande) data, _joueur);
                } else if(data instanceof InstructionModel){
                    _session.answer_request(((InstructionModel) data).instruction, _joueur);
                }else{
                    System.out.println("Unknown data received");
                }
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
