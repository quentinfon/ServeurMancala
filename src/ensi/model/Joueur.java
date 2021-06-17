package ensi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;


public class Joueur implements Serializable
{
    public String id;
    public String pseudo;
    public String ip;
    public String port;
    public ArrayList<String> action;

    public boolean connected;

    public Joueur(String id)
    {
        this.id = id;
        this.action= new ArrayList<>();
        this.connected = true;
    }

    public Joueur(String id, String pseudo, String ip, String port)
    {
        this.id = id;
        this.pseudo = pseudo;
        this.ip=ip;
        this.port=port;
        this.action= new ArrayList<>();
        this.connected = true;
    }

    public String toString()
    {
        return this.id + " : " + this.pseudo + " - " + (connected ? " en ligne" : "hors ligne");
    }
    public void setPseudo(String pseudo)
    {
        this.pseudo=pseudo;
    }
    public void setIp(String ip)
    {
        this.ip=ip;
    }
    public void setPort(String port)
    {
        this.port=port;
    }
    public String getPseudo()
    {
        return pseudo;
    }
    public String getIp( )
    {
        return ip;
    }
    public String getPort()
    {
        return port;
    }

    @Override
    public boolean equals(Object o){
        if(o == this) return true;
        if (!(o instanceof Joueur)) return false;

        Joueur j = (Joueur) o;
        return j.id.equals(this.id);

    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
}
