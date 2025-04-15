package veljko.couple_todo.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "invite_code")
    private String inviteCode;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<UserTask> userTasks = new ArrayList<>();

    @OneToMany(mappedBy = "inviter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserConnection> invitedUsers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_connections",
            joinColumns = @JoinColumn(name = "inviter_id"),
            inverseJoinColumns = @JoinColumn(name = "invited_id")
    )
    private List<User> connectedUsers = new ArrayList<>();

    public List<User> getConnectedUsers() {
        return connectedUsers;
    }

    public void setConnectedUsers(List<User> connectedUsers) {
        this.connectedUsers = connectedUsers;
    }

    public List<UserConnection> getInvitedUsers() {
        return invitedUsers;
    }

    public void setInvitedUsers(List<UserConnection> invitedUsers) {
        this.invitedUsers = invitedUsers;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserTask> getUserTasks() {
        return userTasks;
    }

    public void setUserTasks(List<UserTask> userTasks) {
        this.userTasks = userTasks;
    }

    public User getPartner() {
        if (connectedUsers != null && !connectedUsers.isEmpty()) {
            return connectedUsers.get(0);
        }
        return null;
    }
}
