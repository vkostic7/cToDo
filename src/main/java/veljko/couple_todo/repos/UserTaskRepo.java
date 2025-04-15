package veljko.couple_todo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import veljko.couple_todo.entities.UserTask;

@Repository
public interface UserTaskRepo extends JpaRepository<UserTask, Integer> {

    void deleteTaskById(int taskId);
}
