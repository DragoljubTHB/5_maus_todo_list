package de.thb.fbi.maus.todo.tasks;

import java.util.List;

import android.os.AsyncTask;
import de.thb.fbi.maus.todo.accessors.api.ITodoListAccessor;
import de.thb.fbi.maus.todo.accessors.api.TodoCRUDAccessor;
import de.thb.fbi.maus.todo.model.Todo;

public class TodoReceiver extends AsyncTask<TodoCRUDAccessor, Void, List<Todo>>{

	public ITodoListAccessor localAccessor; 
	
	
	public TodoReceiver(ITodoListAccessor localAccessor) {
		super();
		this.localAccessor = localAccessor;
	}
	@Override
	protected List<Todo> doInBackground(TodoCRUDAccessor... params) {
		return params[0].readAllItems();
	}
	public ITodoListAccessor getLocalAccessor() {
		return localAccessor;
	}
	public void setLocalAccessor(ITodoListAccessor localAccessor) {
		this.localAccessor = localAccessor;
	}

	
}
