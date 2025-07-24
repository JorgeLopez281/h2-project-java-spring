package com.tecnova.technical_test.infrastructure.adapter.mapper;

import com.tecnova.technical_test.domain.model.Task;
import com.tecnova.technical_test.domain.model.dto.TaskDto;
import com.tecnova.technical_test.infrastructure.adapter.entity.TaskEntity;
import com.tecnova.technical_test.infrastructure.adapter.entity.TaskStatusEntity;
import com.tecnova.technical_test.infrastructure.adapter.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TaskDboMapperTest {

    private final TaskDboMapper mapper = Mappers.getMapper(TaskDboMapper.class);

    @Test
    void shouldMapTaskEntityToDomain() {
        TaskEntity entity = getTaskEntity();

        Task task = mapper.toDomain(entity);

        assertNotNull(task);
        assertEquals(1L, task.getId());
        assertEquals("Do homework", task.getTitle());
        assertEquals("Math homework", task.getDescription());
        assertEquals(LocalDate.of(2025, 7, 20), task.getLimitDate());
        assertEquals(10, task.getIdUser());
        assertEquals("Carlos", task.getNameUser());
        assertEquals(5, task.getIdTaskStatus());
        assertEquals("In Progress", task.getDescriptionTaskStatus());
    }

    @Test
    void shouldMapTaskEntityToTask() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Linda");

        TaskStatusEntity taskStatusEntity = new TaskStatusEntity();
        taskStatusEntity.setId(2L);
        taskStatusEntity.setDescription("In progress");

        TaskEntity entity = new TaskEntity();
        entity.setId(100L);
        entity.setTitle("Write tests");
        entity.setDescription("Cover all mappers");
        entity.setLimitDate(LocalDate.of(2025, 7, 11));
        entity.setUserEntity(userEntity);
        entity.setTaskStatusEntity(taskStatusEntity);

        Task task = mapper.toDomain(entity);

        assertNotNull(task);
        assertEquals(100L, task.getId());
        assertEquals("Write tests", task.getTitle());
        assertEquals("Cover all mappers", task.getDescription());
        assertEquals(LocalDate.of(2025, 7, 11), task.getLimitDate());
        assertEquals(1L, task.getIdUser());
        assertEquals("Linda", task.getNameUser());
        assertEquals(2L, task.getIdTaskStatus());
        assertEquals("In progress", task.getDescriptionTaskStatus());
    }

    @Test
    void shouldMapTaskEntityToDbo() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Title");
        task.setDescription("Description Example");
        task.setLimitDate(LocalDate.of(2025, 7, 11));
        task.setIdUser(1L);
        task.setIdTaskStatus(1L);

        TaskEntity entity = mapper.toDbo(task);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Title", entity.getTitle());
        assertEquals("Description Example", entity.getDescription());
        assertEquals(LocalDate.of(2025, 7, 11), entity.getLimitDate());
        assertEquals(1L, entity.getUserEntity().getId());
        assertEquals(1L, entity.getTaskStatusEntity().getId());

    }

    @Test
    void toDomain_shouldHandleNullEntityObject() {
        Task task = mapper.toDomain(null);
        assertNull(task);
    }

    @Test
    void toDomain_shouldHandleNullDomainObject() {
        TaskEntity task = mapper.toDbo(null);
        assertNull(task);
    }

    @Test
    void shouldMapTaskEntityToDbo_WithAllNull() {
        Task task = new Task();
        task.setId(null);
        task.setTitle(null);
        task.setDescription(null);
        task.setLimitDate(null);
        task.setIdUser(null);
        task.setIdTaskStatus(null);

        TaskEntity entity = mapper.toDbo(task);

        assertNull(entity.getId());
        assertNull(entity.getTitle());
        assertNull(entity.getDescription());
        assertNull(entity.getLimitDate());
        assertNull(entity.getUserEntity().getId());
        assertNull(entity.getTaskStatusEntity().getId());
    }

    @Test
    void shouldMapTaskEntityToDomain_WithAllNull() {
        TaskEntity task = new TaskEntity();
        task.setId(null);
        task.setTitle(null);
        task.setDescription(null);
        task.setLimitDate(null);
        task.setUserEntity(null);
        task.setTaskStatusEntity(null);;

        Task entity = mapper.toDomain(task);

        assertNull(entity.getId());
        assertNull(entity.getTitle());
        assertNull(entity.getDescription());
        assertNull(entity.getLimitDate());
        assertNull(entity.getIdUser());
        assertNull(entity.getIdTaskStatus());
    }

    private static TaskEntity getTaskEntity() {
        UserEntity user = new UserEntity();
        user.setId(10L);
        user.setName("Carlos");

        TaskStatusEntity status = new TaskStatusEntity();
        status.setId(5L);
        status.setDescription("In Progress");

        TaskEntity entity = new TaskEntity();
        entity.setId(1L);
        entity.setTitle("Do homework");
        entity.setDescription("Math homework");
        entity.setLimitDate(LocalDate.of(2025, 7, 20));
        entity.setUserEntity(user);
        entity.setTaskStatusEntity(status);
        return entity;
    }

    @Test
    void toDomain_shouldHandleNullUserEntity() {

        List<TaskEntity> taskList = new ArrayList<>();

        TaskEntity task = new TaskEntity();
        task.setId(1L);
        task.setTitle("Some task");
        task.setDescription("Some description");
        task.setLimitDate(LocalDate.now());
        task.setUserEntity(null); // <- Aquí el foco
        task.setTaskStatusEntity(new TaskStatusEntity());

        Task result = mapper.toDomain(task);

        assertNull(result.getIdUser());
        assertNull(result.getNameUser());
    }

    @Test
    void toDomain_shouldHandleNullUserEntityFields() {
        UserEntity user = new UserEntity();
        user.setId(null);
        user.setName(null);

        TaskEntity task = new TaskEntity();
        task.setUserEntity(user);
        task.setTaskStatusEntity(new TaskStatusEntity());

        Task result = mapper.toDomain(task);

        assertNull(result.getIdUser());
        assertNull(result.getNameUser());
    }

    @Test
    void toDomain_shouldHandleNullTaskStatusEntity() {
        TaskEntity task = new TaskEntity();
        task.setId(1L);
        task.setTitle("Task");
        task.setDescription("Desc");
        task.setLimitDate(LocalDate.now());
        task.setUserEntity(new UserEntity());
        task.setTaskStatusEntity(null); // <- foco

        Task result = mapper.toDomain(task);

        assertNull(result.getIdTaskStatus());
        assertNull(result.getDescriptionTaskStatus());
    }

    @Test
    void toDomain_shouldHandleNullTaskStatusEntityFields() {
        TaskStatusEntity status = new TaskStatusEntity();
        status.setId(null);
        status.setDescription(null);

        TaskEntity task = new TaskEntity();
        task.setUserEntity(new UserEntity());
        task.setTaskStatusEntity(status);

        Task result = mapper.toDomain(task);

        assertNull(result.getIdTaskStatus());
        assertNull(result.getDescriptionTaskStatus());
    }




}
