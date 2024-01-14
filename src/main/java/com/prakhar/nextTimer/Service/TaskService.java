package com.prakhar.nextTimer.Service;

import com.prakhar.nextTimer.DTO.TimerDTO;
import com.prakhar.nextTimer.Entity.Task;
import com.prakhar.nextTimer.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;



    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);

    }


    public String createTask(Task newTask) {
        Task task = new Task(newTask.getName(), newTask.getDescription(), newTask.getTimerType(),newTask.getSeconds());
        taskRepository.save(task);
        return task.toString();
    }

    public void editTask(Task task) {
        Optional<Task> optionalTask = taskRepository.findById(task.getId());
        if (optionalTask.isPresent()) {
            Task new_task = optionalTask.get();
            new_task.setName(task.getName());
            new_task.setDescription(task.getDescription());
            new_task.setTimerType(task.getTimerType());
            new_task.setSeconds(task.getSeconds());
            taskRepository.save(new_task);
        }
    }


    public void deleteTaskById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            taskRepository.deleteById(id);
        }

    }

    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }

    public void postSeconds(TimerDTO timerDTO) {
        Optional<Task> optionalTask = taskRepository.findById(Long.valueOf(timerDTO.getId()));
        if (optionalTask.isPresent()) {
            Task new_task = optionalTask.get();
            new_task.setSeconds(timerDTO.getSeconds());
            taskRepository.save(new_task);
        } else {
            throw new RuntimeException("User not Found!");
        }
    }


}
