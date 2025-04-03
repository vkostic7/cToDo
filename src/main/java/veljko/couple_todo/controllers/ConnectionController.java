package veljko.couple_todo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import veljko.couple_todo.services.ConnectionService;
import veljko.couple_todo.services.UserService;

@Controller
@RequestMapping("/connect")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @GetMapping("/generate")
    public String showGenerateForm() {
        return "generate-code";
    }

    @PostMapping("/generate")
    public String generateCode(Model model) {
        String code = connectionService.generateCode();
        model.addAttribute("code", code);
        return "generate-code";
    }

    @GetMapping("/join")
    public String showJoinForm() {
        return "join-partner";
    }

    @PostMapping("/join")
    public String joinPartner(@RequestParam String code) {
        connectionService.connectUsers(code);
        return "redirect:/tasks";
    }
}
