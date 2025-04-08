package veljko.couple_todo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import veljko.couple_todo.entities.User;
import veljko.couple_todo.entities.UserConnection;
import veljko.couple_todo.repos.UserConnectionRepo;
import veljko.couple_todo.repos.UserRepo;

@Service
public class UserConnectionService {

    private final UserConnectionRepo userConnectionRepo;
    private final UserRepo userRepo;

    @Autowired
    public UserConnectionService(UserConnectionRepo userConnectionRepo, UserRepo userRepo) {
        this.userConnectionRepo = userConnectionRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public void connectUsers(String inviteCode, User invitedUser) {

        User inviter = userRepo.findByInviteCode(inviteCode);
        UserConnection connection = new UserConnection();

        if (inviter == null) {
            throw new RuntimeException("Invalid invite code");
        }
        boolean alreadyConnected = userConnectionRepo.existsConnectionBetween(inviter, invitedUser);
        if (alreadyConnected) {
            throw new RuntimeException("Users are already connected.");
        }
        connection.setInviter(inviter);
        connection.setInvited(invitedUser);

        userConnectionRepo.save(connection);
    }
}
