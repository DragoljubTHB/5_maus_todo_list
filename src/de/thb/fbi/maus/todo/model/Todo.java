package de.thb.fbi.maus.todo.model;

import java.io.Serializable;

public class Todo implements Serializable {

	/**
	 * some static id assignment
	 */
	private static long ID = 0;

	/**
	 * 
	 */
	private static final long serialVersionUID = -7481912314472891511L;

	/**
	 * the fields
	 */
	private int id;

	private String name;
	private String description;
	private String iconUrl;
	private long dueDate;
	private int durationMin;
	private boolean done;
	private Rang rang;

	public enum Rang {
		A, B, C, D, E, F
	}

	/*
	 * CTORS
	 */
	public Todo(int id, String name, String description, String iconUrl, long dueDate, int durationMin, boolean done,
			Rang rang) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.iconUrl = iconUrl;
		this.dueDate = dueDate;
		this.durationMin = durationMin;
		this.done = done;
		this.rang = rang;
	}

	public Todo() {	}

	public void updateFrom(Todo todo) {
		this.setName(todo.getName());
		this.setDescription(todo.getDescription());
		this.setIconUrl(todo.getIconUrl());
		this.setDueDate(todo.getDueDate());
		this.setDone(todo.isDone());
		this.setRang(todo.getRang());
	}

	/*
	 * std
	 */

	public static long getID() {
		return ID;
	}

	public static void setID(long aID) {
		ID = aID;
	}

	public int getId() {
		return id;
	}

	public void setId(int aId) {
		id = aId;
	}

	public String getName() {
		return name;
	}

	public void setName(String aName) {
		name = aName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String aDescription) {
		description = aDescription;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	/**
	 * TODO implement
	 * 
	 * @return
	 */
	public void setIconUrl(String aIconUrl) {
		iconUrl = aIconUrl;
	}

	public long getDueDate() {
		return dueDate;
	}

	public void setDueDate(long aDueDate) {
		dueDate = aDueDate;
	}


	public int getDurationMin() {
		return durationMin;
	}

	public void setDurationMin(int durationMin) {
		this.durationMin = durationMin;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean aDone) {
		done = aDone;
	}

	public Rang getRang() {
		return rang;
	}

	public void setRang(Rang aRang) {
		rang = aRang;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Todo)){
			return false; 
		}else {
			return ((Todo) obj).getId() == this.getId();
		}
	}
	
	/*
	 * 
	 */
	

}