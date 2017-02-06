package de.thb.fbi.maus.todo.accessors.api;

import de.thb.fbi.maus.todo.model.TodoUser;

public interface ITodoUserAccessor {

	boolean hasItem();

	TodoUser readItem();

	void createItem();

	void writeItem();

	void deleteItem();

}
