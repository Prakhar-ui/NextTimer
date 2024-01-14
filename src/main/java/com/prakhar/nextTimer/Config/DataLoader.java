package com.prakhar.nextTimer.Config;

import com.prakhar.nextTimer.Entity.User;
import com.prakhar.nextTimer.Repository.TaskRepository;
import com.prakhar.nextTimer.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.prakhar.nextTimer.Entity.Task;

@Component
public class DataLoader implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Add instances to the repository here
        Task task1 = new Task(11L, "anime", "Dr Stone","One-Time", 21600L);
        Task task2 = new Task(22L, "study", "Coding","Daily", 10800L);
        Task task3 = new Task(33L, "games", "CS2","One-Time", 7200L);
        User user = new User("Prakhar", 23, "prakha8380@gmail.com",passwordEncoder.encode("123"));
        userRepository.save(user);
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

    }
}
