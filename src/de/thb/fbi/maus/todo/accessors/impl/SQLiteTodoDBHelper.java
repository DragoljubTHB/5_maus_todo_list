package de.thb.fbi.maus.todo.accessors.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import de.thb.fbi.maus.todo.model.Todo;
import de.thb.fbi.maus.todo.model.Todo.Rang;

public class SQLiteTodoDBHelper {
	private SQLiteDatabase db;

	public SQLiteTodoDBHelper(SQLiteDatabase db) {
		setDb(db);
	}

	protected static final String logger = SQLiteTodoDBHelper.class.getName();
	protected static final String TABNAME_TODO = "todos";

	protected static final String COL_ID = "_id";
	protected static final String COL_NAME = "name";
	protected static final String COL_DESCRIPTION = "description";
	protected static final String COL_ICONURL = "iconUrl";
	protected static final String COL_RANG = "rang";
	protected static final String COL_DUEDATE = "dueDate";
	protected static final String COL_DURATION = "durationMin";
	protected static final String COL_DONE = "done";

	/**
	 * the creation query Todo ds
	 */
	private static final String TABLE_CREATION_QUERY_TODO = "CREATE TABLE " + TABNAME_TODO + " (" + COL_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,\n" 
			+ COL_RANG + " TEXT,\n" 
			+ COL_NAME + " TEXT,\n" 
			+ COL_DESCRIPTION + " TEXT,\n" 
			+ COL_ICONURL + " TEXT,\n" 
			+ COL_DUEDATE + " TEXT,\n" 
			+ COL_DURATION + " TEXT,\n" 
			+ COL_DONE
			+ " INTEGER);";

	/**
	 * the where clause for item deletion
	 */
	private static final String WHERE_IDENTIFY_ITEM = COL_ID + "=?";

	public String getTableCreationQuery() {
		return TABLE_CREATION_QUERY_TODO;
	}

	/**
	 * create a ContentValues object which can be passed to a db query
	 * 
	 * @param todo
	 * @return ContentValues
	 */
	public static ContentValues createDBDataItem(Todo todo) {
		ContentValues insertItem = new ContentValues();
		insertItem.put(COL_NAME, todo.getName());
		insertItem.put(COL_DESCRIPTION, todo.getDescription());
		insertItem.put(COL_RANG, String.valueOf(todo.getRang()));
		insertItem.put(COL_ICONURL, "dummyURL");// FIXME:todo.getIconUrl().toString()
																	// URL.toString
		insertItem.put(COL_DUEDATE, todo.getDueDate());
		insertItem.put(COL_DURATION, todo.getDurationMin());
		insertItem.put(COL_DONE, (todo.isDone()) ? 1 : 0);
		Log.e(logger, "Content value : "+insertItem.get(COL_DONE));
		return insertItem;
	}

	public Cursor getCursor() {
		// we make a query, which possibly will return an empty list
		SQLiteQueryBuilder querybuilder = new SQLiteQueryBuilder();
		querybuilder.setTables(TABNAME_TODO);
		// we specify all columns
		String[] asColumsToReturn = 
			{ 
				COL_ID
				, COL_NAME
				, COL_DESCRIPTION
				, COL_ICONURL
				, COL_RANG
				, COL_DUEDATE
				, COL_DURATION
				, COL_DONE 
				};
		// we specify an ordering
		String ordering = COL_ID + " ASC";

		Cursor c = querybuilder.query(this.db, asColumsToReturn, null, null, null, null, ordering);
		return c;
	}

	public Todo createItemFromCursor(Cursor c) {
		// create the item
		Todo currentTodo = new Todo();

		// then populate the item with the results from the cursor
		currentTodo.setId(c.getInt(c.getColumnIndex(COL_ID)));
		currentTodo.setName(c.getString(c.getColumnIndex(COL_NAME)));
		currentTodo.setDescription(c.getString(c.getColumnIndex(COL_DESCRIPTION)));
		// TODO: iconUrl
		currentTodo.setRang(Rang.valueOf(c.getString(c.getColumnIndex(COL_RANG))));
		currentTodo.setDueDate(c.getLong(c.getColumnIndex(COL_DUEDATE)));
		currentTodo.setDurationMin(c.getInt(c.getColumnIndex(COL_DURATION)));
		currentTodo.setDone(
				(c.getString(c.getColumnIndex(COL_DONE))).equals("1") ? true : false
				);
		Log.e(logger, "created item from cursor : COL_DONE "+currentTodo.isDone());

		return currentTodo;
	}

	public void addItemToDb(Todo todo) {
		Log.i(logger, "addItemToDb(): " + todo);

		/**
		 * add the item to the db
		 */
		ContentValues insertItem = SQLiteTodoDBHelper.createDBDataItem(todo);
		long newItemId = this.db.insert(TABNAME_TODO, null, insertItem);
		Log.i(logger, "addItemToDb(): got new todo id after insertion: " + newItemId);
		todo.setId((int) newItemId);//TODO: dubious cast
	}

	public void removeItemFromDb(Todo todo) {
		Log.i(logger, "removeItemFromDb(): " + todo);

		// we first delete the item
		this.db.delete(TABNAME_TODO, WHERE_IDENTIFY_ITEM, new String[] { String.valueOf(todo.getId()) });
		Log.i(logger, "removeItemFromDb(): deletion in db done");
	}

	public void updateItemInDb(Todo todo) {
		Log.i(logger, "updateItemInDb(): " + todo);

		// do the update in the db
		this.db.update(TABNAME_TODO, createDBDataItem(todo), WHERE_IDENTIFY_ITEM,
				new String[] { String.valueOf(todo.getId()) });
		Log.i(logger, "updateItemInDb(): update has been carried out");
	}

	public void close() {
		this.db.close();
		Log.i(logger, "db has been closed");
	}

	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}

}
