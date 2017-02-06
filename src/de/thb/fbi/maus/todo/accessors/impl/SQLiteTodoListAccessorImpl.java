package de.thb.fbi.maus.todo.accessors.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import de.thb.fbi.maus.todo.R;
import de.thb.fbi.maus.todo.accessors.api.ITodoListAccessor;
import de.thb.fbi.maus.todo.model.Todo;

public class SQLiteTodoListAccessorImpl extends AbstractActivityDataAccessor implements ITodoListAccessor {

	private List<Todo> todos = new ArrayList<Todo>();
	

	private ArrayAdapter<Todo> adapter;
	private SQLiteDBHelper helper;

	@Override
	public ListAdapter getAdapter() {
		init();

		readOutItemsFromDatabase();

		this.adapter = new ArrayAdapter<Todo>(getActivity(), de.thb.fbi.maus.todo.R.layout.item_in_listview3,
				this.todos) {

			@Override
			public View getView(int position, View listItemView, ViewGroup parent) {

				View layout = listItemView != null ? listItemView
						: getActivity().getLayoutInflater().inflate(de.thb.fbi.maus.todo.R.layout.item_in_listview3,
								null);
				
				return setupView(layout, position);
			}
		};
		
		
		this.adapter.setNotifyOnChange(true);
		return this.adapter;
	}// end getAdapter()
	
	private View setupView(View aLayout, int aPosition) {
		View layout = aLayout;		
		final int position = aPosition;
		
		TextView itemView = (TextView) layout.findViewById(de.thb.fbi.maus.todo.R.id.itemName);
		TextView itemDueDate = (TextView) layout.findViewById(de.thb.fbi.maus.todo.R.id.itemDueDate);
		TextView itemWichtigkeit = (TextView)layout.findViewById(R.id.item_wichtigkeit);
		
		setDateTextView(itemDueDate, todos.get(position));
		
		itemView.setText(todos.get(position).getName());
		itemWichtigkeit.setText(todos.get(position).getRang().toString());
		final ImageView imageView = (ImageView) layout.findViewById(R.id.todo_image);
		imageView.setImageResource(todos.get(position).isDone() ? R.drawable.star_grey : R.drawable.star_yellow);
		
		imageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean done =todos.get(position).isDone();
				todos.get(position).setDone(!done);
				updateItem(todos.get(position));
				imageView.setImageResource(todos.get(position).isDone() ? R.drawable.star_yellow : R.drawable.star_grey);
			}
		});
		return layout;
	}
	private TextView setDateTextView(TextView itemDueDate, Todo todo) {
		GregorianCalendar currentgreg = new GregorianCalendar();
		currentgreg = (GregorianCalendar) Calendar.getInstance();
		//           wenn in vergangenheit                      und nicht fertig
		if((currentgreg.getTimeInMillis() >= todo.getDueDate()) && !todo.isDone())
		{
			itemDueDate.setTypeface(itemDueDate.getTypeface(), Typeface.BOLD_ITALIC);
			itemDueDate.setTextColor(Color.parseColor("red"));
		}
		else 
		{
			itemDueDate.setTypeface(itemDueDate.getTypeface(), Typeface.BOLD);
			itemDueDate.setTextColor(Color.parseColor("black"));
		}
		currentgreg.setTimeInMillis(todo.getDueDate());
		
		itemDueDate.setText(currentgreg.getTime().toString());
		
		return itemDueDate;
		
	}

	
	@Override
	public void sortBy(String aSortCriterium) {
		if(aSortCriterium.equals("date")){
			Collections.sort(this.todos, getDateRangComparator());
		} else {
			Collections.sort(this.todos, getRangDateComparator());
		}
		adapter.notifyDataSetChanged();
	}
	
	private Comparator<Todo> getDateRangComparator(){
		return new Comparator<Todo>() {

			@Override
			public int compare(Todo o1, Todo o2) {
				int res = 0;
				if(o1.getDueDate() == o2.getDueDate()) // gleich ? dann rang gucken
				{
					res = o1.getRang().compareTo(o2.getRang());
				}
				res = (o1.getDueDate() > o2.getDueDate() ? 1 : -1);
						
				return res;
			}
		};
	
	}
	private Comparator<Todo> getRangDateComparator(){
		return new Comparator<Todo>() {

			@Override
			public int compare(Todo o1, Todo o2) {
				int res =  o1.getRang().compareTo(o2.getRang());
				if(res == 0)
				{
					res = (o1.getDueDate() > o2.getDueDate() ? 1 : -1);
				}
				return res;
			}
		};
	
	}
	
	@Override
	public void init() {
		helper = new SQLiteDBHelper(getActivity());
		helper.prepareSQLiteDatabase();
	}
	@Override
	public List<Todo> readAll() {
		readOutItemsFromDatabase();
		return this.todos;

	}

	private void readOutItemsFromDatabase() {
		Cursor c = helper.getTodoHelper().getCursor();
		c.moveToFirst();
		while (!c.isAfterLast()) {
			this.todos.add(helper.getTodoHelper().createItemFromCursor(c));
			c.moveToNext();
		}

	}

	@Override
	public Todo getSelectedItem(int position, long id) {
		return adapter.getItem(position);
	}

	@Override
	public void updateItem(Todo item) {
		helper.getTodoHelper().updateItemInDb(item);
		lookupItem(item).updateFrom(item);
		this.adapter.notifyDataSetChanged();// refresh the view

	}

	@Override
	public void deleteItem(Todo item) {
		helper.getTodoHelper().removeItemFromDb(item);
		adapter.remove(lookupItem(item));

	}

	private Todo lookupItem(Todo item) {
		for (Todo current : this.todos) {
			if (current.getId() == item.getId()) {
				return current;
			}
		}
		return null;
	}

	@Override
	public void addItem(Todo item) {
		helper.getTodoHelper().addItemToDb(item);
		this.adapter.add(item);

	}

	@Override
	public void close() {
		helper.close();

	}

	/*
	 * std
	 */
	@Override
	public List<Todo> getTodos() {
		return todos;
	}

	public void setTodos(List<Todo> todos) {
		this.todos = todos;
	}



}
