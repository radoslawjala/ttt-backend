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

    public PreparedMove() {
    }

    public PreparedMove(int fieldNumber, String text) {
        this.fieldNumber = fieldNumber;
        this.text = text;
    }
}
