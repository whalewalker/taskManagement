package com.example.taskmanagement.data.repository;

import com.example.taskmanagement.data.modal.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {
    boolean existsByTitle(String title);
}
