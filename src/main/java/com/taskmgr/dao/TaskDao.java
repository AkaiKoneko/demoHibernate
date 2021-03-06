package com.taskmgr.dao;

import com.taskmgr.model.Task;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Akai on 2017-03-29.
 */

public interface TaskDao {
	@Transactional
	List<Task> findAll();

	@Transactional
	Task getById(int id);

	@Transactional
	void delete(Task t);

	/**
	 * Use to edit task without change storyId
	 *
	 * @param task new values to assign
	 */
	@Transactional
	void edit(Task task);

	@Transactional
	void saveOrUpdate(Task t);

	@Transactional
	List<Task> getByStoryId(int storyId);

}
