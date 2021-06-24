package es.com.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Reset {

    private boolean enabled;
    private String opponentName;

    public Reset() {
    }

    public Reset(boolean enabled, String opponentName) {
        this.enabled = enabled;
        this.opponentName = opponentName;
    }
}
