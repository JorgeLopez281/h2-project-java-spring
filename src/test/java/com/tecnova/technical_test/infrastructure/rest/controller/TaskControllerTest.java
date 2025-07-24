package com.tecnova.technical_test.infrastructure.rest.controller;

import com.tecnova.technical_test.application.usecase.ITaskService;
import com.tecnova.technical_test.domain.model.dto.TaskDto;
import com.tecnova.technical_test.domain.model.dto.request.TaskCreationRequest;
import com.tecnova.technical_test.domain.model.dto.request.TaskUpdateRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskControllerTest {

    @Mock
    private ITaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getByIdTask_ShouldReturnUser() {
        Long taskId = 1L;
        LocalDate limitDate = LocalDate.of(2023, 5, 15);
        TaskDto taskDto = new TaskDto(
                taskId,
                "Task Example",
                "Task Description",
                limitDate,
                1L,
                "jlopez",
                1L,
                "2"
        );

        Mockito.when(taskService.getTaskById(taskId)).thenReturn(taskDto);

        ResponseEntity<TaskDto> response = taskController.getByIdTask(taskId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(taskDto, response.getBody());
        verify(taskService, times(1)).getTaskById(taskId);
    }

    @Test
    void getAllTask_ShouldReturnListOfTask() {
        LocalDate limitDate = LocalDate.of(2023, 5, 15);

        List<TaskDto> task = List.of(
                new TaskDto(1L,
                        "Task Example",
                        "Task Description",
                        limitDate,
                        1L,
                        "jlopez",
                        1L,
                        "2"),
                new TaskDto(2L,
                        "Task Example",
                        "Task Description",
                        limitDate,
                        1L,
                        "jlopez",
                        1L,
                        "2")
        );

        when(taskService.getAllTask()).thenReturn(task);

        ResponseEntity<List<TaskDto>> response = taskController.getAllTask();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(task, response.getBody());
    }

    @Test
    void createTask_ShouldReturnCreatedTask() {
        LocalDate limitDate = LocalDate.of(2023, 5, 15);

        TaskCreationRequest creationTaskRequest = new TaskCreationRequest("Task Example",
                "Task Description",
                limitDate,
                1,
                1);

        TaskDto taskDto = new TaskDto(2L,
                "Task Example",
                "Task Description",
                limitDate,
                1L,
                "jlopez",
                1L,
                "2");

        when(taskService.createTask(creationTaskRequest)).thenReturn(taskDto);

        ResponseEntity<TaskDto> response = taskController.createTask(creationTaskRequest);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(taskDto, response.getBody());
    }

    @Test
    void editTask_ShouldReturnUpdatedUser() {
        Long taskId = 1L;
        LocalDate limitDate = LocalDate.of(2023, 5, 15);

        TaskUpdateRequest request = new TaskUpdateRequest("Task Example",
                "Task Description",
                limitDate,
                1,
                1);

        TaskDto taskDto = new TaskDto(2L,
                "Task Example",
                "Task Description",
                limitDate,
                1L,
                "jlopez",
                1L,
                "2");

        when(taskService.updateTask(request, taskId)).thenReturn(taskDto);

        ResponseEntity<TaskDto> response = taskController.editTask(request, taskId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(taskDto, response.getBody());
    }

    @Test
    void deleteTask_ShouldReturnNoContent() {
        Long taskId = 1L;

        ResponseEntity<Void> response = taskController.deleteTask(taskId);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskService, times(1)).deleteTaskById(taskId);
    }

}
