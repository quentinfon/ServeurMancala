package ensi.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by faye on 01/06/2017.
 */
public class Personne implements Serializable {
    public String nom;
    public String prenom;
    public String ip;
    public String port;
    public ArrayList<String> action;

    public Personne()
    {
        this.action= new ArrayList<>();
        this.action.add("tes1");
        this.action.add("test2");
    }

    public Personne(String n, String p, String ip, String port)
    {
        this.nom=n;
        this.prenom=p;
        this.ip=ip;
        this.port=port;
        this.action= new ArrayList<>();
        this.action.add("tes1");
        this.action.add("test2");
    }

    public void afficher()
    {
        System.out.println("   nom" + this.nom);
        System.out.println("   prenom " + this.prenom);
        System.out.println("   ip " + this.ip);
        System.out.println("   port " + this.port);
        for(int i=0;i<this.action.size();i++)
        {
            System.out.println("action "+ i+" ="+this.action.get(i));
        }
        System.out.println(" ");
    }
    public void setNom(String n)
    {
        this.nom=n;
    }
    public void setPrenom(String p)
    {
        this.prenom=p;
    }
    public void setIp(String ip)
    {
        this.ip=ip;
    }
    public void setPort(String port)
    {
        this.port=port;
    }
    public String setNom()
    {
        return nom;
    }
    public String getPrenom()
    {
        return prenom;
    }
    public String getIp( )
    {
        return ip;
    }
    public String getPort()
    {
        return port;
    }
}
