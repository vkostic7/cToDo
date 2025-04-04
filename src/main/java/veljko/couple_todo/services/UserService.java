package veljko.couple_todo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import veljko.couple_todo.entities.User;
import veljko.couple_todo.entities.UserConnection;
import veljko.couple_todo.repos.UserConnectionRepo;
import veljko.couple_todo.repos.UserRepo;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserConnectionRepo userConnectionRepo;

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

    public User getInviterForUser(int invitedUserId) {
        UserConnection connection = userConnectionRepo.findByInvitedId(invitedUserId);
        if (connection != null) {
            return connection.getInviter(); // Vrati pozivaoca (inviter)
        }
        return null; // Ako nema konekcije, vrati null
    }


    @Transactional
    public void connectUsers(String inviteCode, User invitedUser) {
        // Proveri da li pozivni kod postoji
        User inviter = userRepo.findByInviteCode(inviteCode);

        if (inviter == null) {
            throw new RuntimeException("Invalid invite code");
        }

        // Proveri da li je lozinka za invitedUser postavljena
        if (invitedUser.getPassword() == null || invitedUser.getPassword().isEmpty()) {
            throw new RuntimeException("Password cannot be null or empty");
        }

        // Šifruj lozinku za invitedUser
        invitedUser.setPassword(passwordEncoder.encode(invitedUser.getPassword()));

        // Sačuvaj invitedUser pre dodavanja veze ako još nije sačuvan
        if (invitedUser.getId() == 0) {
            invitedUser = userRepo.save(invitedUser);
        }

        // Kreiraj vezu između inviter i invitedUser
        UserConnection connection = new UserConnection();
        connection.setInviter(inviter);
        connection.setInvited(invitedUser);

        inviter.getInvitedUsers().add(connection);
        userRepo.save(inviter); // Sačuvaj pozivaoca sa vezama
    }

    public User getConnectedUser(int currentUserId) {
        UserConnection asInviter = userConnectionRepo.findByInviterId(currentUserId);
        if (asInviter != null) {
            return asInviter.getInvited();
        }

        UserConnection asInvited = userConnectionRepo.findByInvitedId(currentUserId);
        if (asInvited != null) {
            return asInvited.getInviter();
        }

        return null;
    }


}