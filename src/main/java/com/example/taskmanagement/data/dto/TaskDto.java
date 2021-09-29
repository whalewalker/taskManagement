package com.example.taskmanagement.data.dto;


import com.example.taskmanagement.data.modal.TaskCategory;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TaskDto {
    @NotBlank(message = "task title cannot be blank")
    private String title;
    private String description;
    private LocalDate createdDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private TaskCategory category;


}
