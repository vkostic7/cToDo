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
    private final SharedTaskListService sharedTaskListService;

    @Autowired
    public UserConnectionService(UserConnectionRepo userConnectionRepo, UserRepo userRepo, SharedTaskListService sharedTaskListService) {
        this.userConnectionRepo = userConnectionRepo;
        this.userRepo = userRepo;
        this.sharedTaskListService = sharedTaskListService;
    }

    @Transactional
    public void connectUsers(String inviteCode, User invitedUser) {
        User inviter = userRepo.findByInviteCode(inviteCode);
        if (inviter == null) {
            throw new RuntimeException("Invalid invite code");
        }
        boolean alreadyConnected = userConnectionRepo.existsConnectionBetween(inviter, invitedUser);
        if (alreadyConnected) {
            throw new RuntimeException("Users are already connected.");
        }

        UserConnection connection = new UserConnection();
        connection.setInviter(inviter);
        connection.setInvited(invitedUser);
        UserConnection savedConnection = userConnectionRepo.save(connection);

        sharedTaskListService.createSharedTaskListForConnection(savedConnection);
    }
}
