package veljko.couple_todo.utils;

import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import veljko.couple_todo.entities.User;
import veljko.couple_todo.services.UserService;

@Component
public class CurrentUser {

    private final UserService userService;

    // Constructor injection is preferred over @Autowired fields
    public CurrentUser(UserService userService) {
        this.userService = userService;
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return userService.findByUsername(auth.getName());
    }
}