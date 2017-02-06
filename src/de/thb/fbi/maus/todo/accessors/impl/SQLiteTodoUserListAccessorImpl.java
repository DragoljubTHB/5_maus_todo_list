package de.thb.fbi.maus.todo.accessors.impl;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import de.thb.fbi.maus.todo.accessors.api.ITodoUserListAccessor;
import de.thb.fbi.maus.todo.model.TodoUser;

public class SQLiteTodoUserListAccessorImpl extends AbstractActivityDataAccessor implements ITodoUserListAccessor {

	private List<TodoUser> todoUsers = new ArrayList<TodoUser>();

	private ArrayAdapter<TodoUser> adapter;
	private SQLiteDBHelper helper;

	@Override
	public ListAdapter getAdapter() {
		init();

		readOutItemsFromDatabase();

		this.adapter = new ArrayAdapter<TodoUser>(getActivity(), de.thb.fbi.maus.todo.R.layout.item_in_listview3,
				this.todoUsers) {

			@Override
			public View getView(int position, View listItemView, ViewGroup parent) {

				View layout = listItemView != null ? listItemView
						: getActivity().getLayoutInflater().inflate(de.thb.fbi.maus.todo.R.layout.item_in_listview3,
								null);
				TextView itemView = (TextView) layout.findViewById(de.thb.fbi.maus.todo.R.id.itemName);
				itemView.setText(todoUsers.get(position).getEmail());

				return layout;
			}

		};
		this.adapter.setNotifyOnChange(true);
		return this.adapter;
	}
	public void init(){
		helper = new SQLiteDBHelper(getActivity());
		helper.prepareSQLiteDatabase();
	}
	
	@Override
	public List<TodoUser> readAll() {
		readOutItemsFromDatabase();
		return todoUsers;
	}
	
	private void readOutItemsFromDatabase() {
		Cursor c = helper.getUserHelper().getCursor();
		c.moveToFirst();
		while (!c.isAfterLast()) {
			this.todoUsers.add(helper.getUserHelper().createItemFromCursor(c));
			c.moveToNext();
		}

	}

	@Override
	public TodoUser getSelectedItem(int position, long id) {
		return adapter.getItem(position);
	}

	@Override
	public void updateItem(TodoUser item) {
		helper.getUserHelper().updateItemInDb(item);
		lookupItem(item).updateFrom(item);
		if (this.adapter != null){
			this.adapter.notifyDataSetChanged();// refresh the view			
		}
	}

	@Override
	public void deleteItem(TodoUser item) {
		helper.getUserHelper().removeItemFromDb(item);
		if (this.adapter != null){
			adapter.remove(lookupItem(item));			
		}
	}

	private TodoUser lookupItem(TodoUser item) {
		for (TodoUser current : this.todoUsers) {
			if (current.getId() == item.getId()) {
				return current;
			}
		}
		return null;
	}

	@Override
	public void addItem(TodoUser item) {
		helper.getUserHelper().addItemToDb(item);
		if (this.adapter != null){
			this.adapter.add(item);			
		}
	}

	@Override
	public void close() {
		helper.close();

	}
	

	/*
	 * std
	 */

	public List<TodoUser> getTodoUsers() {
		return todoUsers;
	}

	public void setTodoUsers(List<TodoUser> todos) {
		this.todoUsers = todos;
	}

	

}
