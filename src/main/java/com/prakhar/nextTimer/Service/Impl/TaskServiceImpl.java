package com.prakhar.nextTimer.Service.Impl;

import com.prakhar.nextTimer.DTO.EditTaskDTO;
import com.prakhar.nextTimer.DTO.TaskDTO;
import com.prakhar.nextTimer.DTO.TimerDTO;
import com.prakhar.nextTimer.Entity.Task;
import com.prakhar.nextTimer.Exception.TaskNotFoundException;
import com.prakhar.nextTimer.Repository.TaskRepository;
import com.prakhar.nextTimer.Service.TaskService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);


    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        try {
            List<Task> tasks = taskRepository.findAll();
            logger.info("Retrieved all tasks from database successfully");
            return tasks;
        } catch (Exception e) {
            logger.error("Error getting all tasks", e);
            throw e;
        }
    }

    public Task getTaskById(Long id) {
        try {
            Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
            logger.info("Retrieved task by ID {} from database successfully", id);
            return task;

        } catch (Exception e) {
            logger.error("Error getting task by ID: {}", id, e);
            throw e;
        } catch (TaskNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTask(TaskDTO newTask) {
        try {
            Task task = new Task(newTask.name(), newTask.description(), newTask.timerType(), newTask.seconds());
            taskRepository.save(task);
            logger.info("Task created successfully: {}", task);
        } catch (Exception e) {
            logger.error("Error creating task", e);
            throw e;
        }
    }

    public void editTask(EditTaskDTO task) {
        try {
            Optional<Task> optionalTask = taskRepository.findById(task.id());
            if (optionalTask.isPresent()) {
                Task newTask = optionalTask.get();
                newTask.setName(task.name());
                newTask.setDescription(task.description());
                newTask.setTimerType(task.timerType());
                newTask.setSeconds(task.seconds());
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
        System.out.println(timerDTO);
        try {
            Optional<Task> optionalTask = taskRepository.findById(timerDTO.taskId());
            if (optionalTask.isPresent()) {
                Task newTask = optionalTask.get();
                newTask.setSeconds(timerDTO.seconds());
                taskRepository.save(newTask);
                logger.info("Seconds posted successfully for task with ID: {}", timerDTO.taskId());
            } else {
                throw new RuntimeException("Task not found with ID: " + timerDTO.taskId());
            }
        } catch (Exception e) {
            logger.error("Error posting seconds for task with ID: {}", timerDTO.taskId(), e);
            throw e;
        }
    }
}