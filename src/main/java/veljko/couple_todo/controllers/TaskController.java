package veljko.couple_todo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import veljko.couple_todo.entities.*;
import veljko.couple_todo.repos.GroupRepo;
import veljko.couple_todo.services.GroupService;
import veljko.couple_todo.services.TaskService;
import veljko.couple_todo.services.UserService;

import java.util.List;

@Controller
public class TaskController {

    private TaskService taskService;
    private UserService userService;
    private GroupService groupService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService, GroupService groupService) {
        this.taskService = taskService;
        this.userService = userService;
        this.groupService = groupService;
    }

    @GetMapping("/tasks")
    public String getAllTasks(Model model) {
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("user", user);

        if (user.getGroup() != null) {
            model.addAttribute("tasks", groupService.getGroupTasks(user.getGroup().getId()));
        }
        return "tasks";
    }

    @GetMapping("/tasks/new")
    public String showTaskForm(Model model) {
        model.addAttribute("difficulties", TaskDifficulty.values());
        return "new-task";
    }

    @PostMapping("/tasks/new")
    public String addTask(@ModelAttribute Task task) {
        User user = getCurrentUser();
        Group group = user.getGroup();

        task.setTaskDifficulty(TaskDifficulty.NORMAL);
        task.setGroup(group);
        task.setTaskStatus(TaskStatus.OPEN);
        taskService.addTask(task);
        return "redirect:/tasks";
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(auth.getName());
    }

}
