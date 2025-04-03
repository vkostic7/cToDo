package veljko.couple_todo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import veljko.couple_todo.entities.Task;
import veljko.couple_todo.entities.User;
import veljko.couple_todo.repos.UserRepo;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public User findByUsername(String username) {
        return userRepo.findByUserName(username);
    }

    public User save(User user) {
        return userRepo.save(user);
    }
}