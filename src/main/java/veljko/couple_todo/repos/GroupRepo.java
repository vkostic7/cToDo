package veljko.couple_todo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import veljko.couple_todo.entities.Group;

import java.util.Optional;

public interface GroupRepo extends JpaRepository<Group, Integer> {

    Optional<Group> findByJoinCode(String joinCode);
}
