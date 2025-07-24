package com.tecnova.technical_test.infrastructure.adapter.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estados_tarea")
public class TaskStatusEntity {

    @Id
    private Long id;

    @Column(nullable = false, name = "descripcion")
    private String description;

    @OneToMany(mappedBy = "taskStatusEntity", cascade = CascadeType.ALL)
    private List<TaskEntity> taskList;
}
