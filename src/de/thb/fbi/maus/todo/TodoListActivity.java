package de.thb.fbi.maus.todo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;
import de.thb.fbi.maus.todo.accessors.api.ITodoListAccessor;
import de.thb.fbi.maus.todo.accessors.api.TodoCRUDAccessor;
import de.thb.fbi.maus.todo.accessors.impl.AbstractActivityDataAccessor;
import de.thb.fbi.maus.todo.accessors.impl.SQLiteTodoListAccessorImpl;
import de.thb.fbi.maus.todo.model.Todo;
import de.thb.fbi.maus.todo.tasks.TodoReceiver;
import de.thb.fbi.maus.todo.tasks.TodoSender;

/**
 * Die Anzeige der Todoliste soll eine Übersicht über alle Todos darstellen und
 * die Erstellung neuer Todos ermöglichen
 * 
 * @author Martin Schafföner, Dado Milasinovic
 *
 */
public class TodoListActivity extends Activity {

	protected static final String logger = TodoListActivity.class.getName();

	public static final String ACCESSOR_ARGUMENT_NAME = "accessorClass";

	public static final String ARG_ITEM_OBJECT = "itemObject";
	public static final int RESPONSE_ITEM_EDITED = 1;
	public static final int RESPONSE_ITEM_DELETED = 2;
	public static final int RESPONSE_NOCHANGE = -1;
	public static final int REQUEST_ITEM_DETAILS = 2;
	public static final int REQUEST_ITEM_CREATION = 1;

	private String actualAccessorName;
	private ITodoListAccessor accessor;
	private TodoCRUDAccessor restAccessor;

	private ListView todosView;
	private ListAdapter listAdapter;
	private boolean doubleBackToExitPressedOnce = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemlistview);
		
		todosView = (ListView) findViewById(R.id.list);
		Button newitemButton = (Button) findViewById(R.id.newitemButton);
		ToggleButton btnSorter = (ToggleButton)findViewById(R.id.toggleButton);
	        
		Log.i(logger, "got accessor args: " + getIntent().getExtras().getString(ACCESSOR_ARGUMENT_NAME));
		actualAccessorName = getIntent().getExtras().getString(ACCESSOR_ARGUMENT_NAME);
		setupAccessor(actualAccessorName);
		
		todosView.setAdapter(listAdapter);
		
		todosView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO doOnOptionClicked(); // ui
				Todo todo = accessor.getSelectedItem(position, id);
				processItemSelection(todo);
			}
		});

		newitemButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				processNewItemRequest();
			}

		});
		btnSorter.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView
					, boolean isChecked) {
				sortList(isChecked);
				
			}
		});
		
		
	}// end onCreate

	protected void sortList(boolean isChecked) {
		accessor.sortBy(isChecked ? "date" : "importance");
		
	}

	private void setupAccessor(String accessorName) {
		// Class<?> clazz = Class.forName(accessorName);
		Log.i(logger, "Local : init");
		accessor = new SQLiteTodoListAccessorImpl();
		((AbstractActivityDataAccessor) accessor).setActivity(this);
		listAdapter = accessor.getAdapter();//FIXME: auslagern? 
		
		if (accessorName.equals(TodoCRUDAccessor.class.getName())) {
			Log.i(logger, "Online : init");
			restAccessor = ((TodoApplication) getApplication()).getTodoRestAccessor();
			
			abgleich();
			
		}
	}

	/**
	 * entscheidend wann aufgerufen wird
	 * @return success : boolean
	 */
	private boolean abgleich() {
		boolean success = false;
		List<Todo> localTodos = new ArrayList<Todo>();
		Log.i(logger, "Abgleich : LocalAccessor : "+accessor);
		localTodos = accessor.getTodos();
		try {
			if (localTodos.size() != 0) { // liegen  locale todos vor
				Log.i(logger, "size local : "+localTodos.size());
				TodoSender sender = new TodoSender(accessor);
				success = sender.execute(restAccessor).get();//FIXME: NOT thread safe
				Log.i(logger, "success ? : "+success); 
			} else {
				TodoReceiver receiver = new TodoReceiver(accessor);
				localTodos = receiver.execute(restAccessor).get();
				int i = 0;
				for(Todo t : localTodos){
					accessor.addItem(t); i++;
				}
				success = i == localTodos.size();
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return success;
	}

	protected void processItemSelection(Todo todo) {
		Intent intent = new Intent(TodoListActivity.this, TodoDetailsActivity.class);
		intent.putExtra(ARG_ITEM_OBJECT, todo);
		startActivityForResult(intent, REQUEST_ITEM_DETAILS);

	}

	protected void processNewItemRequest() {
		Intent intent = new Intent(TodoListActivity.this, TodoDetailsActivity.class);
		intent.putExtra(SQLiteTodoListAccessorImpl.class.getCanonicalName(),
				SQLiteTodoListAccessorImpl.class.getCanonicalName());

		startActivityForResult(intent, REQUEST_ITEM_CREATION);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);

		Todo item = data != null ? (Todo) data.getSerializableExtra(ARG_ITEM_OBJECT) : null;
		if (requestCode == REQUEST_ITEM_DETAILS) {
			if (resultCode == RESPONSE_ITEM_EDITED) {

				this.accessor.updateItem(item);
			} else if (resultCode == RESPONSE_ITEM_DELETED) {
				this.accessor.deleteItem(item);
			}
			// this.listview.invalidate();
		} else if (requestCode == REQUEST_ITEM_CREATION && resultCode == RESPONSE_ITEM_EDITED) {

			this.accessor.addItem(item);
		}

	}// end onActivityResult

	@Override
	protected void onDestroy() {
		// super.onDestroy();
		this.accessor.close();
		super.onStop();
		
	}
	@Override
	public void onBackPressed() {
		if(doubleBackToExitPressedOnce){
			super.onBackPressed();
		}
		Toast.makeText(this, "press back to finish", Toast.LENGTH_SHORT).show();
		this.doubleBackToExitPressedOnce = true;
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
				Log.i(logger, "postDelayed : runnable : "+doubleBackToExitPressedOnce);
			}
		}, 2 * 1000);
	}//end onBackPressed
}
