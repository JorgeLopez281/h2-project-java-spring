package com.tecnova.technical_test.domain.port;

import com.tecnova.technical_test.domain.model.Task;

import java.util.List;

public interface ITaskPort {

    Task getTaskById(Long id);
    List<Task> getAllTask();
    Task createTask(Task request);
    Task updateTask(Task request, Long idTask);
    void deleteTaskById(Long idTask);

}
