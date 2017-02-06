package de.thb.fbi.maus.todo.accessors.api;

import java.util.List;

import android.widget.ListAdapter;
import de.thb.fbi.maus.todo.model.Todo;

public interface ITodoListAccessor {
	
	void init();

	ListAdapter getAdapter();
	
	List<Todo> getTodos();
	
	List<Todo> readAll();

	Todo getSelectedItem(int position, long id);

	void updateItem(Todo item);

	void deleteItem(Todo item);

	void addItem(Todo item);

	void close();

	void sortBy(String string);

}
