package es.com.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Reset {

    private boolean disabled;
    private String opponentName;

    public Reset() {
    }

    public Reset(boolean disabled, String opponentName) {
        this.disabled = disabled;
        this.opponentName = opponentName;
    }
}
