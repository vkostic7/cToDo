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
import veljko.couple_todo.repos.UserTaskRepo;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
public class TaskController {

    private TaskService taskService;
    private UserService userService;
    private SharedListService sharedListService;
    private CurrentUser currentUser;
    private UserTaskRepo userTaskRepo;

    @Autowired
    public TaskController(TaskService taskService, UserService userService,
                          SharedListService sharedListService, CurrentUser currentUser,
                          UserTaskRepo userTaskRepo) {
        this.taskService = taskService;
        this.userService = userService;
        this.sharedListService = sharedListService;
        this.currentUser = currentUser;
        this.userTaskRepo = userTaskRepo;
    }

    @GetMapping("/tasks")
    public String showTasks(@RequestParam(required = false) Integer sharedWithId, Model model) {
        User user = currentUser.getCurrentUser();
        model.addAttribute("currentUser", user);

        List<Task> tasks;

        if (sharedWithId != null) {
            User joinedUser = userService.findById(sharedWithId);
            model.addAttribute("joinedUser", joinedUser);
            tasks = taskService.getTasksBetweenUsers(user.getId(), sharedWithId);

            Map<Integer, List<Task>> tasksByAssignee = new HashMap<>();

            tasksByAssignee.put(user.getId(), tasks.stream()
                    .filter(task -> task.getAssignedTo() != null && task.getAssignedTo().getId() == user.getId())
                    .collect(Collectors.toList()));

            tasksByAssignee.put(joinedUser.getId(), tasks.stream()
                    .filter(task -> task.getAssignedTo() != null && task.getAssignedTo().getId() == joinedUser.getId())
                    .collect(Collectors.toList()));

            List<Task> unassignedTasks = tasks.stream()
                    .filter(task -> task.getAssignedTo() == null)
                    .collect(Collectors.toList());

            model.addAttribute("tasksByAssignee", tasksByAssignee);
            model.addAttribute("unassignedTasks", unassignedTasks);
        } else {
            // No specific shared user - show personal tasks
            model.addAttribute("joinedUser", null);
            tasks = taskService.getTasksForUserOnly(user.getId());

            List<Task> assignedToUser = tasks.stream()
                    .filter(task -> task.getAssignedTo() != null && task.getAssignedTo().getId() == user.getId())
                    .collect(Collectors.toList());

            List<Task> unassignedTasks = tasks.stream()
                    .filter(task -> task.getAssignedTo() == null)
                    .collect(Collectors.toList());

            model.addAttribute("assignedToUser", assignedToUser);
            model.addAttribute("unassignedTasks", unassignedTasks);
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
        task.setTaskStatus(TaskStatus.OPEN);

        if (sharedWithId != null) {
            User sharedWith = userService.findById(sharedWithId);
            if (sharedWith != null) {
                SharedList sharedList = sharedListService.getOrCreateSharedList(user, sharedWith);
                task.setSharedList(sharedList);
            }
        }

        taskService.save(task);

        if (sharedWithId != null) {
            return "redirect:/tasks?sharedWithId=" + sharedWithId;
        } else {
            return "redirect:/tasks";
        }
    }

    @PostMapping("/tasks/assign/{taskId}")
    public String assignTaskToUser(
            @PathVariable int taskId,
            @RequestParam(required = false) Integer assignToUserId,
            @RequestParam(required = false) Integer sharedWithId) {

        User currentUserObj = currentUser.getCurrentUser();

        Task task = taskService.getTaskById(taskId);
        if (task == null) {
            throw new RuntimeException("Task not found");
        }

        User assignToUser;
        if (assignToUserId != null) {
            assignToUser = userService.findById(assignToUserId);
        } else {
            assignToUser = currentUserObj;
        }

        if (assignToUser == null) {
            throw new RuntimeException("User not found for assignment");
        }

        task.setAssignedTo(assignToUser);
        task.setTaskStatus(TaskStatus.IN_PROGRESS);

        Task savedTask = taskService.save(task);

        try {
            UserTask userTask = new UserTask();
            userTask.setUser(assignToUser);
            userTask.setTask(savedTask);
            userTaskRepo.save(userTask);
        } catch (Exception e) {
            System.err.println("Error creating UserTask: " + e.getMessage());
            // Ne prekidamo izvršavanje ako se desi greška, jer je najbitnije da je task dodeljen
        }

        if (sharedWithId != null) {
            return "redirect:/tasks?sharedWithId=" + sharedWithId;
        } else {
            return "redirect:/tasks";
        }
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