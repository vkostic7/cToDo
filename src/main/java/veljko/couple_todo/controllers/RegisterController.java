package veljko.couple_todo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import veljko.couple_todo.entities.User;
import veljko.couple_todo.services.UserService;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping
    public String registerUser(@ModelAttribute User user, Model model) {
        User alreadyExists = userService.findByUsername(user.getUserName());
        if(alreadyExists != null) {
            model.addAttribute("error", "User already exists!");
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/login";
    }
}
