package veljko.couple_todo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import veljko.couple_todo.entities.User;
import veljko.couple_todo.services.UserService;
import veljko.couple_todo.utils.CurrentUser;

import java.util.List;

@Controller
public class MedjustranaController {

    private UserService userService;
    private CurrentUser currentUser;

    @Autowired
    public MedjustranaController(UserService userService, CurrentUser currentUser) {
        this.userService = userService;
        this.currentUser = currentUser;
    }

    @GetMapping("/choose")
    public String showChoosePage(Model model) {
        User user = currentUser.getCurrentUser();
        List<User> connectedUsers = userService.getConnectedUsers(user.getId());

        System.out.println("Current user: " + user.getUserName());
        System.out.println("Connected users: " + connectedUsers.size());
        for (User connectedUser : connectedUsers) {
            System.out.println("- " + connectedUser.getUserName() + " (ID: " + connectedUser.getId() + ")");
        }

        model.addAttribute("connectedUsers", connectedUsers);
        return "medjustrana";
    }
}
