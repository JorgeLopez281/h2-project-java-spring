package com.tecnova.technical_test.domain.model.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskCreationRequest {

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 5, max = 30, message = "Title must be between 5 and 30 characters")
    private String title;

    @NotEmpty(message = "Description cannot be empty")
    @Size(min = 10, max = 60, message = "Description must be between 10 and 60 characters")
    private String description;

    @NotEmpty(message = "Limit Date cannot be empty")
    @FutureOrPresent(message = "Limit Date must be today or in the future")
    private LocalDate limitDate;

    @NotEmpty(message = " Id User cannot be empty")
    private Integer idUser;

    @NotEmpty(message = " Id Task Status cannot be empty")
    private Integer idTaskStatus;
}