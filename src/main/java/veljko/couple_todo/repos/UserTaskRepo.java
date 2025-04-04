package veljko.couple_todo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import veljko.couple_todo.entities.UserTask;

public interface UserTaskRepo extends JpaRepository<UserTask, Integer> {
}
