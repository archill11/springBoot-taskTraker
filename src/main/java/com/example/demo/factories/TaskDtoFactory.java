package com.example.demo.factories;

import org.springframework.stereotype.Component;

import com.example.demo.dto.TaskDto;
import com.example.demo.entity.TaskEntity;

@Component
public class TaskDtoFactory {

  public TaskDto makeTaskDto(TaskEntity entity) {

    return TaskDto
            .builder()
            .id(entity.getId())
            .name(entity.getName())
            .createAt(entity.getCreateAt())
            .description(entity.getDescription())
            .build();
  }
}

