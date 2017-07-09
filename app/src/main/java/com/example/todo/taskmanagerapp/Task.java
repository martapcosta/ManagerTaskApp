package com.example.todo.taskmanagerapp;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Task's model (class) 
 * @author Rute Pereira & Marta Costa
 *
 */
public class Task implements KvmSerializable {

	private static final long serialVersionUID = -5577579081118070434L;

	private String title;
	private String description;
	private Date creationDate = new Date();
	private Date updateDate;
	private Date todoDate;
	private Long id;
	private boolean status = false;

	public Long Id;
	public String Title;
	public String Description;
	//public Date TodoDate;

	public Task(){}


	public Task(long id, String title, String description, Date todoDate) {

		Id = id;
		Title = title;
		Description = description;
		//TodoDate = todoDate;
	}

	/**
	 * Gets the task's title
	 * @return [String] the task's title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the task's title
	 * @param [String] title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the task's description
	 * @return [String] the task's description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the task's description
	 * @param [String] description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the task's creation date
	 * @return [Date] the task's creation date
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Gets the task's last update date
	 * @return [Date] the task's last update date
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * Sets the task's last update date
	 * @param [Date] updateDate
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * Gets the task's to-do date
	 * @return [Date] the task's to-do date
	 */
	public Date getTodoDate() {
		return todoDate;
	}

	/**
	 * Sets the task's to-do date
	 * @param [Date] todoDate
	 */
	public void setTodoDate(Date todoDate) {
		this.todoDate = todoDate;
	}

	/**
	 * Gets the task's id
	 * @return [long] id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the task's id
	 * @param [int] id
	 */
	public void setId(Long id) {
		this.id = id;
	}/**
	 * Gets the task's status
	 * Returns true if task is completed, false otherwise
	 * @return [boolean] status
	 */
	public boolean getStatus() {
		return status;
	}

	/**
	 * Sets the task's status
	 * False for task not yet done, True for task done
	 * @param [boolean] status
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public Object getProperty(int arg0) {
		switch(arg0)
		{
			case 0:
				return Id;
			case 1:
				return Title;
			case 2:
				return Description;
			/*case 3:
				return TodoDate;*/
		}

		return null;
	}

	@Override
	public int getPropertyCount() {
		return 4;
	}

	@Override
	public void setProperty(int index, Object value) {
		switch(index)
		{
			case 0:
				Id = Long.parseLong(value.toString());
				break;
			case 1:
				Title = value.toString();
				break;
			case 2:
				Description = value.toString();
				break;
			/*case 3:
				DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
				Date startDate = df.parse(value.toString());
				TodoDate = Date;
				break;*/
			default:
				break;
		}
	}

	@Override
	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		switch(index)
		{
			case 0:
				info.type = PropertyInfo.LONG_CLASS;
				info.name  = "Id";
				break;
			case 1:
				info.type = PropertyInfo.STRING_CLASS;
				info.name  = "Title";
				break;
			case 2:
				info.type = PropertyInfo.STRING_CLASS;
				info.name  = "Description";
				break;
			/*case 3:
				info.type = PropertyInfo.OBJECT_TYPE;
				info.name  = "TodoDate";
				break;*/
			default:break;
		}
	}
}
