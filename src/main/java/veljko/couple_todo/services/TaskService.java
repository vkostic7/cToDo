package veljko.couple_todo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import veljko.couple_todo.entities.Task;
import veljko.couple_todo.entities.TaskStatus;
import veljko.couple_todo.repos.GroupRepo;
import veljko.couple_todo.repos.TaskRepo;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepo taskRepo;
    private final GroupRepo groupRepository;

    @Autowired
    public TaskService(TaskRepo taskRepo, GroupRepo groupRepository) {
        this.taskRepo = taskRepo;
        this.groupRepository = groupRepository;
    }

    public List<Task> getTaskByStatus(TaskStatus status) {
        return taskRepo.findByTaskStatus(status);
    }

    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    @Transactional
    public Task addTask(Task task) {
        task.setTaskStatus(TaskStatus.OPEN);
        return taskRepo.save(task);
    }

    @Transactional
    public Task updateTaskStatus(int taskId, TaskStatus status) {
        Task task = taskRepo.findById(taskId);
        task.setTaskStatus(status);
        return taskRepo.save(task);
    }
}