package veljko.couple_todo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import veljko.couple_todo.entities.ConnectionStatus;
import veljko.couple_todo.entities.Group;
import veljko.couple_todo.entities.User;
import veljko.couple_todo.repos.GroupRepo;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConnectionService {

    @Autowired
    UserService userService;

    @Autowired
    private GroupRepo groupRepo;

    public String generateCode() {
        String code = generateRandomCode();
        User currentUser = getCurrentUser();

        Group group = new Group();
        group.setJoinCode(code);
        group.setUser1(currentUser);
        groupRepo.save(group);

        currentUser.setGroup(group);
        currentUser.setStatus(ConnectionStatus.PENDING);
        userService.save(currentUser);

        return code;
    }

    public void connectUsers(String code) {
        Group group = groupRepo.findByJoinCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid join code"));

        User joiner = getCurrentUser();
        User inviter = group.getUser1();

        if (group.getUser2() != null) {
            throw new RuntimeException("Group is already full");
        }

        group.setUser2(joiner);
        joiner.setGroup(group);
        joiner.setStatus(ConnectionStatus.CONNECTED);
        inviter.setStatus(ConnectionStatus.CONNECTED);

        groupRepo.save(group);
        userService.save(joiner);
        userService.save(inviter);
    }

    private String generateRandomCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(auth.getName());
    }

}
