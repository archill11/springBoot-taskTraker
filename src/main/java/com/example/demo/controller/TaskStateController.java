package com.example.demo.controller;

import com.example.demo.dto.ProjectDto;
import com.example.demo.dto.TaskStateDto;
import com.example.demo.entity.ProjectEntity;
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
import java.util.Optional;
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
  public List<TaskStateDto> getTaskStates(@PathVariable Long project_id) {

    ProjectEntity project = projectRepository
            .findById(project_id)
            .orElseThrow(() -> new NotFoundException(String.format("Project \"%s\" not found.", project_id)));

    return project.getTaskStates()
            .stream()
            .map(taskStateEntity -> taskStateDtoFactory.makeTasStateDto(taskStateEntity))
            .collect(Collectors.toList());
  }


  @PostMapping("/api/projects/{project_id}/task-states")
  public ProjectDto createTaskState(@RequestParam String name, @PathVariable Long project_id) {

    if (name.trim().isEmpty()) {
      throw new BadRequestException("Name is empty!");
    }

    ProjectEntity project = projectRepository
            .findById(project_id)
            .orElseThrow(() -> new NotFoundException(String.format("Project \"%s\" not found.", project_id)));

    Optional<TaskStateEntity> optionalAnotherTaskState = taskStateRepository
            .findTaskStateEntityByRightTaskStateIdIsNullAndProjectId(project_id); // ищем самого правого taskState в проекте



    // сохраняем новый taskState в БД
    TaskStateEntity taskState = taskStateRepository.saveAndFlush(
            TaskStateEntity.builder().name(name).project(project).build()
    );

    optionalAnotherTaskState
            .ifPresent(anotherTaskState -> {
              taskState.setLeftTaskState(anotherTaskState); // устанавливаем новому taskState левого соседа
              taskState.setRightTaskState(null);
              anotherTaskState.setRightTaskState(taskState); // устанавливаем самому правому taskState правого соседа
              taskStateRepository.saveAndFlush(anotherTaskState);
            });

    TaskStateEntity savedTaskState = taskStateRepository.saveAndFlush(taskState);

    /*// добавляем taskState в проект
//    List<TaskStateEntity> taskStates = project.getTaskStates();
//    taskStates.add(savedTaskState);
//    project.setTaskStates( taskStates );
//
//    project = projectRepository.saveAndFlush(project);*/

    return projectDtoFactory.makeProjectDto(project);
  }
}
