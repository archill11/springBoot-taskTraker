package com.example.demo.factories;

import org.springframework.stereotype.Component;

import com.example.demo.dto.TaskStateDto;
import com.example.demo.entity.TaskStateEntity;

@Component
public class TaskStateDtoFactory {

  public TaskStateDto makeTasStateDto(TaskStateEntity entity) {

    return TaskStateDto
            .builder()
            .id(entity.getId())
            .name(entity.getName())
            .ordinal(entity.getOrdinal())
            .createAt(entity.getCreateAt())
            .build();
  }
}

