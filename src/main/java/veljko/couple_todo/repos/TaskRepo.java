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

    Task findById(int id);

    List<Task> findByCreatorIdIn(List<Integer> ids);

    public List<Task> findByCreatorIdAndSharedListIsNull(Integer creatorId);

    @Query("""
    SELECT t FROM Task t
    WHERE t.sharedList.user1.id = :userAId AND t.sharedList.user2.id = :userBId
    OR t.sharedList.user1.id = :userBId AND t.sharedList.user2.id = :userAId
""")
    List<Task> findSharedTasksBetweenUsers(@Param("userAId") int userAId, @Param("userBId") int userBId);

}
