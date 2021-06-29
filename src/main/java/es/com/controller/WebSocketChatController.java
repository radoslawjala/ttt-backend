package es.com.controller;

import es.com.dto.*;
import es.com.model.GameResult;
import es.com.service.Match;
import es.com.util.ActiveUserChangeListener;
import es.com.util.ActiveUserManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Set;

@Controller
@Log4j2
public class WebSocketChatController implements ActiveUserChangeListener {


    @Autowired
    private SimpMessagingTemplate webSocket;

    @Autowired
    private ActiveUserManager activeUserManager;

    @PostConstruct
    private void init() {
        activeUserManager.registerListener(this);
    }

    @PreDestroy
    private void destroy() {
        activeUserManager.removeListener(this);
    }

    private Match match;

    @MessageMapping("/joinRequest")
    public void joinRequest(SimpMessageHeaderAccessor sha, @Payload JoinRequest joinRequest) {
        if(joinRequest != null) {
            if(sha.getUser().getName().equals(joinRequest.getInvitingUser())) {
                webSocket.convertAndSendToUser(joinRequest.getInvitedUser(), "/queue/joinRequest", joinRequest);
            }
            log.info("Join request: " + joinRequest);
        } else {
            log.error("Recipient name is null");
        }
    }

    @MessageMapping("/joinResponse")
    public void joinResponse(SimpMessageHeaderAccessor sha, @Payload JoinResponse joinResponse) {
        if(joinResponse != null) {
            if(sha.getUser().getName().equals(joinResponse.getInvitedUser())) {
                if(joinResponse.getDecision().equals("yes")) {
                    createMatch(joinResponse.getInvitingUser(), joinResponse.getInvitedUser());
                    webSocket.convertAndSendToUser(joinResponse.getInvitingUser(),
                            "/queue/reset", new Reset(false, joinResponse.getInvitedUser()));
                    webSocket.convertAndSendToUser(joinResponse.getInvitedUser(),
                            "/queue/reset", new Reset(true, joinResponse.getInvitingUser()));
                }
                webSocket.convertAndSendToUser(joinResponse.getInvitingUser(), "/queue/joinResponse", joinResponse);
            }
            log.info("Join response: " + joinResponse);
        } else {
            log.error("Receipient name is null");
        }
    }

    private void createMatch(String invitingUser, String invitedUser) {
        match = new Match(invitingUser, invitedUser);
    }

    @MessageMapping("/sendMove")
    public void receiveMoveFromPlayer(SimpMessageHeaderAccessor sha, @Payload MoveFromPlayer moveFromPlayer) {
        match.setGameField(moveFromPlayer);
        sendMoveToUsers(sha.getUser().getName(), moveFromPlayer);
        if(match.checkIfPlayerWon(sha.getUser().getName())) {
            log.info("User " + sha.getUser().getName() + " won!");
            webSocket.convertAndSendToUser(sha.getUser().getName(), "/queue/gameResult", new GameResult(sha.getUser().getName()));
            webSocket.convertAndSendToUser(moveFromPlayer.getOpponentName(), "/queue/gameResult", new GameResult(sha.getUser().getName()));
        }
    }

    private void sendMoveToUsers(String senderName, MoveFromPlayer move) {
        String currentSign = match.getSenderSign(senderName);
        PreparedMove moveToSender = new PreparedMove(move.getFieldNumber(), currentSign, true);
        PreparedMove moveToOpponent = new PreparedMove(move.getFieldNumber(), currentSign, false);
        webSocket.convertAndSendToUser(senderName, "/queue/moveReceived", moveToSender);
        webSocket.convertAndSendToUser(move.getOpponentName(), "/queue/moveReceived", moveToOpponent);
    }
    
    @Override
    public void notifyActiveUserChange() {
        Set<String> activeUsers = activeUserManager.getAll();
        webSocket.convertAndSend("/topic/active", activeUsers);
    }
}
