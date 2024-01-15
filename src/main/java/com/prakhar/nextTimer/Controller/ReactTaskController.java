package com.prakhar.nextTimer.Controller;

import com.prakhar.nextTimer.DTO.TimerDTO;
import com.prakhar.nextTimer.Entity.Task;
import com.prakhar.nextTimer.Service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin
public class ReactTaskController {

    private static final Logger logger = LoggerFactory.getLogger(ReactTaskController.class);

    @Autowired
    private TaskService taskService;

    @GetMapping("/api/myTasks")
    public ResponseEntity<List<Task>> getMyTasks(Model model) {
        try {
            List<Task> tasks = taskService.getAllTasks();
            logger.info("Retrieved all tasks successfully");
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            logger.error("Error retrieving tasks", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/api/postSeconds")
    public ResponseEntity<String> postSeconds(@RequestBody TimerDTO timerDTO) {
        try {
            taskService.postSeconds(timerDTO);
            logger.info("Posted seconds successfully");
            return ResponseEntity.ok("Seconds posted successfully");
        } catch (Exception e) {
            logger.error("Error posting seconds", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @GetMapping("/api/getTask/{id}")
    public ResponseEntity<Optional<Task>> getTask(@PathVariable Long id) {
        try {
            Optional<Task> task = taskService.getTaskById(id);
            logger.info("Retrieved task with ID {} successfully", id);
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            logger.error("Error retrieving task with ID {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/api/deleteTask/{id}")
    public ResponseEntity<String> deleteTaskById(@PathVariable Long id) {
        try {
            taskService.deleteTaskById(id);
            logger.info("Deleted task with ID {} successfully", id);
            return ResponseEntity.ok("Task deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting task with ID {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @DeleteMapping("/api/deleteAllTasks")
    public ResponseEntity<String> deleteAllTasks() {
        try {
            taskService.deleteAllTasks();
            logger.info("Deleted all tasks successfully");
            return ResponseEntity.ok("All Tasks deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting all tasks", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @PostMapping("/api/newTask")
    public ResponseEntity<String> createTask(@RequestBody Task task) {
        try {
            taskService.createTask(task);
            logger.info("Created a new task successfully");
            return ResponseEntity.ok("New task created successfully");
        } catch (Exception e) {
            logger.error("Error creating a new task", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @PostMapping("/api/editTask")
    public ResponseEntity<String> editTask(@RequestBody Task task) {
        try {
            taskService.editTask(task);
            logger.info("Edited a task successfully");
            return ResponseEntity.ok("Task edited successfully");
        } catch (Exception e) {
            logger.error("Error editing a task", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}
