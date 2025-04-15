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

    private TaskRepo taskRepo;
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

    @Transactional
    public void addTaskWithRelation(Task task, User user) {
        if (task.getCreator() == null) {
            throw new RuntimeException("Task must have a creator!");
        }

        task.setTaskStatus(TaskStatus.OPEN);
        Task savedTask = taskRepo.save(task);

        UserTask ut = new UserTask();
        ut.setUser(user);
        ut.setTask(savedTask);
        userTaskRepo.save(ut);
    }

    @Transactional
    public List<Task> getTasksForUserAndConnections(int userId) {
        List<Integer> userIds = new ArrayList<>();
        userIds.add(userId);

        User connected = userService.getConnectedUser(userId);
        if (connected != null) {
            userIds.add(connected.getId());
        }
        return taskRepo.findByCreatorIdIn(userIds);
    }

    @Transactional
    public void deleteTask(int taskId) {
        userTaskRepo.deleteTaskById(taskId);
        taskRepo.deleteById(taskId);
    }

    @Transactional
    public Task save(Task task) {
        return taskRepo.save(task);
    }

    @Transactional
    public List<Task> getTasksBetweenUsers(int userAId, int userBId) {
        return taskRepo.findSharedTasksBetweenUsers(userAId, userBId);
    }

    @Transactional
    public List<Task> getTasksForUserOnly(int userId) {
        return taskRepo.findByCreatorIdAndSharedListIsNull(userId);
    }
}