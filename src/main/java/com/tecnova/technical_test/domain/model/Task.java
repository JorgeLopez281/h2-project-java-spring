package com.tecnova.technical_test.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Task {
    private Long id;
    private String title;
    private String description;
    private LocalDate limitDate;
    private Long idUser;
    private String nameUser;
    private Long idTaskStatus;
    private String descriptionTaskStatus;
}