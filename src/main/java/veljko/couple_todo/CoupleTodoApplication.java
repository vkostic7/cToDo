package veljko.couple_todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;
import veljko.couple_todo.entities.Task;
import veljko.couple_todo.entities.User;
import veljko.couple_todo.repos.TaskRepo;
import veljko.couple_todo.repos.UserRepo;
import veljko.couple_todo.services.UserService;

import java.util.List;

@SpringBootApplication
public class CoupleTodoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CoupleTodoApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {

	}
}