package com.example.taskmanagement.services;

import com.example.taskmanagement.data.dto.TaskDto;
import com.example.taskmanagement.data.model.Task;
import com.example.taskmanagement.data.model.User;
import com.example.taskmanagement.data.repository.TaskRepository;
import com.example.taskmanagement.data.repository.UserRepository;
import com.example.taskmanagement.web.exceptions.TaskException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.example.taskmanagement.data.model.TaskCategory.WORK;
import static com.example.taskmanagement.data.model.TaskStatus.DOING;
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
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(format("No user found with user name %s", userId)));

        if (taskRepository.existsByTitle(taskDto.getTitle())){
            throw new TaskException(format("Task already exists with %s", taskDto.getTitle()));
        }
        Task task = mapper.map(taskDto, Task.class);

        task.setCreatedDate(LocalDate.now());
        task.setStartTime(LocalTime.now());
        task.setEndTime(LocalTime.now().plusHours(24));
         if (task.getCategory() == null || task.getTaskStatus() == null){
             task.setCategory(WORK);
             task.setTaskStatus(DOING);
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
    public List<Task> getAllTask() {
        return taskRepository.findAll();
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
