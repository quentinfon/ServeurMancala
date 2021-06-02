package ensi.model;

public class MissingNumToPlayException extends Exception{

    public MissingNumToPlayException() {
        super("Missing num to the Command object for an action of type Play");
    }

}
