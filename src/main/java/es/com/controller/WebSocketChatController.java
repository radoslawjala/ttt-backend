package es.com.controller;

import es.com.dto.JoinRequest;
import es.com.dto.JoinResponse;
import es.com.dto.Reset;
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

//    @MessageMapping("/chat")
//    public void send(SimpMessageHeaderAccessor sha, @Payload ChatMessage message) {
//        if(message.getRecipient() != null) {
//            message.setTime(StringUtils.getCurrentFormattedTime(Long.parseLong(message.getTime())));
//            String sender = sha.getUser().getName();
//            log.info("Sender name from SimpMessageHeader: " + sender);
//            if (senderIsNotRecipient(message.getRecipient(), sender)) {
//                webSocket.convertAndSendToUser(sender, "/queue/messages", message);
//            }
//            webSocket.convertAndSendToUser(message.getRecipient(), "/queue/messages", message);
//            log.info("Received message: " + message);
//        } else {
//            log.error("Recipient name is null");
//        }
//    }

    @MessageMapping("/joinrequest")
    public void joinRequest(SimpMessageHeaderAccessor sha, @Payload JoinRequest joinRequest) {
        if(joinRequest != null) {
            if(sha.getUser().getName().equals(joinRequest.getInvitingUser())) {
                webSocket.convertAndSendToUser(joinRequest.getInvitedUser(), "/queue/joinrequest", joinRequest);
            }
            log.info("Join request: " + joinRequest);
        } else {
            log.error("Recipient name is null");
        }
    }

    @MessageMapping("/joinresponse")
    public void joinResponse(SimpMessageHeaderAccessor sha, @Payload JoinResponse joinResponse) {
        if(joinResponse != null) {
            if(sha.getUser().getName().equals(joinResponse.getInvitedUser())) {
                webSocket.convertAndSendToUser(joinResponse.getInvitingUser(), "/queue/joinresponse", joinResponse);
                webSocket.convertAndSendToUser(joinResponse.getInvitingUser(), "/queue/reset", new Reset(false));
                webSocket.convertAndSendToUser(joinResponse.getInvitedUser(), "/queue/reset", new Reset(true));
            }
            log.info("Join response: " + joinResponse);
        } else {
            log.error("Receipient name is null");
        }
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
