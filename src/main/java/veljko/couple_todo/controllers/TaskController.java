package veljko.couple_todo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
     * Adding New Tasks to Shared List
     **/
    @GetMapping("/tasks/new")
    public String showAddTaskToSharedListForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("difficulties", TaskDifficulty.values());
        model.addAttribute("statuses", TaskStatus.values());
        User currentUser = getCurrentUser();
        User joinedUser = userService.getConnectedUser(currentUser.getId());
        model.addAttribute("joinedUser", joinedUser); // Potrebno za formu
        return "new-shared-task"; // Kreiraj novu formu: new-shared-task.html
    }

    @PostMapping("/tasks/new/shared")
    public String addTaskToSharedList(@ModelAttribute Task task, @RequestParam(value = "connectedUserId", required = false) Integer connectedUserId) {
        User currentUser = getCurrentUser();

        if (currentUser == null) {
            throw new RuntimeException("Current user must exist before creating a task");
        }

        if (connectedUserId == null) {
            throw new RuntimeException("Must specify the connected user for a shared task");
        }

        task.setCreator(currentUser);
        taskService.addTaskToSharedList(currentUser.getId(), connectedUserId, task);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/assign/{taskId}")
    public String assignTaskToUser(@PathVariable int taskId) {
        // Implementacija dodele taska jednom od povezanih korisnika
        // Ovo će zahtevati dodatnu logiku i formu za odabir korisnika
        // Za sada ostavljamo kao placeholder
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