package es.com.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Reset {

    private boolean disabled;

    public Reset() {
    }

    public Reset(boolean disabled) {
        this.disabled = disabled;
    }
}
