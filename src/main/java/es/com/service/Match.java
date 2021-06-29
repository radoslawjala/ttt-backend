package es.com.service;

import es.com.dto.MoveFromPlayer;
import es.com.model.GameField;
import lombok.ToString;

import java.util.Arrays;

@ToString
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
        for (int i = 0; i < 9; i++) {
            gameBoard[i] = new GameField("", false);
        }
    }

    public void setGameField(MoveFromPlayer moveFromPlayer) {
        if (moveFromPlayer.getOpponentName().equals(player1)) {
            GameField gameField = new GameField(player2Sign, true);
            gameBoard[moveFromPlayer.getFieldNumber()] = gameField;
        } else {
            GameField gameField = new GameField(player1Sign, true);
            gameBoard[moveFromPlayer.getFieldNumber()] = gameField;
        }
    }

    public String getSenderSign(String senderName) {
        if (senderName.equals(player1)) {
            return player1Sign;
        } else {
            return player2Sign;
        }
    }

    public boolean checkIfPlayerWon(String name) {
        String sign = getSenderSign(name);
        return checkAllPossibilities(sign);
    }

    private boolean checkAllPossibilities(String sign) {
        return (checkHorizontal(sign) || checkVertical(sign) || checkDiagonal(sign));
    }

    private boolean checkHorizontal(String sign) {
        return checkFirstRow(sign) || checkSecondRow(sign) || checkThirdRow(sign);
    }

    private boolean checkVertical(String sign) {
        return checkFirstColumn(sign) || checkSecondColumn(sign) || checkThirdColumn(sign);
    }

    private boolean checkDiagonal(String sign) {
        return checkFirstDiagonal(sign) || checkSecondDiagonal(sign);
    }

    private boolean checkFirstRow(String sign) {
        return checkGameField(sign, 0)
                && checkGameField(sign, 1)
                && checkGameField(sign, 2);
    }

    private boolean checkSecondRow(String sign) {
        return checkGameField(sign, 3)
                && checkGameField(sign, 4)
                && checkGameField(sign, 5);
    }

    private boolean checkThirdRow(String sign) {
        return checkGameField(sign, 6)
                && checkGameField(sign, 7)
                && checkGameField(sign, 8);
    }

    private boolean checkFirstColumn(String sign) {
        return checkGameField(sign, 0)
                && checkGameField(sign, 3)
                && checkGameField(sign, 6);
    }

    private boolean checkSecondColumn(String sign) {
        return checkGameField(sign, 1)
                && checkGameField(sign, 4)
                && checkGameField(sign, 7);
    }

    private boolean checkThirdColumn(String sign) {
        return checkGameField(sign, 2)
                && checkGameField(sign, 5)
                && checkGameField(sign, 8);
    }

    private boolean checkFirstDiagonal(String sign) {
        return checkGameField(sign, 0)
                && checkGameField(sign, 4)
                && checkGameField(sign, 8);
    }

    private boolean checkSecondDiagonal(String sign) {
        return checkGameField(sign, 2)
                && checkGameField(sign, 4)
                && checkGameField(sign, 6);

    }

    private boolean checkGameField(String sign, int fieldNumber) {
        return gameBoard[fieldNumber].isAlreadyselected()
                && gameBoard[fieldNumber].getSign().equals(sign);
    }
}
