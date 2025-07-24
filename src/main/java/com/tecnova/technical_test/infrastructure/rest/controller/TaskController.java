package com.tecnova.technical_test.infrastructure.rest.controller;

import com.tecnova.technical_test.application.usecase.ITaskService;
import com.tecnova.technical_test.domain.model.dto.TaskDto;
import com.tecnova.technical_test.domain.model.dto.request.TaskCreationRequest;
import com.tecnova.technical_test.domain.model.dto.request.TaskUpdateRequest;
import com.tecnova.technical_test.domain.model.dto.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app/task")
@Tag(name = "Task Controller", description = "Controller to management all operation related with Task")
public class TaskController {

    private final ITaskService taskService;

    @Autowired
    public TaskController(ITaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get One Task By Id", responses = {
            @ApiResponse(responseCode = "200", description = "Task Found",
                    content = @Content(schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "404", description = "Task Not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))},
            description = "Returns an existing Task in DB by Id")
    public ResponseEntity<TaskDto> getByIdTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping()
    @Operation(summary = "Get All Tasks", responses = {
            @ApiResponse(responseCode = "200", description = "Tasks Found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskDto.class)))),
            @ApiResponse(responseCode = "404", description = "Task Not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))},
            description = "Returns all Tasks in DB")
    public ResponseEntity<List<TaskDto>> getAllTask() {
        return ResponseEntity.ok(taskService.getAllTask());
    }

    @PostMapping()
    @Operation(summary = "Create Task", responses = {
            @ApiResponse(responseCode = "201", description = "Tasks Created",
                    content = @Content(schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "404", description = "Task Not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Task Not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))},
            description = "API to create a task in the DB")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskCreationRequest creationTaskRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(creationTaskRequest));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Task", responses = {
            @ApiResponse(responseCode = "200", description = "Tasks Updated",
                    content = @Content(schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "404", description = "User Not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Task Status Not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Task Not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))},
            description = "API to Edit a task in the DB")
    public ResponseEntity<TaskDto> editTask(@RequestBody TaskUpdateRequest taskUpdateRequest,
                                            @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.updateTask(taskUpdateRequest, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Task", responses = {
            @ApiResponse(responseCode = "204", description = "Tasks Deleted"),
            @ApiResponse(responseCode = "404", description = "Task Not Found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))},
            description = "API to Delete a task in the DB")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }
}
