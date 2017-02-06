package de.thb.fbi.maus.todo.tasks;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import de.thb.fbi.maus.todo.accessors.api.ITodoListAccessor;
import de.thb.fbi.maus.todo.accessors.api.TodoCRUDAccessor;
import de.thb.fbi.maus.todo.model.Todo;

public class TodoSender extends AsyncTask<TodoCRUDAccessor, Void, Boolean> {

	public ITodoListAccessor localAccessor;

	public TodoSender(ITodoListAccessor localAccessor) {
		super();
		this.localAccessor = localAccessor;
	}

	@Override
	protected Boolean doInBackground(TodoCRUDAccessor... params) {
		return updateAllWebAppTodos(params[0]);

	}

	private boolean updateAllWebAppTodos(TodoCRUDAccessor restAccessor) {
		boolean success = false;
		restAccessor.deleteAllItems();
		List<Todo> localTodos = new ArrayList<Todo>();
		localTodos = localAccessor.getTodos();
		for (Todo t : localTodos) {
			restAccessor.createItem(t);
		}
		return success;
	}

	public ITodoListAccessor getLocalAccessor() {
		return localAccessor;
	}

	public void setLocalAccessor(ITodoListAccessor localAccessor) {
		this.localAccessor = localAccessor;
	}

}
