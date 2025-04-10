package veljko.couple_todo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import veljko.couple_todo.entities.User;
import veljko.couple_todo.entities.UserConnection;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserConnectionRepo extends JpaRepository<UserConnection, Integer> {

    List<UserConnection> findByInviterId(int inviterId);

    List<UserConnection> findByInvitedId(int invitedId);

    Optional<UserConnection> findByInviterAndInvited(User inviter, User invited);

    @Query("SELECT uc FROM UserConnection uc WHERE uc.invited.id = :invitedId")
    List<UserConnection> findAllByInvitedId(@Param("invitedId") int invitedId);

    @Query("""
    SELECT COUNT(uc) > 0
    FROM UserConnection uc
    WHERE (uc.inviter = :user1 AND uc.invited = :user2)
       OR (uc.inviter = :user2 AND uc.invited = :user1)
    """)
    boolean existsConnectionBetween(User user1, User user2);

}
