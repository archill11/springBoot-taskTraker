package com.example.demo.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "project")
public class ProjectEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  Long id;

  @Column(unique = true)
  String name;

  @Builder.Default
  Instant createAt = Instant.now();

  @Builder.Default
  @OneToMany(fetch = FetchType.LAZY) // удалить лайзи
  @JoinColumn(name = "project_id", referencedColumnName = "id")
  List<TaskStateEntity> taskState = new ArrayList<>();
}
