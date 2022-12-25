package com.example.demo.entity;

import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "task_state")
public class TaskStateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  String name;

  Long ordinal;

  @OneToOne
  TaskStateEntity leftTaskState;

  @ColumnDefault("null")
  @OneToOne
  TaskStateEntity rightTaskState;

  @ManyToOne
  ProjectEntity project;

  @Builder.Default
  Instant createAt = Instant.now();

  @Builder.Default
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "task_state_id", referencedColumnName = "id")
  List<TaskEntity> tasks = new ArrayList<>();

  public Optional<TaskStateEntity> getLeftTaskState() { // так как этих сущностей может и не быть
    return Optional.ofNullable(leftTaskState);          // а null возвращать нельзя
  }

  public Optional<TaskStateEntity> getRightTaskState() {
    return Optional.ofNullable(rightTaskState);
  }
}
