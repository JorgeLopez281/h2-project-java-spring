package com.tecnova.technical_test.application.service;

import com.tecnova.technical_test.application.mapper.TaskDtoMapper;
import com.tecnova.technical_test.application.usecase.ITaskService;
import com.tecnova.technical_test.domain.model.dto.TaskDto;
import com.tecnova.technical_test.domain.model.dto.request.TaskCreationRequest;
import com.tecnova.technical_test.domain.model.dto.request.TaskUpdateRequest;
import com.tecnova.technical_test.domain.port.ITaskPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService implements ITaskService {

    private final ITaskPort taskPort;
    private final TaskDtoMapper taskDtoMapper;

    @Autowired
    public TaskService(final ITaskPort taskPort,
                       final TaskDtoMapper taskDtoMapper) {
        this.taskPort = taskPort;
        this.taskDtoMapper = taskDtoMapper;
    }

    @Override
    public TaskDto getTaskById(Long id) {
        var taskInfo = taskPort.getTaskById(id);

        return taskDtoMapper.toDto(taskInfo);
    }

    @Override
    public List<TaskDto> getAllTask() {
        var taskList = taskPort.getAllTask();
        return taskList.stream().map(taskDtoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public TaskDto createTask(TaskCreationRequest request) {
        var taskToCreate = taskDtoMapper.toDomain(request);
        var taskCreated = taskPort.createTask(taskToCreate);

        return taskDtoMapper.toDto(taskCreated);
    }

    @Override
    public TaskDto updateTask(TaskUpdateRequest taskUpdateRequest, Long idTask) {
        var taskToUpdate = taskDtoMapper.toDomainUpdate(taskUpdateRequest);
        var taskUpdated = taskPort.updateTask(taskToUpdate, idTask);

        return taskDtoMapper.toDto(taskUpdated);
    }

    @Override
    public void deleteTaskById(Long idTask) {
        taskPort.deleteTaskById(idTask);
    }
}
