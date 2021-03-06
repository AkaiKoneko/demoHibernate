package com.taskmgr.controller.Rest;

import com.taskmgr.dao.StoryDao;
import com.taskmgr.dao.TaskDao;
import com.taskmgr.model.Task;
import com.taskmgr.model.TaskStatus;
import com.taskmgr.model.User;
import com.taskmgr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akai on 2017-03-29.
 */
@RestController
public class TaskRestController {

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private StoryDao storyDao;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/api/task", method = RequestMethod.POST, consumes = {"application/json"})
	public ResponseEntity<Task> addTask(@RequestBody Task task) {
		taskDao.saveOrUpdate(task);
		return new ResponseEntity<Task>(task, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/api/task{taskId}", method = RequestMethod.DELETE, consumes = {"application/json"})
	public ResponseEntity deleteTask(@PathVariable int taskId) {
		taskDao.delete(taskDao.getById(taskId));
		return new ResponseEntity(HttpStatus.ACCEPTED);
	}


	@RequestMapping(value = "/api/task{status}", method = RequestMethod.PUT, consumes = {"application/json"})
	public ResponseEntity<Task> editTask(@RequestBody Task task, @PathVariable String status) {
		//TODO check task to exist in database
		List<TaskStatus> taskStatuses = new ArrayList<TaskStatus>();
		taskStatuses.addAll(taskDao.getById(task.getId()).getStory().getIteration().getTaskStatuses());
		for (int i = 0; i < taskStatuses.size(); i++) {
			if (taskStatuses.get(i).getName().equals(status)) {
				task.setTaskStatus(taskStatuses.get(i));
			}
		}
		taskDao.edit(task);
		return new ResponseEntity<Task>(task, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/task{id}/{status}", method = RequestMethod.POST, consumes = {"application/json"})
	public ResponseEntity<Task> addTaskToStory(@RequestBody Task task, @PathVariable int id, @PathVariable String status) {
		task.setStory(storyDao.getById(id));
		List<TaskStatus> taskStatuses = new ArrayList<TaskStatus>();
		taskStatuses.addAll(storyDao.getById(id).getIteration().getTaskStatuses());
		for (int i = 0; i < taskStatuses.size(); i++) {
			if (taskStatuses.get(i).getName().equals(status)) {
				task.setTaskStatus(taskStatuses.get(i));
			}
		}
		taskDao.saveOrUpdate(task);
		return new ResponseEntity<Task>(task, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/taskList", method = RequestMethod.GET)
	public List<Task> allTaskList() {
		return taskDao.findAll();
	}


	@RequestMapping(value = "/api/taskList{id}")
	public List<Task> taskArrayList(@PathVariable int id) {
		List<Task> taskList = new ArrayList<Task>();
		User user = userService.findById(id);
		taskList.addAll(user.getUserTasks());
		return taskList;
	}


	@RequestMapping(value = "/api/storyTasks{id}")
	public List<Task> iterationList(@PathVariable int id) {
		List<Task> taskList = taskDao.getByStoryId(id);
		return taskList;
	}



}

