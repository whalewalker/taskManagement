package com.example.taskmanagement.data.modal;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.taskmanagement.data.modal.TaskCategory.WORK;
import static com.example.taskmanagement.data.modal.TaskStatus.DOING;
import static com.example.taskmanagement.data.modal.TaskStatus.TODO;

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

    public Task(String title, String description) {
        this(title);
        this.description = description;
        this.taskStatus = DOING;
    }

    public Task(String title) {
        this.title = title;
        createdDate = LocalDate.now();
        startTime = LocalTime.now();
        endTime = LocalTime.now().plusHours(24);
        category = WORK;
        this.taskStatus = DOING;
    }

    public Task(String title, String description, LocalDate createdDate, LocalTime startTime, LocalTime endTime, TaskCategory category) {
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
    }
}
