package es.com.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GameField {

    private String sign;
    private boolean alreadyselected;

    public GameField() {
    }

    public GameField(String sign, boolean alreadyselected) {
        this.sign = sign;
        this.alreadyselected = alreadyselected;
    }
}
