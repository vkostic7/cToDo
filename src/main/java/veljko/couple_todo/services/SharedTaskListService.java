package veljko.couple_todo.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import veljko.couple_todo.entities.SharedTaskList;
import veljko.couple_todo.entities.UserConnection;
import veljko.couple_todo.repos.SharedTaskListRepo;
import veljko.couple_todo.repos.UserConnectionRepo;

import java.util.List;
import java.util.Optional;

@Service
public class SharedTaskListService {

    private final SharedTaskListRepo sharedTaskListRepo;
    private final UserConnectionRepo userConnectionRepo;

    @Autowired
    public SharedTaskListService(SharedTaskListRepo sharedTaskListRepo, UserConnectionRepo userConnectionRepo) {
        this.sharedTaskListRepo = sharedTaskListRepo;
        this.userConnectionRepo = userConnectionRepo;
    }

    @Transactional
    public SharedTaskList createSharedTaskListForConnection(UserConnection connection) {
        SharedTaskList sharedTaskList = new SharedTaskList();
        sharedTaskList.setConnection(connection);
        return sharedTaskListRepo.save(sharedTaskList);
    }

    public SharedTaskList findByConnection(UserConnection connection) {
        Optional<SharedTaskList> optionalSharedTaskList = sharedTaskListRepo.findByConnection(connection);
        if (optionalSharedTaskList.isPresent()) {
            return optionalSharedTaskList.get();
        }
        return null;
    }
}
