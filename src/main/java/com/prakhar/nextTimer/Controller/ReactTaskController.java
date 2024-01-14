package com.prakhar.nextTimer.Controller;

import com.prakhar.nextTimer.DTO.TimerDTO;
import com.prakhar.nextTimer.Entity.Task;
import com.prakhar.nextTimer.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class ReactTaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/api/myTasks")
    public List<Task> getmyTasks(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        return tasks;
    }

    @PostMapping("/api/postSeconds")
    public void postSeconds(@RequestBody TimerDTO timerDTO) {
        taskService.postSeconds(timerDTO);
    }


    @GetMapping("/api/getTask/{id}")
    public ResponseEntity<Optional<Task>> getTask(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }


    @DeleteMapping("/api/deleteTask/{id}")
    public ResponseEntity<String> deleteTaskById(@PathVariable Long id) {
        System.out.println("id " + id);
        taskService.deleteTaskById(id);
        return ResponseEntity.ok("Task deleted successfully");
    }

    @DeleteMapping("/api/deleteAllTasks")
    public ResponseEntity<String> deleteAllTasks() {
        taskService.deleteAllTasks();
        return ResponseEntity.ok("All Tasks deleted successfully");
    }

    @PostMapping("/api/newTask")
    public void createTask(@RequestBody Task task) {
        taskService.createTask(task);
    }

    @PostMapping("/api/editTask")
    public void editTask(@RequestBody Task task) {
        taskService.editTask(task);
    }
}
