package com.prakhar.nextTimer.Service;

import com.prakhar.nextTimer.DTO.EditTaskDTO;
import com.prakhar.nextTimer.DTO.TaskDTO;
import com.prakhar.nextTimer.DTO.TimerDTO;
import com.prakhar.nextTimer.Entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();

    Task getTaskById(Long id);

    void createTask(TaskDTO taskDTO);

    void editTask(EditTaskDTO taskDTO);

    void deleteTaskById(Long id);

    void deleteAllTasks();

    void postSeconds(TimerDTO timerDTO);
}