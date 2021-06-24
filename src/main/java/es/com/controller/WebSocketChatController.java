package es.com.controller;

import es.com.dto.*;
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
                    webSocket.convertAndSendToUser(joinResponse.getInvitingUser(), "/queue/reset", new Reset(true, joinResponse.getInvitedUser()));
                    webSocket.convertAndSendToUser(joinResponse.getInvitedUser(), "/queue/reset", new Reset(false, joinResponse.getInvitingUser()));
                }
                webSocket.convertAndSendToUser(joinResponse.getInvitingUser(), "/queue/joinResponse", joinResponse);
            }
            log.info("Join response: " + joinResponse);
        } else {
            log.error("Receipient name is null");
        }
    }

    @MessageMapping("/sendMove")
    public void receiveMoveFromPlayer(SimpMessageHeaderAccessor sha, @Payload MoveFromPlayer moveFromPlayer) {
        PreparedMove move = new PreparedMove(moveFromPlayer.getFieldNumber(), "X");
        webSocket.convertAndSendToUser(sha.getUser().getName(), "/queue/moveReceived", move);
//        webSocket.convertAndSendToUser(joinResponse.getInvitingUser(), "/queue/reset", new Reset(true));
    }

    private boolean senderIsNotRecipient(String recipient, String sender) {
        return !sender.equals(recipient);
    }

    @Override
    public void notifyActiveUserChange() {
        Set<String> activeUsers = activeUserManager.getAll();
        webSocket.convertAndSend("/topic/active", activeUsers);
    }
}
