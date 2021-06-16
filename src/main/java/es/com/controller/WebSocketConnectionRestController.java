package es.com.controller;

import es.com.dto.MessageResponse;
import es.com.dto.UserDTO;
import es.com.util.ActiveUserManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@CrossOrigin
@Log4j2
public class WebSocketConnectionRestController {

    @Autowired
    private ActiveUserManager activeSessionManager;

    @PostMapping("/rest/user-connect")
    public ResponseEntity<?> userConnect(@RequestBody UserDTO user, HttpServletRequest request) {
        String remoteAddr = getRemoteAddress(request);
        return manageConnectedUser(user, remoteAddr);
    }

    private ResponseEntity<MessageResponse> manageConnectedUser(UserDTO user, String remoteAddr) {
        if(userAlreadyExists(user.getUsername())) {
            log.info("User " + user.getUsername() + " already exists");
            return ResponseEntity.ok(new MessageResponse("User " + user.getUsername() + " already exists", false));
        } else {
            activeSessionManager.add(user.getUsername(), remoteAddr);
            log.info("User " + user.getUsername() + " connected");
            return ResponseEntity.ok(new MessageResponse("User " + user.getUsername() + " connected", true));
        }
    }

    private String getRemoteAddress(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("Remote_Addr");
            if (StringUtils.isEmpty(remoteAddr)) {
                remoteAddr = request.getHeader("X-FORWARDED-FOR");
                if (remoteAddr == null || "".equals(remoteAddr)) {
                    remoteAddr = request.getRemoteAddr();
                }
            }
        }
        return remoteAddr;
    }

    private boolean userAlreadyExists(String username) {
        return activeSessionManager.userAlreadyExists(username);
    }

    @PostMapping("/rest/user-disconnect")
    public ResponseEntity<?> userDisconnect(@RequestBody UserDTO user) {
        activeSessionManager.remove(user.getUsername());
        log.info("User " + user.getUsername() + " disconnected");
        return ResponseEntity.ok(new MessageResponse("User " + user.getUsername() + " disconnected", true));
    }

    @GetMapping("/rest/active-users-except/{userName}")
    public ResponseEntity<?> getActiveUsersExceptCurrentUser(@PathVariable String userName) {
        ArrayList<String> users = new ArrayList<>(activeSessionManager.getActiveUsersExceptCurrentUser(userName));
        log.info("Active users: " + users);
        return ResponseEntity.ok(users);
    }
}

