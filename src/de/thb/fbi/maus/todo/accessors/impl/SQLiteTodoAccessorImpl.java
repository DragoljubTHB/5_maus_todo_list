package de.thb.fbi.maus.todo.accessors.impl;

import android.content.Intent;
import de.thb.fbi.maus.todo.TodoListActivity;
import de.thb.fbi.maus.todo.accessors.api.ITodoAccessor;
import de.thb.fbi.maus.todo.model.Todo;
import de.thb.fbi.maus.todo.model.Todo.Rang;

/**
 * Used as Intent
 * 
 * @author User, Joern Kreutel
 *
 */
public class SQLiteTodoAccessorImpl extends AbstractActivityDataAccessor implements ITodoAccessor {

	private Todo item;

	@Override
	public boolean hasItem() {
		return readItem() != null;
	}

	@Override
	public Todo readItem() {
		if (this.item == null) {
			this.item = (Todo) getActivity().getIntent().getSerializableExtra(TodoListActivity.ARG_ITEM_OBJECT);
		}

		return this.item;
	}

	@Override
	public void createItem() {

		this.item = new Todo(-1, "aName", "aDescription", "aSpecUrl", 10202017,
		30, false, Rang.F);
	}

	@Override
	public void writeItem() {
		// and return to the calling activity
		Intent returnIntent = new Intent();

		// set the item
		returnIntent.putExtra(TodoListActivity.ARG_ITEM_OBJECT, this.item);

		// set the result code
		getActivity().setResult(TodoListActivity.RESPONSE_ITEM_EDITED, returnIntent);

	}

	@Override
	public void deleteItem() {
		// and return to the calling activity
		Intent returnIntent = new Intent();

		// set the item
		returnIntent.putExtra(TodoListActivity.ARG_ITEM_OBJECT, this.item);

		// set the result code
		getActivity().setResult(TodoListActivity.RESPONSE_ITEM_DELETED, returnIntent);

	}

}
