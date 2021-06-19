package ensi.model;

import java.io.Serializable;

public enum Instruction implements Serializable {

    PLAY_SOUND,
    END_OF_GAME,
    END_OF_MATCH,
    NEW_GAME,
    SAVE_GAME,
    SPLIT_LAST_POINTS,
    LOAD_GAME,
    OPPONENT_DISCONNECT,
    YES,
    NO,
    UNDO_MOVE

}
