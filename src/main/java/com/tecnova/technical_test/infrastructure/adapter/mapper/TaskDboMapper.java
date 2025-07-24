package com.tecnova.technical_test.infrastructure.adapter.mapper;

import com.tecnova.technical_test.domain.model.Task;
import com.tecnova.technical_test.infrastructure.adapter.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface TaskDboMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "limitDate", target = "limitDate")
    @Mapping(source = "userEntity.id", target = "idUser")
    @Mapping(source = "userEntity.name", target = "nameUser")
    @Mapping(source = "taskStatusEntity.id", target = "idTaskStatus")
    @Mapping(source = "taskStatusEntity.description", target = "descriptionTaskStatus")
    Task toDomain(TaskEntity entity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "limitDate", target = "limitDate")
    @Mapping(source = "idUser", target = "userEntity.id")
    @Mapping(source = "idTaskStatus", target = "taskStatusEntity.id")
    TaskEntity toDbo(Task domain);
}
