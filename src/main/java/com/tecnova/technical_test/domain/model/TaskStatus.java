package com.tecnova.technical_test.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskStatus {

    private Long id;
    private String description;
    private List<Task> taskList;
}
