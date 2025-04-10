package veljko.couple_todo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import veljko.couple_todo.entities.User;
import veljko.couple_todo.entities.UserConnection;
import veljko.couple_todo.repos.UserConnectionRepo;
import veljko.couple_todo.repos.UserRepo;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    private UserConnectionRepo userConnectionRepo;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, UserConnectionRepo userConnectionRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.userConnectionRepo = userConnectionRepo;
    }

    public User findByUsername(String username) {
        return userRepo.findByUserName(username);
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    @Transactional
    public User registerUser(User user) {
        User existingUser = userRepo.findByUserName(user.getUserName());

        if (existingUser != null) {
            throw new RuntimeException("User already exists"); // TODO Implement exceptioj
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Transactional
    public String generateInviteCode(User user) {
        String inviteCode = UUID.randomUUID().toString();
        user.setInviteCode(inviteCode);
        save(user);
        return inviteCode;
    }

    @Transactional
    public User getInviterForUser(int invitedUserId) {
        UserConnection connection = (UserConnection) userConnectionRepo.findByInvitedId(invitedUserId);
        if (connection != null) {
            return connection.getInviter();
        }
        return null;
    }

    @Transactional
    public User getConnectedUser(int currentUserId) {
        List<UserConnection> asInviterList = userConnectionRepo.findByInviterId(currentUserId);
        if (!asInviterList.isEmpty()) {
            return asInviterList.get(0).getInvited();
        }

        List<UserConnection> asInvitedList = userConnectionRepo.findByInvitedId(currentUserId);
        if (!asInvitedList.isEmpty()) {
            return asInvitedList.get(0).getInviter();
        }

        return null;
    }
}