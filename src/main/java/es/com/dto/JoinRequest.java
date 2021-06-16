package es.com.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JoinRequest {

    private String invitingUser;
    private String invitedUser;

    public JoinRequest() {
    }

    public JoinRequest(String invitingUser, String invitedUser) {
        this.invitingUser = invitingUser;
        this.invitedUser = invitedUser;
    }
}
