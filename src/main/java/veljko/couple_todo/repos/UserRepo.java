package veljko.couple_todo.repos;

import veljko.couple_todo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    List<User> findAll();

    User findById(int id);

    User findByUserName(String userName);

    User findByInviteCode(String inviteCode);
}
