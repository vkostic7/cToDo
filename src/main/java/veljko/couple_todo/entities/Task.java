package veljko.couple_todo.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_difficulty", nullable = false)
    private TaskDifficulty taskDifficulty;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status")
    private TaskStatus taskStatus;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<UserTask> userTasks = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "shared_list_id", nullable = true)
    private SharedList sharedList;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;

    public Task() {
    }

    public Task(String taskName, String description, TaskDifficulty taskDifficulty, TaskStatus taskStatus, User creator, SharedList sharedList, User assignedTo) {
        this.taskName = taskName;
        this.description = description;
        this.taskDifficulty = taskDifficulty;
        this.taskStatus = taskStatus;
        this.creator = creator;
        this.sharedList = sharedList;
        this.assignedTo = assignedTo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskDifficulty getTaskDifficulty() {
        return taskDifficulty;
    }

    public void setTaskDifficulty(TaskDifficulty taskDifficulty) {
        this.taskDifficulty = taskDifficulty;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public List<UserTask> getUserTasks() {
        return userTasks;
    }

    public void setUserTasks(List<UserTask> userTasks) {
        this.userTasks = userTasks;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public SharedList getSharedList() {
        return sharedList;
    }

    public void setSharedList(SharedList sharedList) {
        this.sharedList = sharedList;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", taskDifficulty=" + taskDifficulty +
                ", taskStatus=" + taskStatus +
                ", creator=" + creator +
                ", sharedList=" + sharedList +
                ", assignedTo=" + assignedTo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return id == task.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
