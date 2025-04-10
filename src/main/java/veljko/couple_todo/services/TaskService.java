package veljko.couple_todo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import veljko.couple_todo.entities.*;
import veljko.couple_todo.repos.TaskRepo;
import veljko.couple_todo.repos.UserConnectionRepo;
import veljko.couple_todo.repos.UserRepo;
import veljko.couple_todo.repos.UserTaskRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private TaskRepo taskRepo;
    private UserTaskRepo userTaskRepo; // Možda će biti uklonjen ili korišćen samo za individualne taskove
    private UserService userService;
    private UserConnectionRepo userConnectionRepo;
    private SharedTaskListService sharedTaskListService;
    private UserRepo userRepo;

    @Autowired
    public TaskService(TaskRepo taskRepo, UserTaskRepo userTaskRepo, UserService userService, UserConnectionRepo userConnectionRepo, SharedTaskListService sharedTaskListService, UserRepo userRepo) {
        this.taskRepo = taskRepo;
        this.userTaskRepo = userTaskRepo;
        this.userService = userService;
        this.userConnectionRepo = userConnectionRepo;
        this.sharedTaskListService = sharedTaskListService;
        this.userRepo = userRepo;
    }

    public Task getTaskById(int id) {
        return taskRepo.findById(id);
    }

    // Ova metoda može ostati ako planiraš individualne taskove
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
    public void addTaskToSharedList(int currentUserId, int connectedUserId, Task task) {
        UserConnection connection = findConnection(currentUserId, connectedUserId);
        if (connection == null) {
            throw new RuntimeException("Connection not found between users");
        }

        SharedTaskList sharedTaskList = sharedTaskListService.findByConnection(connection);
        if (sharedTaskList == null) {
            throw new RuntimeException("SharedTaskList not found for this connection");
        }

        User creator = userRepo.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        task.setCreator(creator);
        task.setSharedTaskList(sharedTaskList);
        taskRepo.save(task);
    }

    @Transactional
    public List<Task> getTasksForUserAndConnections(int userId) {
        List<Task> allTasks = new ArrayList<>();

        // Dohvati individualne taskove korisnika (ako ih ima)
        // List<Task> individualTasks = taskRepo.findByCreatorId(userId);
        // if (individualTasks != null) {
        //     allTasks.addAll(individualTasks);
        // }

        // Dohvati taskove iz deljenih lista sa kojima je korisnik povezan
        List<UserConnection> connectionsAsInviter = userConnectionRepo.findByInviterId(userId);
        for (UserConnection connection : connectionsAsInviter) {
            SharedTaskList sharedTaskList = sharedTaskListService.findByConnection(connection);
            if (sharedTaskList != null) {
                List<Task> tasks = sharedTaskList.getTasks();
                if (tasks != null) {
                    allTasks.addAll(tasks);
                }
            }
        }

        List<UserConnection> connectionsAsInvited = userConnectionRepo.findByInvitedId(userId);
        for (UserConnection connection : connectionsAsInvited) {
            SharedTaskList sharedTaskList = sharedTaskListService.findByConnection(connection);
            if (sharedTaskList != null) {
                List<Task> tasks = sharedTaskList.getTasks();
                if (tasks != null) {
                    allTasks.addAll(tasks);
                }
            }
        }

        return allTasks;
    }

    @Transactional
    public void deleteTask(int taskId) {
        // Proveri da li je task individualni ili deljeni i obriši ga na odgovarajući način
        Optional<Task> optionalTaskToDelete = Optional.ofNullable(taskRepo.findById(taskId));
        if (optionalTaskToDelete.isPresent()) {
            Task taskToDelete = optionalTaskToDelete.get();
            if (taskToDelete.getSharedTaskList() != null) {
                // Task je deljeni, samo ga obriši
                taskRepo.deleteById(taskId);
            } else {
                // Task je individualni (ako koristiš UserTask), obriši vezu pa task
                userTaskRepo.deleteTaskById(taskId);
                taskRepo.deleteById(taskId);
            }
        }
    }

    private UserConnection findConnection(int userId1, int userId2) {
        User inviter1 = userRepo.findById(userId1).orElse(null);
        User invited1 = userRepo.findById(userId2).orElse(null);
        if (inviter1 == null || invited1 == null) return null;

        Optional<UserConnection> connection1Optional = userConnectionRepo.findByInviterAndInvited(inviter1, invited1);
        if (connection1Optional.isPresent()) {
            return connection1Optional.get();
        }

        Optional<UserConnection> connection2Optional = userConnectionRepo.findByInviterAndInvited(invited1, inviter1);
        return connection2Optional.orElse(null);
    }
}