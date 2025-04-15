package veljko.couple_todo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import veljko.couple_todo.entities.User;
import veljko.couple_todo.entities.UserConnection;

import java.util.List;

@Repository
public interface UserConnectionRepo extends JpaRepository<UserConnection, Integer> {

    @Query("SELECT uc FROM UserConnection uc WHERE uc.inviter.id = :inviterId")
    List<UserConnection> findByInviterId(@Param("inviterId") int inviterId);

    @Query("SELECT uc FROM UserConnection uc WHERE uc.invited.id = :invitedId")
    List<UserConnection> findByInvitedId(@Param("invitedId") int invitedId);

    @Query("""
    SELECT COUNT(uc) > 0
    FROM UserConnection uc
    WHERE (uc.inviter = :user1 AND uc.invited = :user2)
       OR (uc.inviter = :user2 AND uc.invited = :user1)
    """)
    boolean existsConnectionBetween(User user1, User user2);

}
