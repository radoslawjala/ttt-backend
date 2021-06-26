package es.com.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MoveFromPlayer {

    private int fieldNumber;
    private String opponentName;

    public MoveFromPlayer() {
    }

    public MoveFromPlayer(int fieldNumber, String opponentName) {
        this.fieldNumber = fieldNumber;
        this.opponentName = opponentName;
    }
}
