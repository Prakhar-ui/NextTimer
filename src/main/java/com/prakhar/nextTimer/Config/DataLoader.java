package com.prakhar.nextTimer.Config;

import com.prakhar.nextTimer.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.prakhar.nextTimer.Entity.Task;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void run(String... args) throws Exception {
        // Add instances to the repository here
        Task task1 = new Task(11L, "anime", "Dr Stone","One-Time", 21600L, 3, true);
        Task task2 = new Task(22L, "study", "Coding","Daily", 10800L, 1, true);
        Task task3 = new Task(33L, "games", "CS2","One-Time", 7200L, 2, true);
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

    }
}
