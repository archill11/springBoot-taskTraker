package com.example.demo.controller;

import com.example.demo.dto.ProjectDto;
import com.example.demo.entity.ProjectEntity;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.factories.ProjectDtoFactory;
import com.example.demo.repository.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor // эта анотация инджектит зависимовть через конструктор
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // эта анотация делает все поля private и final
@Transactional
@RestController
public class ProjectController {

  ProjectRepository projectRepository;

  ProjectDtoFactory projectDtoFactory;


  @GetMapping("/api/projects")
  public List<ProjectDto> getProjects() {

    List<ProjectEntity> projects = projectRepository.findAll();

    List<ProjectDto> projectsDto = projects.stream()
            .map(projectEntity -> projectDtoFactory.makeProjectDto(projectEntity))
            .collect(Collectors.toList());

    return projectsDto;
  }


  @PostMapping("/api/projects")
  public ProjectDto createProject(@RequestParam String name) {

    if (name.trim().isEmpty()) {
      throw new BadRequestException("Name is empty!");
    }

    projectRepository
            .findByName(name)
            .ifPresent(project -> {
              throw new BadRequestException(String.format("Project \"%s\" already exists.", name));
            });

    ProjectEntity project = projectRepository.saveAndFlush(
            ProjectEntity.builder().name(name).build()
    );

    return projectDtoFactory.makeProjectDto(project);
  }


  @PatchMapping("/api/projects/{project_id}")
  public ProjectDto editProject(@RequestParam String name, @PathVariable Long project_id) {

    if (name.trim().isEmpty()) {
      throw new BadRequestException("Name is empty!");
    }

    ProjectEntity project = projectRepository
            .findById(project_id)
            .orElseThrow(() -> new NotFoundException(String.format("Project \"%s\" not found.", project_id)));

    projectRepository.findByName(name)
            .filter(anotherProject -> !Objects.equals(anotherProject.getId(), project_id))
            .ifPresent(anotherProject -> {
              throw new BadRequestException(String.format("Project \"%s\" already exists.", name));
            });

    project.setName(name);

    project = projectRepository.saveAndFlush(project);

    return projectDtoFactory.makeProjectDto(project);
  }


  @DeleteMapping("/api/projects/{project_id}")
  public String deleteProject(@PathVariable Long project_id) {

    projectRepository.findById(project_id)
            .orElseThrow(() -> new NotFoundException(String.format("Project \"%s\" not found.", project_id)));

    projectRepository.deleteById(project_id);

    return "Successfully deleted";
  }
}
