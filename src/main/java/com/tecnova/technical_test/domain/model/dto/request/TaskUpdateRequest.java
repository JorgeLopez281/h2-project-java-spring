package com.tecnova.technical_test.domain.model.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
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
public class TaskUpdateRequest {

    @Size(min = 5, max = 30, message = "Title must be between 5 and 30 characters")
    private String title;

    @Size(min = 10, max = 60, message = "Description must be between 10 and 60 characters")
    private String description;

    @FutureOrPresent(message = "Limit Date must be today or in the future")
    private LocalDate limitDate;

    private Integer idUser;

    private Integer idTaskStatus;
}