package de.thb.fbi.maus.todo.model;

import java.io.Serializable;
import java.util.List;

public class TodoUser implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String email;
	private String password;
	
	private List<Todo> todos; //FIXME @OneToMany
	
	
	
	public TodoUser(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
	public TodoUser(int id, String email, String password, List<Todo> todos) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.todos = todos;
	}
	public TodoUser() {
		// TODO Auto-generated constructor stub
	}
	public TodoUser updateFrom(TodoUser aUser){
		this.setEmail(aUser.getEmail());
		this.setPassword(aUser.getPassword());
		return this;
	}
	
	/*
	 * std
	 */
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Todo> getTodos() {
		return todos;
	}
	public void setTodos(List<Todo> todos) {
		this.todos = todos;
	}
	/*
	 * 
	 */

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof TodoUser)){
			return false;
		} else {
			return ((TodoUser) obj).getId() == this.getId();
		}
	}
	
	

}
