package com.tecnova.technical_test.infrastructure.adapter;

import com.tecnova.technical_test.domain.model.Task;
import com.tecnova.technical_test.domain.model.constant.TaskConstant;
import com.tecnova.technical_test.domain.model.constant.TaskStatusConstant;
import com.tecnova.technical_test.domain.model.constant.UserConstant;
import com.tecnova.technical_test.domain.port.ITaskPort;
import com.tecnova.technical_test.infrastructure.adapter.entity.TaskEntity;
import com.tecnova.technical_test.infrastructure.adapter.exceptions.TaskException;
import com.tecnova.technical_test.infrastructure.adapter.exceptions.TaskStatusException;
import com.tecnova.technical_test.infrastructure.adapter.exceptions.UserException;
import com.tecnova.technical_test.infrastructure.adapter.mapper.TaskDboMapper;
import com.tecnova.technical_test.infrastructure.adapter.repository.ITaskRepository;
import com.tecnova.technical_test.infrastructure.adapter.repository.ITaskStatusRepository;
import com.tecnova.technical_test.infrastructure.adapter.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskSpringJpaAdapter implements ITaskPort {

    private final ITaskRepository taskRepository;
    private final IUserRepository userRepository;
    private final ITaskStatusRepository taskStatusRepository;
    private final TaskDboMapper taskDboMapper;

    @Autowired
    public TaskSpringJpaAdapter(ITaskRepository taskRepository, IUserRepository userRepository,
                                ITaskStatusRepository taskStatusRepository,
                                TaskDboMapper taskDboMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskStatusRepository = taskStatusRepository;
        this.taskDboMapper = taskDboMapper;
    }

    @Override
    public Task getTaskById(Long id) {
        //Esto devuelve un objeto de Infraestructura
        var optionalUserInfo = taskRepository.findById(id);

        if (optionalUserInfo.isEmpty()) {
            throw new TaskException(HttpStatus.NOT_FOUND,
                    String.format(TaskConstant.TASK_NOT_FOUND_MESSAGE_ERROR, id));
        }

        //Se convierte el objeto de Infraestructura a Dominio
        return taskDboMapper.toDomain(optionalUserInfo.get());
    }

    @Override
    public List<Task> getAllTask() {
        var taskList = taskRepository.findAll();

        if (taskList.isEmpty()) {
            throw new TaskException(HttpStatus.NOT_FOUND,
                    String.format(TaskConstant.TASKS_NOT_FOUND_MESSAGE_ERROR));
        }
        return taskList.stream().map(taskDboMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Task createTask(Task request) {
        var userInfoOptional = userRepository.findById(request.getIdUser());

        if (userInfoOptional.isEmpty()) {
            throw new UserException(HttpStatus.NOT_FOUND,
                    String.format(UserConstant.USER_NOT_FOUND_MESSAGE_ERROR, request.getIdUser()));
        }

        var taskStatusOptional = taskStatusRepository.findById(request.getIdTaskStatus());

        if (taskStatusOptional.isEmpty()) {
            throw new TaskStatusException(HttpStatus.NOT_FOUND,
                    String.format(TaskStatusConstant.TASK_STATUS_NOT_FOUND_MESSAGE_ERROR, request.getIdTaskStatus()));
        }

        var taskToCreate = taskDboMapper.toDbo(request);
        var taskSaved = taskRepository.save(taskToCreate);

        taskSaved.getUserEntity().setName(userInfoOptional.get().getName());
        taskSaved.getTaskStatusEntity().setDescription(taskStatusOptional.get().getDescription());

        return taskDboMapper.toDomain(taskSaved);
    }

    @Override
    public Task updateTask(Task request, Long idTask) {
        var existingTaskOptional = taskRepository.findById(idTask);

        if (existingTaskOptional.isEmpty()) {
            throw new TaskException(HttpStatus.NOT_FOUND,
                    String.format(TaskConstant.TASK_NOT_FOUND_MESSAGE_ERROR, request.getIdUser()));
        }
        var existingTask = getTaskEntity(request, existingTaskOptional, idTask);
        var taskUpdated = taskRepository.save(existingTask);

        return taskDboMapper.toDomain(existingTask);
    }

    @Override
    public void deleteTaskById(Long idTask) {
        var existingTaskOptional = taskRepository.findById(idTask);

        if (existingTaskOptional.isEmpty()) {
            throw new TaskException(HttpStatus.NOT_FOUND,
                    String.format(TaskConstant.TASK_NOT_FOUND_MESSAGE_ERROR, idTask));
        }
        taskRepository.deleteById(idTask);
    }

    private TaskEntity getTaskEntity(Task request, Optional<TaskEntity> existingTaskOptional, Long idTask) {

        if (existingTaskOptional.isEmpty()) {
            throw new TaskException(HttpStatus.NOT_FOUND,
                    String.format(TaskConstant.TASK_NOT_FOUND_MESSAGE_ERROR, idTask));
        }

        var existingTask = existingTaskOptional.get();

        if (request.getTitle() != null) {
            existingTask.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            existingTask.setDescription(request.getDescription());
        }

        if (request.getLimitDate() != null) {
            existingTask.setLimitDate(request.getLimitDate());
        }

        if (request.getIdUser() != null) {
            var userInfoOptional = userRepository.findById(request.getIdUser());
            if (userInfoOptional.isEmpty()) {
                throw new UserException(HttpStatus.NOT_FOUND,
                        String.format(UserConstant.USER_NOT_FOUND_MESSAGE_ERROR, request.getIdUser()));
            }
            existingTask.setUserEntity(userInfoOptional.get());
        }

        if (request.getIdTaskStatus() != null) {
            var taskStatusOptional = taskStatusRepository.findById(request.getIdTaskStatus());
            if (taskStatusOptional.isEmpty()) {
                throw new TaskStatusException(HttpStatus.NOT_FOUND,
                        String.format(TaskStatusConstant.TASK_STATUS_NOT_FOUND_MESSAGE_ERROR, request.getIdTaskStatus()));
            }
            existingTask.setTaskStatusEntity(taskStatusOptional.get());
        }
        return existingTask;
    }
}
