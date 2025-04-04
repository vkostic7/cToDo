package veljko.couple_todo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import veljko.couple_todo.entities.Task;
import veljko.couple_todo.entities.TaskStatus;
import veljko.couple_todo.entities.User;
import veljko.couple_todo.entities.UserTask;
import veljko.couple_todo.repos.TaskRepo;
import veljko.couple_todo.repos.UserTaskRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepo taskRepo;
    private UserTaskRepo userTaskRepo;
    private UserService userService;

    @Autowired
    public TaskService(TaskRepo taskRepo, UserTaskRepo userTaskRepo, UserService userService) {
        this.taskRepo = taskRepo;
        this.userTaskRepo = userTaskRepo;
        this.userService = userService;
    }

    public Task getTaskById(int id) {
       return taskRepo.findById(id);
    }

    public List<Task> getTasksForUser(int userId) {
        return taskRepo.findTasksByUserAccess(userId);
    }

    public List<Task> getTaskByStatus(TaskStatus status) {
        return taskRepo.findByTaskStatus(status);
    }

    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    @Transactional
    public Task addTask(Task task) {
        if (task.getCreator() == null) {
            throw new RuntimeException("Task must have a creator!");
        }
        task.setTaskStatus(TaskStatus.OPEN);
        return taskRepo.save(task);
    }

    @Transactional
    public Task addTaskWithRelation(Task task, User user) {
        if (task.getCreator() == null) {
            throw new RuntimeException("Task must have a creator!");
        }

        task.setTaskStatus(TaskStatus.OPEN);
        Task savedTask = taskRepo.save(task);

        // Dodaj zapis u user_tasks
        UserTask ut = new UserTask();
        ut.setUser(user);
        ut.setTask(savedTask);
        userTaskRepo.save(ut);

        return savedTask;
    }

    @Transactional
    public Task updateTaskStatus(int taskId, TaskStatus status) {
        Task task = taskRepo.findById(taskId);
        task.setTaskStatus(status);
        return taskRepo.save(task);
    }

    public List<Task> getTasksForUserAndConnections(int userId) {
        List<Integer> userIds = new ArrayList<>();
        userIds.add(userId);

        User connected = userService.getConnectedUser(userId);
        if (connected != null) {
            userIds.add(connected.getId());
        }

        return taskRepo.findByCreatorIdIn(userIds);
    }


}