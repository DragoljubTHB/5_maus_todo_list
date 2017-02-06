package de.thb.fbi.maus.todo;

import android.app.Application;
import de.thb.fbi.maus.todo.accessors.api.TodoCRUDAccessor;
import de.thb.fbi.maus.todo.accessors.api.TodoUserCRUDAccessor;
import de.thb.fbi.maus.todo.accessors.impl.ResteasyTodoCRUDAccessor;
import de.thb.fbi.maus.todo.accessors.impl.ResteasyTodoUserCRUDAccessor;

public class TodoApplication extends Application {

	private TodoUserCRUDAccessor userRestAccessor;
	private TodoCRUDAccessor todoRestAccessor;
	public TodoApplication() {
		this.todoRestAccessor = new ResteasyTodoCRUDAccessor(getRestBaseUrl());
		this.userRestAccessor = new ResteasyTodoUserCRUDAccessor(getRestBaseUrl());
	}
	/*
	 * std
	 */
	
	
	public TodoUserCRUDAccessor getUserRestAccessor() {
		return userRestAccessor;
	}


	public void setUserRestAccessor(TodoUserCRUDAccessor userRestAccessor) {
		this.userRestAccessor = userRestAccessor;
	}


	public TodoCRUDAccessor getTodoRestAccessor() {
		return todoRestAccessor;
	}


	public void setTodoRestAccessor(TodoCRUDAccessor todoRestAccessor) {
		this.todoRestAccessor = todoRestAccessor;
	}

	private String getRestBaseUrl() {

		return getWebAppBaseUrl() + "rest";
	}
	private String getWebAppBaseUrl() {
		
		return "http://10.0.2.2:8080/DataAccessRemoteWebapp/";
	}
	public String getWebAppIpAndPort(){
		return "10.0.2.2:8080";
	}
	
	

}
