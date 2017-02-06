package de.thb.fbi.maus.todo.tasks;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import de.thb.fbi.maus.todo.TestActivity;
import de.thb.fbi.maus.todo.TodoListActivity;
import de.thb.fbi.maus.todo.accessors.api.ITodoListAccessor;

public class PingWebAppTask extends AsyncTask<String, Void, Boolean> {
	Activity theCallingActivity;
	private boolean isServerReachable = false;
	private ProgressDialog dialog = null;
	private String hostName;
	private int hostPort;

	public PingWebAppTask(Activity aActivity, ProgressDialog dialog, String hostName, int hostPort) {
		// super();
		this.theCallingActivity = aActivity; 
		this.dialog = dialog;
		this.hostName = hostName;
		this.hostPort = hostPort;
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		dialog.setMessage("Verbinden... : ");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		setServerReachable(connectSocket());
		return isServerReachable();
	}

	@Override
	public void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		TestActivity.serverReached = result; //FIXME : tight coupling
		if (!isServerReachable()) {
			Intent intent = new Intent(theCallingActivity, TodoListActivity.class);
			intent.putExtra(TodoListActivity.ACCESSOR_ARGUMENT_NAME, ITodoListAccessor.class.getName());
			theCallingActivity.startActivity(intent);
			theCallingActivity.finish();
		}

		dialog.cancel();
	}

	/**
	 * 
	 * @return isConnected
	 */
	private boolean connectSocket() {
		Socket sock = null;
		boolean isReachable = false;
		try {
			sock = new Socket();
			sock.connect(new InetSocketAddress(hostName, hostPort), 5 * 1000); // FIXME:
																				// hard
																				// coded
			isReachable = sock.isConnected();
		} catch (Exception e) {
		} finally {
			try {
				sock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return isReachable;
	}

	public boolean isServerReachable() {
		return isServerReachable;
	}

	public void setServerReachable(boolean isServerReachable) {
		this.isServerReachable = isServerReachable;
	}

}
