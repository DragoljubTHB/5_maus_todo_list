package de.thb.fbi.maus.todo.accessors.impl;

import android.app.Activity;

/**
 * allows access to the activity object for its subclasses
 * 
 * @author Joern Kreutel
 *
 */
public abstract class AbstractActivityDataAccessor {

	private Activity activity;

	protected Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
}
