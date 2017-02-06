package de.thb.fbi.maus.todo.accessors.impl;

import java.util.ArrayList;

import android.content.Intent;
import de.thb.fbi.maus.todo.TestActivity;
import de.thb.fbi.maus.todo.accessors.api.ITodoUserAccessor;
import de.thb.fbi.maus.todo.model.Todo;
import de.thb.fbi.maus.todo.model.TodoUser;

/**
 * Used as Intent
 * 
 * @author User
 *
 */
public class SQLiteTodoUserAccessorImpl extends AbstractActivityDataAccessor implements ITodoUserAccessor {

	private TodoUser item;

	@Override
	public boolean hasItem() {
		return readItem() != null;
	}

	@Override
	public TodoUser readItem() {
		if (this.item == null) {
			this.item = (TodoUser) getActivity().getIntent().getSerializableExtra(TestActivity.ARG_ITEM_OBJECT);
		}

		return this.item;
	}

	@Override
	public void createItem() {

		this.item = new TodoUser(-1, "", "", new ArrayList<Todo>());
	}

	@Override
	public void writeItem() {
		// and return to the calling activity
		Intent returnIntent = new Intent();
		
		// set the item
		returnIntent.putExtra(TestActivity.ARG_ITEM_OBJECT, this.item);

		// set the result code
		getActivity().setResult(TestActivity.RESPONSE_ITEM_EDITED, returnIntent);

	}

	@Override
	public void deleteItem() {
		// and return to the calling activity
		Intent returnIntent = new Intent();

		// set the item
		returnIntent.putExtra(TestActivity.ARG_ITEM_OBJECT, this.item);

		// set the result code
		getActivity().setResult(TestActivity.RESPONSE_ITEM_DELETED, returnIntent);

	}

}
