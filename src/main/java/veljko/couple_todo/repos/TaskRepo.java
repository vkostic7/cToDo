package veljko.couple_todo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import veljko.couple_todo.entities.Task;
import veljko.couple_todo.entities.TaskStatus;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, String> {

    List<Task> findAll();

    @Query("SELECT t FROM Task t WHERE t.taskStatus = 'OPEN'")
    List<Task> findOpenTasks();

    List<Task> findByTaskStatus(TaskStatus status);

    Task findById(int id);


}
