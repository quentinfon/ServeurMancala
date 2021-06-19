package ensi.model;

import java.io.Serializable;

public enum Action implements Serializable {

    PLAY,
    NEW_GAME,
    SAVE_GAME,
    LOAD_GAME,
    SURRENDER,
    SPLIT_LAST_POINTS,
    UNDO_MOVE

}
