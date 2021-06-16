package es.com.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {

    private String text;
    private boolean userVerifiedSuccessfully;

    public MessageResponse() {
    }

    public MessageResponse(String text, boolean userVerifiedSuccessfully) {
        this.text = text;
        this.userVerifiedSuccessfully = userVerifiedSuccessfully;
    }
}
