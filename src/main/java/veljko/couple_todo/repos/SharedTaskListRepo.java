package veljko.couple_todo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import veljko.couple_todo.entities.SharedTaskList;
import veljko.couple_todo.entities.UserConnection;

import java.util.List;
import java.util.Optional;

@Repository
public interface SharedTaskListRepo extends JpaRepository<SharedTaskList, Integer> {

    Optional<SharedTaskList> findByConnection(UserConnection connection);

}
