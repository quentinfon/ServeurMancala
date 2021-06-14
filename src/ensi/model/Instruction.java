package ensi.model;

import java.io.Serializable;

public enum Instruction implements Serializable {

    PLAY_SOUND,
    END_OF_GAME,
    NEW_GAME,
    SURRENDER,
    LOAD_GAME,
    OPPONENT_DISCONNECT,
    YES,
    NO

}
