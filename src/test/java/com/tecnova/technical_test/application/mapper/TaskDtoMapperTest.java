package com.tecnova.technical_test.application.mapper;

import com.tecnova.technical_test.domain.model.Task;
import com.tecnova.technical_test.domain.model.dto.TaskDto;
import com.tecnova.technical_test.domain.model.dto.request.TaskCreationRequest;
import com.tecnova.technical_test.domain.model.dto.request.TaskUpdateRequest;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskDtoMapperTest {

    private final TaskDtoMapper taskDtoMapper = Mappers.getMapper(TaskDtoMapper.class);

    @Test
    void toDto_shouldMapAllFieldsCorrectly() {
        Task domainTask = new Task();
        domainTask.setId(1L);
        domainTask.setTitle("Buy groceries");
        domainTask.setDescription("Milk, eggs, bread, cheese");
        domainTask.setLimitDate(LocalDate.of(2025, 7, 20));
        domainTask.setIdUser(101L);
        domainTask.setNameUser("John Doe");
        domainTask.setIdTaskStatus(1L);
        domainTask.setDescriptionTaskStatus("Pending");

        TaskDto taskDto = taskDtoMapper.toDto(domainTask);

        assertNotNull(taskDto,
                "El DTO mapeado no debería ser nulo");
        assertEquals(domainTask.getId(), taskDto.getId(),
                "El ID de la tarea debería coincidir");
        assertEquals(domainTask.getTitle(), taskDto.getTitle(),
                "El título de la tarea debería coincidir");
        assertEquals(domainTask.getDescription(), taskDto.getDescription(),
                "La descripción de la tarea debería coincidir");
        assertEquals(domainTask.getLimitDate(), taskDto.getLimitDate(),
                "La fecha límite de la tarea debería coincidir");
        assertEquals(domainTask.getIdUser(), taskDto.getIdUser(),
                "El ID de usuario debería coincidir");
        assertEquals(domainTask.getNameUser(), taskDto.getNameUser(),
                "El nombre de usuario debería coincidir");
        assertEquals(domainTask.getIdTaskStatus(), taskDto.getIdTaskStatus(),
                "El ID del estado de la tarea debería coincidir");
        assertEquals(domainTask.getDescriptionTaskStatus(), taskDto.getDescriptionTaskStatus(),
                "La descripción del estado de la tarea debería coincidir");
    }

    @Test
    void toDto_shouldHandleNullDomainObject() {
        TaskDto taskDto = taskDtoMapper.toDto(null);
        assertNull(taskDto, "Mapear un objeto de dominio nulo debería resultar en un DTO nulo");
    }

    @Test
    void toDomain_toDto_shouldMapAllFieldsCorrectly() {
        TaskCreationRequest creationTaskRequest = new TaskCreationRequest();
        creationTaskRequest.setTitle("Title");
        creationTaskRequest.setDescription("Example Description");
        creationTaskRequest.setLimitDate(LocalDate.of(2025, 7, 20));
        creationTaskRequest.setIdUser(1);
        creationTaskRequest.setIdTaskStatus(1);

        Task task = taskDtoMapper.toDomain(creationTaskRequest);
        assertEquals(task.getTitle(), creationTaskRequest.getTitle(), "El Title de la tarea debería coincidir");
        assertEquals(task.getDescription(), creationTaskRequest.getDescription(), "El Description de la tarea debería coincidir");
        assertEquals(task.getLimitDate(), creationTaskRequest.getLimitDate(), "El LimitDate de la tarea debería coincidir");
        assertEquals(task.getIdUser(), creationTaskRequest.getIdUser().longValue(), "El ID del usuario de la tarea debería coincidir");
        assertEquals(task.getIdTaskStatus(), creationTaskRequest.getIdTaskStatus().longValue(), "El ID de la tarea debería coincidir");
    }

    @Test
    void toDomain_shouldRequestIsNull() {
        Task task = taskDtoMapper.toDomain(null);
        assertNull(task,"El DTO mapeado debería ser nulo");
    }

    @Test
    void toDomainUpdate_shouldMapAllFieldsCorrectly() {
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Title");
        request.setDescription("Example Description");
        request.setLimitDate(LocalDate.of(2025, 7, 20));
        request.setIdUser(1);
        request.setIdTaskStatus(1);

        Task task = taskDtoMapper.toDomainUpdate(request);
        assertEquals(task.getTitle(), request.getTitle(), "El Title de la tarea debería coincidir");
        assertEquals(task.getDescription(), request.getDescription(), "El Description de la tarea debería coincidir");
        assertEquals(task.getLimitDate(), request.getLimitDate(), "El LimitDate de la tarea debería coincidir");
        assertEquals(task.getIdUser(), request.getIdUser().longValue(), "El ID del usuario de la tarea debería coincidir");
        assertEquals(task.getIdTaskStatus(), request.getIdTaskStatus().longValue(), "El ID de la tarea debería coincidir");
    }

    @Test
    void toDomainUpdate_shouldRequestIsNull() {
        Task task = taskDtoMapper.toDomainUpdate(null);
        assertNull(task,"El DTO mapeado debería ser nulo");
    }
}
