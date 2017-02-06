package de.thb.fbi.maus.todo.accessors.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import de.thb.fbi.maus.todo.model.TodoUser;

public class SQLiteTodoUserDBHelper {

	private SQLiteDatabase db;

	public SQLiteTodoUserDBHelper(SQLiteDatabase db) {
		setDb(db);
	}


	protected static final String TABNAME_TODOUSER = "todouser";
	protected static final String COL_ID = "_id";
	protected static final String COL_EMAIL = "email";
	protected static final String COL_PASSWORD = "password";
	private static final String TABLE_CREATION_QUERY_TODOUSER = "CREATE TABLE " 
	+ TABNAME_TODOUSER 
	+ " (" 	+ COL_ID 
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,\n"
			+ COL_EMAIL
			+ " TEXT ,\n" 
			+ COL_PASSWORD 
			+ " TEXT);";

	private static final String WHERE_IDENTIFY_ITEM = COL_ID + "=?";

	public String getTableCreationQuery() {
		return TABLE_CREATION_QUERY_TODOUSER;
	}

	public static ContentValues createDBDataItem(TodoUser todoUser) {
		ContentValues insertItem = new ContentValues();
		insertItem.put(COL_EMAIL, todoUser.getEmail());
		insertItem.put(COL_PASSWORD, todoUser.getPassword());

		return insertItem;
	}

	public Cursor getCursor() {
		// we make a query, which possibly will return an empty list
		SQLiteQueryBuilder querybuilder = new SQLiteQueryBuilder();
		querybuilder.setTables(TABNAME_TODOUSER);
		// we specify all columns
		String[] asColumsToReturn = { COL_ID, COL_EMAIL, COL_PASSWORD };
		// we specify an ordering
		String ordering = COL_ID + " ASC";

		Cursor c = querybuilder.query(this.db, asColumsToReturn, null, null, null, null, ordering);
		return c;
	}

	public TodoUser createItemFromCursor(Cursor c) {
		// create the item
		TodoUser currentTodoUser = new TodoUser();

		// then populate the item with the results from the cursor
		currentTodoUser.setId((int)c.getLong(c.getColumnIndex(COL_ID))); // TODO: dubious cast
		currentTodoUser.setEmail(c.getString(c.getColumnIndex(COL_EMAIL)));
		currentTodoUser.setPassword(c.getString(c.getColumnIndex(COL_PASSWORD)));

		return currentTodoUser;
	}

	public void addItemToDb(TodoUser todoUser) {

		/**
		 * add the item to the db
		 */
		ContentValues insertItem = SQLiteTodoUserDBHelper.createDBDataItem(todoUser);
		long rowId = this.db.insert(TABNAME_TODOUSER, null, insertItem);

		todoUser.setId((int)rowId);// TODO: dubious cast
	}

	public void removeItemFromDb(TodoUser todoUser) {

		// we first delete the item
		this.db.delete(TABNAME_TODOUSER, WHERE_IDENTIFY_ITEM, new String[] { String.valueOf(todoUser.getId()) });
	}

	public void updateItemInDb(TodoUser todoUser) {

		// do the update in the db
		this.db.update(TABNAME_TODOUSER, createDBDataItem(todoUser), WHERE_IDENTIFY_ITEM,
				new String[] { String.valueOf(todoUser.getId()) });
	}

	/*
	 * std
	 */
	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}
}
