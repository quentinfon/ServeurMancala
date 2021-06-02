package ensi.model;

import java.io.Serializable;

public class Commande implements Serializable {


    public Action action;
    public int numToPlay;

    public Commande(Action action) throws MissingNumToPlayException {
        if (action == Action.PLAY) throw new MissingNumToPlayException();
        this.action = action;
    }

    public Commande(Action action, int numToPlay){
        this.action = action;
        this.numToPlay = numToPlay;
    }


}
