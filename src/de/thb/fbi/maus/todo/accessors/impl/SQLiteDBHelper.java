package de.thb.fbi.maus.todo.accessors.impl;

import java.util.Locale;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDBHelper {
	private final Activity mActivity;

	private SQLiteTodoDBHelper todoHelper;
	private SQLiteTodoUserDBHelper userHelper;

	/**
	 * the db name
	 */
	private static final String DBNAME = "todo_db_01_01.db";

	/**
	 * the initial version of the db based on which we decide whether to create
	 * the table or not
	 */
	private static final int INITIAL_DBVERSION = 0;

	/**
	 * the database
	 */
	SQLiteDatabase db;

	public SQLiteDBHelper(Activity aActivity) {
		super();
		mActivity = aActivity;
		
	}

	/**
	 * prepare the database
	 */
	protected void prepareSQLiteDatabase() {

		this.db = getActivity().openOrCreateDatabase(DBNAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
		todoHelper = new SQLiteTodoDBHelper(db);
		userHelper = new SQLiteTodoUserDBHelper(db);

		// we need to check whether it is empty or not...
		if (this.db.getVersion() == INITIAL_DBVERSION) {
			db.setLocale(Locale.getDefault());
			db.setVersion(INITIAL_DBVERSION + 1);
			db.execSQL(todoHelper.getTableCreationQuery());
			db.execSQL(userHelper.getTableCreationQuery());

		}

	}

	public void close() {
		this.db.close();
	}

	/*
	 * std
	 */
	private Activity getActivity() {
		return mActivity;
	}

	public SQLiteTodoDBHelper getTodoHelper() {
		return todoHelper;
	}

	public void setTodoHelper(SQLiteTodoDBHelper todoHelper) {
		this.todoHelper = todoHelper;
	}

	public SQLiteTodoUserDBHelper getUserHelper() {
		return userHelper;
	}

	public void setUserHelper(SQLiteTodoUserDBHelper userHelper) {
		this.userHelper = userHelper;
	}

}
