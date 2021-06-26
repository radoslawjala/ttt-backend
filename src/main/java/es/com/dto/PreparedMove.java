package es.com.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PreparedMove {

    private int fieldNumber;
    private  String text;
    private boolean boardDisabled;

    public PreparedMove() {
    }

    public PreparedMove(int fieldNumber, String text, boolean boardDisabled) {
        this.fieldNumber = fieldNumber;
        this.text = text;
        this.boardDisabled = boardDisabled;
    }
}
