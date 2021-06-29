package es.com.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GameResult {

    private String winner;


    public GameResult() {
    }

    public GameResult(String winner) {
        this.winner = winner;
    }
}
