package es.com.service;

import es.com.dto.MoveFromPlayer;
import es.com.model.GameField;

public class Match {

    private String player1;
    private String player2;

    private GameField[] gameBoard;

    private String player1Sign = "X";
    private String player2Sign = "O";

    public Match(String invitingUser, String invitedUser) {
        this.player1 = invitingUser;
        this.player2 = invitedUser;
        initGameBoard();
    }

    private void initGameBoard() {
        gameBoard = new GameField[9];
        for(int i = 0; i < 9; i++) {
            gameBoard[0] = new GameField("", false);
        }
    }

    public void setGameField(MoveFromPlayer moveFromPlayer) {
        if(moveFromPlayer.getOpponentName().equals(player1)) {
            GameField gameField = new GameField(player1Sign, true);
            gameBoard[moveFromPlayer.getFieldNumber()] = gameField;
        } else {
            GameField gameField = new GameField(player2Sign, true);
            gameBoard[moveFromPlayer.getFieldNumber()] = gameField;
        }
//        gameBoard[moveFromPlayer.getFieldNumber()].setAlreadyselected(true);
    }

    public String getSenderSign(String senderName) {
        if (senderName.equals(player1)) {
            return player1Sign;
        } else {
            return player2Sign;
        }
    }
}
