package com.tecnova.technical_test.application.mapper;

import com.tecnova.technical_test.domain.model.Task;
import com.tecnova.technical_test.domain.model.dto.TaskDto;
import com.tecnova.technical_test.domain.model.dto.request.TaskCreationRequest;
import com.tecnova.technical_test.domain.model.dto.request.TaskUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") 
public interface TaskDtoMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "limitDate", target = "limitDate")
    @Mapping(source = "idUser", target = "idUser")
    @Mapping(source = "nameUser", target = "nameUser")
    @Mapping(source = "idTaskStatus", target = "idTaskStatus")
    @Mapping(source = "descriptionTaskStatus", target = "descriptionTaskStatus")
    TaskDto toDto(Task domain);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "limitDate", target = "limitDate")
    @Mapping(source = "idUser", target = "idUser")
    @Mapping(source = "idTaskStatus", target = "idTaskStatus")
    Task toDomain(TaskCreationRequest creationTaskRequest);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "limitDate", target = "limitDate")
    @Mapping(source = "idUser", target = "idUser")
    @Mapping(source = "idTaskStatus", target = "idTaskStatus")
    Task toDomainUpdate(TaskUpdateRequest taskUpdateRequest);
}
