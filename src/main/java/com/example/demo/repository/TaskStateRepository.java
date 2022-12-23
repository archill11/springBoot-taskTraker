package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.TaskStateEntity;


public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {
}
