package com.tecnova.technical_test.infrastructure.adapter;

import com.tecnova.technical_test.domain.model.Task;
import com.tecnova.technical_test.domain.model.constant.TaskConstant;
import com.tecnova.technical_test.infrastructure.adapter.entity.TaskEntity;
import com.tecnova.technical_test.infrastructure.adapter.entity.TaskStatusEntity;
import com.tecnova.technical_test.infrastructure.adapter.entity.UserEntity;
import com.tecnova.technical_test.infrastructure.adapter.exceptions.TaskException;
import com.tecnova.technical_test.infrastructure.adapter.exceptions.TaskStatusException;
import com.tecnova.technical_test.infrastructure.adapter.exceptions.UserException;
import com.tecnova.technical_test.infrastructure.adapter.mapper.TaskDboMapper;
import com.tecnova.technical_test.infrastructure.adapter.repository.ITaskRepository;
import com.tecnova.technical_test.infrastructure.adapter.repository.ITaskStatusRepository;
import com.tecnova.technical_test.infrastructure.adapter.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class TaskSpringJpaAdapterTest {

    @Mock
    private ITaskRepository taskRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ITaskStatusRepository taskStatusRepository;

    @Mock
    private TaskDboMapper taskDboMapper;

    @InjectMocks
    private TaskSpringJpaAdapter taskAdapter;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_whenUserExists_shouldReturnUser() {
        Long id = 1L;
        TaskEntity task = new TaskEntity();
        Task domainTask = new Task();

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskDboMapper.toDomain(task)).thenReturn(domainTask);

        Task result = taskAdapter.getTaskById(id);

        assertNotNull(result);
        verify(taskRepository).findById(id);
        verify(taskDboMapper).toDomain(task);
    }

    @Test
    void getById_whenUserNotFound_shouldThrowException() {
        Long id = 2L;

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        TaskException exception = assertThrows(TaskException.class, () ->
                taskAdapter.getTaskById(id));
        assertTrue(exception.getErrorCode().is4xxClientError());

        verify(taskRepository).findById(id);
    }

    @Test
    void testGetAllTaskReturnsList() {
        TaskEntity entity = new TaskEntity();
        entity.setId(1L);
        entity.setTitle("Test Task");

        Task domain = new Task();
        domain.setId(1L);
        domain.setTitle("Test Task");

        List<TaskEntity> entityList = List.of(entity);

        when(taskRepository.findAll()).thenReturn(entityList);
        when(taskDboMapper.toDomain(entity)).thenReturn(domain);

        List<Task> result = taskAdapter.getAllTask();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Task", result.get(0).getTitle());

        verify(taskRepository).findAll();
        verify(taskDboMapper).toDomain(entity);
    }

    @Test
    void testGetAllTaskThrowsExceptionWhenEmpty() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        TaskException exception = assertThrows(TaskException.class, () -> {
            taskAdapter.getAllTask();
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getErrorCode());

        verify(taskRepository).findAll();
        verifyNoInteractions(taskDboMapper);
    }

    @Test
    void testCreateTaskSuccess() {
        Task request = new Task();
        request.setIdUser(1L);
        request.setIdTaskStatus(2L);
        request.setTitle("Test Task");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Juan");

        TaskStatusEntity statusEntity = new TaskStatusEntity();
        statusEntity.setId(2L);
        statusEntity.setDescription("Pending");

        TaskEntity entityToSave = new TaskEntity();
        entityToSave.setTitle("Test Task");

        TaskEntity savedEntity = new TaskEntity();
        savedEntity.setId(100L);
        savedEntity.setTitle("Test Task");
        savedEntity.setUserEntity(userEntity);
        savedEntity.setTaskStatusEntity(statusEntity);

        Task expected = new Task();
        expected.setId(100L);
        expected.setTitle("Test Task");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(taskStatusRepository.findById(2L)).thenReturn(Optional.of(statusEntity));
        when(taskDboMapper.toDbo(request)).thenReturn(entityToSave);
        when(taskRepository.save(entityToSave)).thenReturn(savedEntity);
        when(taskDboMapper.toDomain(savedEntity)).thenReturn(expected);

        Task result = taskAdapter.createTask(request);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("Test Task", result.getTitle());

        verify(userRepository).findById(1L);
        verify(taskStatusRepository).findById(2L);
        verify(taskRepository).save(entityToSave);
        verify(taskDboMapper).toDbo(request);
        verify(taskDboMapper).toDomain(savedEntity);
    }

    @Test
    void testCreateTaskThrowsUserExceptionWhenUserNotFound() {
        Task request = new Task();
        request.setIdUser(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> {
            taskAdapter.createTask(request);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getErrorCode());

        verify(userRepository).findById(1L);
        verifyNoInteractions(taskStatusRepository, taskRepository, taskDboMapper);
    }

    @Test
    void testCreateTaskThrowsTaskStatusExceptionWhenStatusNotFound() {
        Task request = new Task();
        request.setIdUser(1L);
        request.setIdTaskStatus(2L);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(taskStatusRepository.findById(2L)).thenReturn(Optional.empty());

        TaskStatusException exception = assertThrows(TaskStatusException.class, () -> {
            taskAdapter.createTask(request);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getErrorCode());

        verify(userRepository).findById(1L);
        verify(taskStatusRepository).findById(2L);
        verifyNoInteractions(taskRepository, taskDboMapper);
    }

    @Test
    void testUpdateTask_Success() {
        // Arrange
        Long idTask = 1L;

        // Request con todos los campos
        Task request = new Task();
        request.setTitle("Nueva tarea");
        request.setDescription("Descripción actualizada");
        request.setLimitDate(LocalDate.now().plusDays(3));
        request.setIdUser(10L);
        request.setIdTaskStatus(100L);

        // Entidades necesarias
        TaskEntity existingTask = new TaskEntity();
        existingTask.setId(idTask);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(10L);
        userEntity.setName("Juan");

        TaskStatusEntity statusEntity = new TaskStatusEntity();
        statusEntity.setId(100L);
        statusEntity.setDescription("COMPLETADO");

        // Optional mocks
        when(taskRepository.findById(idTask)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(10L)).thenReturn(Optional.of(userEntity));
        when(taskStatusRepository.findById(100L)).thenReturn(Optional.of(statusEntity));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Task expectedTask = new Task(); // objeto final mapeado
        when(taskDboMapper.toDomain(existingTask)).thenReturn(expectedTask);

        // Act
        Task result = taskAdapter.updateTask(request, idTask);

        // Assert
        assertNotNull(result);
        verify(taskRepository).findById(idTask);
        verify(userRepository).findById(10L);
        verify(taskStatusRepository).findById(100L);
        verify(taskRepository).save(existingTask);
        verify(taskDboMapper).toDomain(existingTask);
    }

    @Test
    void testUpdateTask_TaskNotFound_ShouldThrowException() {
        // Arrange
        Long idTask = 1L;
        Task request = new Task();
        request.setIdUser(10L);

        when(taskRepository.findById(idTask)).thenReturn(Optional.empty());

        // Act & Assert
        TaskException exception = assertThrows(TaskException.class, () -> {
            taskAdapter.updateTask(request, idTask);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getErrorCode());
        verify(taskRepository).findById(idTask);
    }

    @Test
    void testUpdateTask_UserNotFound_ShouldThrowException() {
        // Arrange
        Long idTask = 1L;
        Task request = new Task();
        request.setIdUser(10L);

        TaskEntity existingTask = new TaskEntity();
        existingTask.setId(idTask);

        when(taskRepository.findById(idTask)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        // Act & Assert
        UserException exception = assertThrows(UserException.class, () -> {
            taskAdapter.updateTask(request, idTask);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getErrorCode());
        verify(userRepository).findById(10L);
    }

    @Test
    void testUpdateTask_TaskStatusNotFound_ShouldThrowException() {
        // Arrange
        Long idTask = 1L;
        Task request = new Task();
        request.setIdUser(10L);
        request.setIdTaskStatus(200L);

        TaskEntity existingTask = new TaskEntity();
        existingTask.setId(idTask);

        when(taskRepository.findById(idTask)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(10L)).thenReturn(Optional.of(new UserEntity()));
        when(taskStatusRepository.findById(200L)).thenReturn(Optional.empty());

        // Act & Assert
        TaskStatusException exception = assertThrows(TaskStatusException.class, () -> {
            taskAdapter.updateTask(request, idTask);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getErrorCode());
        verify(taskStatusRepository).findById(200L);
    }

    @Test
    void deleteTaskById_ShouldCallRepositoryDeleteById() {
        Long taskId = 1L;
        taskAdapter.deleteTaskById(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }
}
