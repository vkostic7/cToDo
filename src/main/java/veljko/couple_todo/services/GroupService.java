package veljko.couple_todo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import veljko.couple_todo.entities.Group;
import veljko.couple_todo.entities.Task;
import veljko.couple_todo.repos.GroupRepo;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    private GroupRepo groupRepo;

    public Group findByJoinCode(String code) {
        return groupRepo.findByJoinCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid join code"));
    }

    public Group save(Group group) {
        return groupRepo.save(group);
    }

    public List<Task> getGroupTasks(int groupId) {
        return groupRepo.findById(groupId)
                .map(Group::getTasks)
                .orElseThrow(() -> new RuntimeException("Group not found"));
    }
}