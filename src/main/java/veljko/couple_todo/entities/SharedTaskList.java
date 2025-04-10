package veljko.couple_todo.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shared_task_list")
public class SharedTaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "connection_id", unique = true)
    private UserConnection connection;

    @OneToMany(mappedBy = "sharedTaskList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserConnection getConnection() {
        return connection;
    }

    public void setConnection(UserConnection connection) {
        this.connection = connection;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
        task.setSharedTaskList(this);
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
        task.setSharedTaskList(null);
    }
}
