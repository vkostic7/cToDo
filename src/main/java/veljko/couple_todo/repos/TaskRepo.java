package veljko.couple_todo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import veljko.couple_todo.entities.Task;
import veljko.couple_todo.entities.TaskStatus;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Integer> {

    List<Task> findAll();

    @Query("SELECT t FROM Task t WHERE t.taskStatus = 'OPEN'")
    List<Task> findOpenTasks();

    List<Task> findByTaskStatus(TaskStatus status);

    public Task findById(int id);

    @Query("SELECT t FROM Task t JOIN UserTask ut ON ut.task.id = t.id WHERE ut.user.id = :userId OR t.creator.id = :userId")
    List<Task> findTasksByUserAccess(@Param("userId") int userId);

    @Query("""
    SELECT t FROM Task t 
    WHERE t.creator.id = :userId 
    OR t.creator.id IN (
        SELECT uc.inviter.id 
        FROM UserConnection uc 
        WHERE uc.invited.id = :userId
    )
""")
    List<Task> findTasksByUserAndConnections(@Param("userId") int userId);

    List<Task> findByCreatorIdIn(List<Integer> ids);


}
