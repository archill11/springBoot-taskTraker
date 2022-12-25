package com.example.demo.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskStateDto {

  @NonNull
  Long id;

  @NonNull
  String name;

  Long ordinal;

  @JsonProperty("left_task_state_id")
  Long leftTaskStateId;

  @JsonProperty("right_task_state_id")
  Long rightTaskStateId;

  @NonNull
  @JsonProperty("created_at")
  Instant createAt;
}
