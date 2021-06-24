package es.com.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MoveFromPlayer {

    private int fieldNumber;

    public MoveFromPlayer() {
    }

    public MoveFromPlayer(int fieldNumber) {
        this.fieldNumber = fieldNumber;
    }
}
