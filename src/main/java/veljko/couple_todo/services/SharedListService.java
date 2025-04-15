package veljko.couple_todo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import veljko.couple_todo.entities.SharedList;
import veljko.couple_todo.entities.User;
import veljko.couple_todo.repos.SharedListRepo;

@Service
public class SharedListService {

    @Autowired
    private SharedListRepo sharedListRepo;

    public SharedList getOrCreateSharedList(User user1, User user2) {
        int id1 = user1.getId();
        int id2 = user2.getId();

        if (id1 > id2) {
            User temp = user1;
            user1 = user2;
            user2 = temp;
        }

        SharedList existing = sharedListRepo.findByUser1AndUser2(user1, user2);
        if (existing != null) {
            return existing;
        }

        SharedList sharedList = new SharedList();
        sharedList.setUser1(user1);
        sharedList.setUser2(user2);
        return sharedListRepo.save(sharedList);
    }
}
