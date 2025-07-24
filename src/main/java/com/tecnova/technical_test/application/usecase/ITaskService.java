package com.tecnova.technical_test.application.usecase;

import com.tecnova.technical_test.domain.model.dto.TaskDto;
import com.tecnova.technical_test.domain.model.dto.request.TaskCreationRequest;
import com.tecnova.technical_test.domain.model.dto.request.TaskUpdateRequest;

import java.util.List;

public interface ITaskService {

    TaskDto getTaskById(Long id);
    List<TaskDto> getAllTask();
    TaskDto createTask(TaskCreationRequest request);
    TaskDto updateTask(TaskUpdateRequest taskUpdateRequest, Long idTask);
    void deleteTaskById(Long idTask);

}
