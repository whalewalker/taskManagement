package com.example.taskmanagement.services;

import com.example.taskmanagement.data.dto.TaskDto;
import com.example.taskmanagement.data.modal.Task;
import com.example.taskmanagement.data.modal.User;
import com.example.taskmanagement.data.repository.TaskRepository;
import com.example.taskmanagement.data.repository.UserRepository;
import com.example.taskmanagement.web.exceptions.TaskException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public TaskDto createTask(String userId, TaskDto taskDto) throws TaskException {
        log.info("Checked");
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(format("No user found with user name %s", userId)));

        Task task;
        if (taskRepository.existsByTitle(taskDto.getTitle())){
            throw new TaskException(format("Task already exists with %s", taskDto.getTitle()));
        }
        if (taskDto.getDescription() == null){
            task = new Task(taskDto.getTitle());
        }else if (taskDto.getDescription() != null && taskDto.getStartTime() == null){
            task = new Task(taskDto.getTitle(), taskDto.getDescription());
        }else {
            task = new Task(taskDto.getTitle(), taskDto.getDescription(), taskDto.getCreatedDate(),
                    taskDto.getStartTime(), taskDto.getEndTime(), taskDto.getCategory());
        }
        task.setUser(user);
        Task savedTask = taskRepository.save(task);
        return mapper.map(savedTask, TaskDto.class);
    }

    @Override
    public TaskDto updateTask(String taskId, TaskDto taskDto) throws TaskException {
        Task taskToUpdate = taskRepository.findById(taskId).orElseThrow(() -> new TaskException(format("No task found with task id %s", taskId)));
        mapper.map(taskDto, taskToUpdate);
        Task savedTask = taskRepository.save(taskToUpdate);
        return mapper.map(savedTask, TaskDto.class);
    }
    @Override
    public void deleteTask(String taskId) throws TaskException {
        Task taskToDelete = taskRepository.findById(taskId).orElseThrow(() -> new TaskException(format("No task found with task id %s", taskId)));
        taskRepository.delete(taskToDelete);
    }

    @Override
    public List<TaskDto> getAllTask() {
        List<Task> tasks = taskRepository.findAll();
        return mapper.map(tasks, (Type) TaskDto.class);
    }

    @Override
    public List<TaskDto> sortByTitle(String title) {
        return null;
    }

    @Override
    public List<TaskDto> filterTaskByDay() {
        return null;
    }
}
