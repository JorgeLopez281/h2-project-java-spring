package com.tecnova.technical_test.application.service;

import com.tecnova.technical_test.application.mapper.TaskDtoMapper;
import com.tecnova.technical_test.domain.model.Task;
import com.tecnova.technical_test.domain.model.dto.TaskDto;
import com.tecnova.technical_test.domain.model.dto.request.TaskCreationRequest;
import com.tecnova.technical_test.domain.model.dto.request.TaskUpdateRequest;
import com.tecnova.technical_test.domain.port.ITaskPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    @Mock
    private ITaskPort taskPort;

    @Mock
    private TaskDtoMapper taskDtoMapper;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTaskById_ReturnsTaskDto() {
        Long taskId = 1L;

        Task domainTask = new Task();
        domainTask.setId(taskId);
        domainTask.setTitle("Task title");

        TaskDto expectedDto = new TaskDto();
        expectedDto.setId(taskId);
        expectedDto.setTitle("Task title");

        when(taskPort.getTaskById(taskId)).thenReturn(domainTask);
        when(taskDtoMapper.toDto(domainTask)).thenReturn(expectedDto);

        TaskDto result = taskService.getTaskById(taskId);

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getTitle(), result.getTitle());

        verify(taskPort, times(1)).getTaskById(taskId);
        verify(taskDtoMapper, times(1)).toDto(domainTask);
    }

    @Test
    void testGetAllTask() {
        Task task1 = new Task();
        task1.setId(1L);
        Task task2 = new Task();
        task2.setId(2L);

        TaskDto dto1 = new TaskDto();
        dto1.setId(1L);
        TaskDto dto2 = new TaskDto();
        dto2.setId(2L);

        when(taskPort.getAllTask()).thenReturn(List.of(task1, task2));
        when(taskDtoMapper.toDto(task1)).thenReturn(dto1);
        when(taskDtoMapper.toDto(task2)).thenReturn(dto2);

        List<TaskDto> result = taskService.getAllTask();

        assertEquals(2, result.size());
        verify(taskPort).getAllTask();
    }

    @Test
    void testCreateTask() {
        TaskCreationRequest request = new TaskCreationRequest();
        request.setTitle("Title");

        Task domainTask = new Task();
        domainTask.setTitle("Title");

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("Title");

        TaskDto dto = new TaskDto();
        dto.setId(1L);
        dto.setTitle("Title");

        when(taskDtoMapper.toDomain(request)).thenReturn(domainTask);
        when(taskPort.createTask(domainTask)).thenReturn(savedTask);
        when(taskDtoMapper.toDto(savedTask)).thenReturn(dto);


        TaskDto result = taskService.createTask(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(taskDtoMapper).toDomain(request);
        verify(taskPort).createTask(domainTask);
        verify(taskDtoMapper).toDto(savedTask);
    }

    @Test
    void testUpdateTask() {
        Long taskId = 1L;

        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Title");

        Task domainTask = new Task();
        domainTask.setTitle("Title");

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("Title");

        TaskDto dto = new TaskDto();
        dto.setId(1L);
        dto.setTitle("Title");

        when(taskDtoMapper.toDomainUpdate(request)).thenReturn(domainTask);
        when(taskPort.updateTask(domainTask, taskId)).thenReturn(savedTask);
        when(taskDtoMapper.toDto(savedTask)).thenReturn(dto);


        TaskDto result = taskService.updateTask(request, taskId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(taskDtoMapper).toDomainUpdate(request);
        verify(taskPort).updateTask(domainTask, taskId);
        verify(taskDtoMapper).toDto(savedTask);
    }

    @Test
    void testDeleteTaskById() {
        Long taskId = 1L;

        doNothing().when(taskPort).deleteTaskById(taskId);
        taskService.deleteTaskById(taskId);
        verify(taskPort, times(1)).deleteTaskById(taskId);
    }
}
