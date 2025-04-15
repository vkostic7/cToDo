package veljko.couple_todo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import veljko.couple_todo.entities.*;
import veljko.couple_todo.services.SharedListService;
import veljko.couple_todo.services.TaskService;
import veljko.couple_todo.services.UserService;
import veljko.couple_todo.utils.CurrentUser;

import java.util.List;

@Controller
public class TaskController {

    private TaskService taskService;
    private UserService userService;
    private SharedListService sharedListService;
    private CurrentUser currentUser;

    @Autowired
    public TaskController(TaskService taskService, UserService userService,
                          SharedListService sharedListService, CurrentUser currentUser) {
        this.taskService = taskService;
        this.userService = userService;
        this.sharedListService = sharedListService;
        this.currentUser = currentUser;
    }

    @GetMapping("/tasks")
    public String showTasks(@RequestParam(required = false) Integer sharedWithId, Model model) {
        User user = currentUser.getCurrentUser();
        model.addAttribute("currentUser", user);

        List<Task> tasks;
        if (sharedWithId != null) {
            // Specific shared user selected
            User joinedUser = userService.findById(sharedWithId);
            model.addAttribute("joinedUser", joinedUser);
            tasks = taskService.getTasksBetweenUsers(user.getId(), sharedWithId);
        } else {
            // No specific shared user - show personal tasks
            model.addAttribute("joinedUser", null);
            tasks = taskService.getTasksForUserOnly(user.getId());
        }

        model.addAttribute("tasks", tasks);
        return "tasks";
    }

    @GetMapping("/tasks/new")
    public String showTaskForm(Model model, @RequestParam(required = false) Integer sharedWithId) {
        model.addAttribute("task", new Task());
        model.addAttribute("difficulties", TaskDifficulty.values());
        model.addAttribute("statuses", TaskStatus.values());

        model.addAttribute("sharedWithId", sharedWithId);
        return "new-task";
    }

    @PostMapping("/tasks/new")
    public String addTask(@ModelAttribute Task task, @RequestParam(required = false) Integer sharedWithId) {
        User user = currentUser.getCurrentUser();

        if (user == null) {
            throw new RuntimeException("Current user must exist before creating a task");
        }

        task.setCreator(user);

        if (sharedWithId != null) {
            User sharedWith = userService.findById(sharedWithId);
            if (sharedWith != null) {
                SharedList sharedList = sharedListService.getOrCreateSharedList(user, sharedWith);
                task.setSharedList(sharedList);
            }
        }
        // Otherwise its personal

        taskService.save(task);

        // Redirect back to the same view
        if (sharedWithId != null) {
            return "redirect:/tasks?sharedWithId=" + sharedWithId;
        } else {
            return "redirect:/tasks";
        }
    }

    @PostMapping("/tasks/assign/{taskId}")
    public String assignTaskToUser(@PathVariable int taskId) {
        User user = currentUser.getCurrentUser();

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
    public String deleteTask(@PathVariable int taskId,
                             @RequestParam(required = false) Integer sharedWithId) {
        taskService.deleteTask(taskId);

        if (sharedWithId != null) {
            return "redirect:/tasks?sharedWithId=" + sharedWithId;
        } else {
            return "redirect:/tasks";
        }
    }

}