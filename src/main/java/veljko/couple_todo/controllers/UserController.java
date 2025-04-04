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
import veljko.couple_todo.entities.User;
import veljko.couple_todo.services.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/connect")
    public String connectUser(@RequestParam String inviteCode) {
        User currentUser = getCurrentUser();

        try {
            userService.connectUsers(inviteCode, currentUser);
        } catch (RuntimeException e) {
            return "redirect:/choose?error=" + e.getMessage();
        }

        return "redirect:/tasks";
    }

    @GetMapping("users/connect/generate")
    public String generateInviteCode(Model model) {
        User currentUser = getCurrentUser();
        String inviteCode = userService.generateInviteCode(currentUser);
        model.addAttribute("inviteCode", inviteCode);
        return "generate-code";
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(auth.getName());
    }
}
