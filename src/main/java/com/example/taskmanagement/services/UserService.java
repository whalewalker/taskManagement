package com.example.taskmanagement.services;


import com.example.taskmanagement.data.dto.TaskDto;
import com.example.taskmanagement.data.model.Task;
import com.example.taskmanagement.web.exceptions.TaskException;

import java.util.List;

public interface UserService {
    TaskDto createTask(String userId, TaskDto taskDto) throws TaskException;
    TaskDto updateTask(String taskId, TaskDto updatedTask) throws TaskException;
    void deleteTask(String taskId) throws TaskException;
    List<Task> getAllTask();
    List<TaskDto> sortByTitle(String title);
    List<TaskDto> filterTaskByDay();

}
