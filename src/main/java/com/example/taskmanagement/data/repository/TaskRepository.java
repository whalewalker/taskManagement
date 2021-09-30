package com.example.taskmanagement.data.repository;

import com.example.taskmanagement.data.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    boolean existsByTitle(String title);
}
