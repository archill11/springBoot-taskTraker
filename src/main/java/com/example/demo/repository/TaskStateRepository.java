package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.TaskStateEntity;

import java.util.Optional;


public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {

  Optional<TaskStateEntity> findTaskStateEntityByRightTaskStateIdIsNullAndProjectId(Long projectId);

}
