package es.com.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JoinResponse {

    private String invitingUser;
    private String invitedUser;
    private boolean decision;

    public JoinResponse() {
    }

    public JoinResponse(String invitingUser, String invitedUser, boolean decision) {
        this.invitingUser = invitingUser;
        this.invitedUser = invitedUser;
        this.decision = decision;
    }
}
