package veljko.couple_todo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "user_connections")
public class UserConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "inviter_id")
    private User inviter;

    @ManyToOne
    @JoinColumn(name = "invited_id")
    private User invited;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getInviter() {
        return inviter;
    }

    public void setInviter(User inviter) {
        this.inviter = inviter;
    }

    public User getInvited() {
        return invited;
    }

    public void setInvited(User invited) {
        this.invited = invited;
    }
}
