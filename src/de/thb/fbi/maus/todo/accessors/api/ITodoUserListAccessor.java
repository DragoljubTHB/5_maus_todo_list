package de.thb.fbi.maus.todo.accessors.api;

import java.util.List;

import android.widget.ListAdapter;
import de.thb.fbi.maus.todo.model.TodoUser;

public interface ITodoUserListAccessor {
	
	 void init();

	ListAdapter getAdapter();
	
	List<TodoUser> readAll();

	TodoUser getSelectedItem(int position, long id);

	void updateItem(TodoUser item);

	void deleteItem(TodoUser item);

	void addItem(TodoUser item);

	void close();

}
