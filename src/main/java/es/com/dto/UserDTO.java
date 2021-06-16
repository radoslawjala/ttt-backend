package es.com.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {

    private String username;

    public UserDTO() {
    }

    public UserDTO(String username) {
        this.username = username;
    }
}
