package veljko.couple_todo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import veljko.couple_todo.entities.User;
import veljko.couple_todo.services.UserConnectionService;
import veljko.couple_todo.services.UserService;
import veljko.couple_todo.utils.CurrentUser;

@Controller
public class UserController {

    private final UserService userService;
    private final UserConnectionService userConnectionService;
    private final CurrentUser currentUser;

    @Autowired
    public UserController(UserService userService, UserConnectionService userConnectionService, CurrentUser currentUser) {
        this.userService = userService;
        this.userConnectionService = userConnectionService;
        this.currentUser = currentUser;
    }

    @PostMapping("/users/connect")
    public String connectUser(@RequestParam String inviteCode) {
        User user = currentUser.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        try {
            userConnectionService.connectUsers(inviteCode, user);
        } catch (RuntimeException e) {
            return "redirect:/choose?error=" + e.getMessage();
        }
        return "redirect:/tasks";
    }

    @GetMapping("users/connect/generate")
    public String generateInviteCode(Model model) {
        User user = currentUser.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        String inviteCode = userService.generateInviteCode(user);
        model.addAttribute("inviteCode", inviteCode);
        return "generate-code";
    }
}