package com.example.taskmanagement.web.controllers;

import com.example.taskmanagement.data.dto.TaskDto;
import com.example.taskmanagement.data.model.Task;
import com.example.taskmanagement.services.UserService;
import com.example.taskmanagement.web.exceptions.TaskException;
import com.example.taskmanagement.web.payloads.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/tasks")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/{userId}/create")
    @PreAuthorize("hasAnyRole('ROLE_INDIVIDUAL', 'ROLE_ORGANIZATION', 'ROLE_ADMIN')")
    public ResponseEntity<?> createTask(@PathVariable String userId, @Valid @RequestBody TaskDto taskDto){
        try {
            TaskDto createdTask  = userService.createTask(userId, taskDto);
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        }catch (TaskException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("{taskId}/update")
    @PreAuthorize("hasAnyRole('ROLE_INDIVIDUAL', 'ROLE_ORGANIZATION', 'ROLE_ADMIN')")
    public ResponseEntity<?> updateTask(@PathVariable String taskId, @Valid @RequestBody TaskDto taskDto){
        try {
            TaskDto updatedTask  = userService.updateTask(taskId, taskDto);
            return new ResponseEntity<>(updatedTask, HttpStatus.CREATED);
        }catch (TaskException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_INDIVIDUAL', 'ROLE_ORGANIZATION', 'ROLE_ADMIN')")
    public ResponseEntity<?> getAllTask(){
        List<Task> tasks  = userService.getAllTask();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @DeleteMapping("{taskId}/delete")
    @PreAuthorize("hasAnyRole('ROLE_INDIVIDUAL', 'ROLE_ORGANIZATION', 'ROLE_ADMIN')")
    public ResponseEntity<?> deleteTask(@PathVariable String taskId){
        try {
            userService.deleteTask(taskId);
            return new ResponseEntity<>(new ApiResponse(true, "Task is successfully deleted"), HttpStatus.OK);
        }catch (TaskException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
