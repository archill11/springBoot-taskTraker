package com.example.demo.factories;

import org.springframework.stereotype.Component;

import com.example.demo.dto.ProjectDto;
import com.example.demo.entity.ProjectEntity;

@Component
public class ProjectDtoFactory {

  public ProjectDto makeProjectDto(ProjectEntity entity) {

    return ProjectDto
            .builder()
            .id(entity.getId())
            .name(entity.getName())
            .createAt(entity.getCreateAt())
            .build();
  }
}
