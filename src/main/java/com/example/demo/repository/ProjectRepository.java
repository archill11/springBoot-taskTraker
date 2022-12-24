package com.example.demo.repository;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ProjectEntity;


public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

  Optional<ProjectEntity> findByName(String name);

//  Stream<ProjectEntity> streamAll();
//
//  Stream<ProjectEntity> findAllContainsName(String name);
}
