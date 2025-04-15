package veljko.couple_todo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import veljko.couple_todo.entities.SharedList;
import veljko.couple_todo.entities.User;

@Repository
public interface SharedListRepo extends JpaRepository<SharedList, Integer> {

    SharedList findByUser1AndUser2(User user1, User user2);

}
