package veljko.couple_todo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import veljko.couple_todo.entities.*;
import veljko.couple_todo.services.TaskService;
import veljko.couple_todo.services.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TaskController {

    private TaskService taskService;
    private UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public String showTasks(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userService.findByUsername(username);

        model.addAttribute("currentUser", currentUser);

        User joinedUser = userService.getConnectedUser(currentUser.getId());
        model.addAttribute("joinedUser", joinedUser);

        List<Task> tasks = taskService.getTasksForUserAndConnections(currentUser.getId());
        model.addAttribute("tasks", tasks);

        return "tasks";
    }

     /**
     * Adding New Tasks
     **/
    @GetMapping("/tasks/new")
    public String showTaskForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("difficulties", TaskDifficulty.values());
        model.addAttribute("statuses", TaskStatus.values());
        return "new-task";
    }

    @PostMapping("/tasks/new")
    public String addTask(@ModelAttribute Task task) {
        User currentUser = getCurrentUser();

        if (currentUser == null) {
            throw new RuntimeException("Current user must exist before creating a task");
        }

        task.setCreator(currentUser);
        taskService.addTaskWithRelation(task, currentUser);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/assign/{taskId}")
    public String assignTaskToUser(@PathVariable int taskId) {
        User user = getCurrentUser();

        Task task = taskService.getTaskById(taskId);
        if (task == null || task.getTaskStatus() != TaskStatus.OPEN) {
            throw new RuntimeException("Task is not available for assignment"); // TODO: Implement exception
        }

        UserTask userTask = new UserTask();
        userTask.setUser(user);
        userTask.setTask(task);

        user.getUserTasks().add(userTask);
        userService.save(user);

        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/delete/{taskId}")
    public String deleteTask(@PathVariable int taskId) {
        taskService.deleteTask(taskId);
        return "redirect:/tasks";
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(auth.getName());
    }
}