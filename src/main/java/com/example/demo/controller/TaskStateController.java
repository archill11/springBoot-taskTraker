package com.example.demo.controller;


import com.example.demo.dto.ProjectDto;
import com.example.demo.dto.TaskStateDto;
import com.example.demo.entity.ProjectEntity;
import com.example.demo.entity.TaskEntity;
import com.example.demo.entity.TaskStateEntity;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.factories.ProjectDtoFactory;
import com.example.demo.factories.TaskStateDtoFactory;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskStateRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor // эта анотация инджектит зависимовть через конструктор
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // эта анотация делает все поля private и final
@Transactional
@RestController
public class TaskStateController {

  TaskStateRepository taskStateRepository;

  TaskStateDtoFactory taskStateDtoFactory;

  ProjectRepository projectRepository;

  ProjectDtoFactory projectDtoFactory;

  @GetMapping("/api/projects/{project_id}/task-states")
  public List<TaskStateDto> getTaskStates(@PathVariable String project_id) {

    List<TaskStateEntity> taskStates = taskStateRepository.findAll();

    List<TaskStateDto> taskStatesDto = taskStates.stream()
            .map(projectEntity -> taskStateDtoFactory.makeTasStateDto(projectEntity))
            .collect(Collectors.toList());

    return taskStatesDto;
  }


  @PostMapping("/api/projects/{project_id}/task-states")
  public ProjectDto createTaskState(@RequestParam String name, @PathVariable Long project_id) {

    if (name.trim().isEmpty()) {
      throw new BadRequestException("Name is empty!");
    }

    ProjectEntity project = projectRepository
            .findById(project_id)
            .orElseThrow(() -> new NotFoundException(String.format("Project \"%s\" not found.", project_id)));

    TaskStateEntity taskState = taskStateRepository.saveAndFlush(
            TaskStateEntity.builder().name(name).build()
    );

    // добавляем taskState в проект
    List<TaskStateEntity> taskStates = project.getTaskStates();
    taskStates.add(taskState);
    project.setTaskStates( taskStates );

    project = projectRepository.saveAndFlush(project);

    return projectDtoFactory.makeProjectDto(project);
  }
}
