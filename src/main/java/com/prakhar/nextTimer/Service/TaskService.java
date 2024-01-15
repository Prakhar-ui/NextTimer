package com.prakhar.nextTimer.Service;

import com.prakhar.nextTimer.DTO.TimerDTO;
import com.prakhar.nextTimer.Entity.Task;
import com.prakhar.nextTimer.Repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);


    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        try {
            List<Task> tasks = taskRepository.findAll();
            logger.info("Retrieved all tasks successfully");
            return tasks;
        } catch (Exception e) {
            logger.error("Error getting all tasks", e);
            throw e;
        }
    }

    public Optional<Task> getTaskById(Long id) {
        try {
            Optional<Task> task = taskRepository.findById(id);
            logger.info("Retrieved task by ID {} successfully", id);
            return task;
        } catch (Exception e) {
            logger.error("Error getting task by ID: {}", id, e);
            throw e;
        }
    }

    public String createTask(Task newTask) {
        try {
            Task task = new Task(newTask.getName(), newTask.getDescription(), newTask.getTimerType(), newTask.getSeconds());
            taskRepository.save(task);
            logger.info("Task created successfully: {}", task);
            return task.toString();
        } catch (Exception e) {
            logger.error("Error creating task", e);
            throw e;
        }
    }

    public void editTask(Task task) {
        try {
            Optional<Task> optionalTask = taskRepository.findById(task.getId());
            if (optionalTask.isPresent()) {
                Task newTask = optionalTask.get();
                newTask.setName(task.getName());
                newTask.setDescription(task.getDescription());
                newTask.setTimerType(task.getTimerType());
                newTask.setSeconds(task.getSeconds());
                taskRepository.save(newTask);
                logger.info("Task edited successfully: {}", newTask);
            }
        } catch (Exception e) {
            logger.error("Error editing task", e);
            throw e;
        }
    }

    public void deleteTaskById(Long id) {
        try {
            Optional<Task> task = taskRepository.findById(id);
            if (task.isPresent()) {
                taskRepository.deleteById(id);
                logger.info("Task deleted successfully with ID: {}", id);
            }
        } catch (Exception e) {
            logger.error("Error deleting task by ID: {}", id, e);
            throw e;
        }
    }

    public void deleteAllTasks() {
        try {
            taskRepository.deleteAll();
            logger.info("All tasks deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting all tasks", e);
            throw e;
        }
    }

    public void postSeconds(TimerDTO timerDTO) {
        try {
            Optional<Task> optionalTask = taskRepository.findById(Long.valueOf(timerDTO.getId()));
            if (optionalTask.isPresent()) {
                Task newTask = optionalTask.get();
                newTask.setSeconds(timerDTO.getSeconds());
                taskRepository.save(newTask);
                logger.info("Seconds posted successfully for task with ID: {}", timerDTO.getId());
            } else {
                throw new RuntimeException("Task not found with ID: " + timerDTO.getId());
            }
        } catch (Exception e) {
            logger.error("Error posting seconds for task with ID: {}", timerDTO.getId(), e);
            throw e;
        }
    }
}