package com.example.taskmanagement.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.taskmanagement.data.model.TaskCategory.WORK;
import static com.example.taskmanagement.data.model.TaskStatus.DOING;

@Data
@Document
public class Task {
    @Id
    private String id;
    private String title;
    private String description;
    private LocalDate createdDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private TaskStatus taskStatus;
    @DBRef
    private User user;
    private TaskCategory category;


}
