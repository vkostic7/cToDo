package veljko.couple_todo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MedjustranaController {

    @GetMapping("/choose")
    public String getMedjustrana() {
        return "medjustrana";
    }
}
