package com.prakhar.nextTimer.Service;

import com.prakhar.nextTimer.DTO.TimerDTO;
import com.prakhar.nextTimer.Entity.Task;
import com.prakhar.nextTimer.Repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("Test Get All Tasks")
    public void testGetAllTasks() {

        List<Task> taskList = Arrays.asList(
                new Task(1L, "NAME1", "DESC1","TIMER1", 7200L),
                new Task(2L, "NAME2", "DESC2","TIMER2", 7300L),
                new Task(3L, "NAME3", "DESC3","TIMER3", 7400L)
        );

        when(taskRepository.findAll()).thenReturn(taskList);

        // Call the method
        List<Task> result = taskService.getAllTasks();

        // Assert the result
        assertEquals(taskList, result);
    }

    @Test
    @DisplayName("Test Get Task By Id")
    public void testGetTaskById() {

        Task taskById = new Task(1L, "NAME1", "DESC1","TIMER1", 7200L);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(taskById));

        // Call the method
        Optional<Task> result = taskService.getTaskById(1L);

        // Assert the result
        assertEquals(Optional.of(taskById), result);
    }

    @Test
    @DisplayName("Test Create New Task")
    public void testCreateNewTask() {

        // Capture the argument passed to taskRepository.save
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        Task task = new Task("NAME1", "DESC1","TIMER1", 7200L);

        // Call the method
        String result = taskService.createTask(task);

        // Verify that userRepository.save was called once
        verify(taskRepository, times(1)).save(taskCaptor.capture());

        // Retrieve the saved user from the captured argument
        Task savedTask = taskCaptor.getValue();

        assertEquals(task.getName(), savedTask.getName());
        assertEquals(task.getDescription(), savedTask.getDescription());
        assertEquals(task.getTimerType(), savedTask.getTimerType());
        assertEquals(task.getSeconds(), savedTask.getSeconds());
        assertEquals(task.toString(), result);

    }

    @Test
    @DisplayName("Test Edit Task")
    public void testEditTask() {
        Task taskById = new Task(1L, "NAME1", "DESC1","TIMER1", 7200L);

        // Capture the argument passed to passwordEncoder.encode
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        when(taskRepository.findById(idCaptor.capture())).thenReturn(Optional.of(taskById));
        when(taskRepository.save(taskCaptor.capture())).thenReturn(null);
        // Call the method
        taskService.editTask(new Task(1L, "NAME2", "DESC2","TIMER2", 7500L));

        // Assert the properties of the saved user
        assertEquals(1L, idCaptor.getValue());

        // Retrieve the saved user from the captured argument
        Task savedTask = taskCaptor.getValue();

        assertEquals("NAME2", savedTask.getName());
        assertEquals("DESC2", savedTask.getDescription());
        assertEquals("TIMER2", savedTask.getTimerType());
        assertEquals(7500L, savedTask.getSeconds());
    }

    @Test
    @DisplayName("Test Delete Task")
    void testDeleteTaskById() {
        // Arrange
        Task taskById = new Task(1L, "NAME1", "DESC1","TIMER1", 7200L);

        // Capture the argument passed to passwordEncoder.encode
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> idCaptor2 = ArgumentCaptor.forClass(Long.class);

        when(taskRepository.findById(idCaptor.capture())).thenReturn(Optional.of(taskById));
        doNothing().when(taskRepository).deleteById(idCaptor2.capture());

        // Act
        taskService.deleteTaskById(1L);

        // Assert
        verify(taskRepository, times(1)).findById(anyLong());
        verify(taskRepository, times(1)).deleteById(anyLong());

        assertEquals(1L,idCaptor.getValue() );
        assertEquals(1L,idCaptor2.getValue());
    }

    @Test
    @DisplayName("Test Delete All Tasks")
    void testDeleteAllTasks() {

        taskService.deleteAllTasks();

        verify(taskRepository, times(1)).deleteAll();


    }

    @Test
    @DisplayName("Test Post Seconds")
    void testPostSeconds() {
        Task taskById = new Task(1L, "NAME1", "DESC1","TIMER1", 7200L);

        // Capture the argument passed to passwordEncoder.encode
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        when(taskRepository.findById(idCaptor.capture())).thenReturn(Optional.of(taskById));
        when(taskRepository.save(taskCaptor.capture())).thenReturn(
                new Task(1L, "NAME1", "DESC1","TIMER1", 7600L));


        taskService.postSeconds(new TimerDTO("1",7600L));

        Task savedTask = taskCaptor.getValue();

        verify(taskRepository, times(1)).save(savedTask);

        assertEquals(1L, idCaptor.getValue());
        assertEquals("NAME1", savedTask.getName());
        assertEquals("DESC1", savedTask.getDescription());
        assertEquals("TIMER1", savedTask.getTimerType());
        assertEquals(7600L, savedTask.getSeconds());


    }
}
