package de.thb.fbi.maus.todo.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import de.thb.fbi.maus.todo.TodoListActivity;
import de.thb.fbi.maus.todo.accessors.api.TodoCRUDAccessor;
import de.thb.fbi.maus.todo.accessors.api.TodoUserCRUDAccessor;
import de.thb.fbi.maus.todo.model.TodoUser;

public class LoginWebApp extends AsyncTask<TodoUserCRUDAccessor, Void, TodoUser>{

	private String email, password;
	private Activity theCallingActivity;
	private ProgressDialog progressDialog;
	
	
	public LoginWebApp(Activity aActivity, ProgressDialog progressDialog,String email, String password) {
		super();
		this.email = email;
		this.password = password;
		this.theCallingActivity = aActivity;
		this.progressDialog = progressDialog;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog.setMessage("Logging in...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.show();
	}

	@Override
	protected TodoUser doInBackground(TodoUserCRUDAccessor... params) {
		return params[0].updateItem(new TodoUser(email, password));
	}
	
	@Override
	protected void onPostExecute(TodoUser result) {
		super.onPostExecute(result);
		progressDialog.cancel();
		 if (result != null) // FIXME: extra && isServerReachable()
		 {
		 Intent intent = new Intent(theCallingActivity,
		 TodoListActivity.class);
		 intent.putExtra(TodoListActivity.ACCESSOR_ARGUMENT_NAME,
		 TodoCRUDAccessor.class.getName());
		 theCallingActivity.startActivity(intent);
		 theCallingActivity.finish();
		 }
		 else
		 {
			 AlertDialog.Builder builder = new AlertDialog.Builder(theCallingActivity);
			 builder.setMessage("Hello Hannah ");
			 builder.setCancelable(true);
			 builder.setPositiveButton("Retry", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					
				}
			});
			 builder.setNegativeButton("Quit", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					theCallingActivity.finish();
					
				}
			});
	
			 
			 AlertDialog alert = builder.create();
			 alert.setCanceledOnTouchOutside(true);
			 alert.show();
		 }
		 
	}
	
	public Activity getTheCallingActivity() {
		return theCallingActivity;
	}
	
	/*
	 * std
	 */

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
