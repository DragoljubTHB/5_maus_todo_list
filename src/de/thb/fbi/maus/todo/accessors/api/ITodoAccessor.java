package de.thb.fbi.maus.todo.accessors.api;

import de.thb.fbi.maus.todo.model.Todo;

public interface ITodoAccessor {

	boolean hasItem();

	Todo readItem();

	void createItem();

	void writeItem();

	void deleteItem();

}
