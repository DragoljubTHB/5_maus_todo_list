package de.thb.fbi.maus.todo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import de.thb.fbi.maus.todo.accessors.api.TodoUserCRUDAccessor;
import de.thb.fbi.maus.todo.model.TodoUser;
import de.thb.fbi.maus.todo.tasks.LoginWebApp;
import de.thb.fbi.maus.todo.tasks.PingWebAppTask;

/**
 * @author Martin Schafföner, Joern Kreutel Dado Milasinovic
 */
public class TestActivity extends Activity {
	protected static final String logger = TestActivity.class.getName();
	private PingWebAppTask ping = null;
	private TodoUserCRUDAccessor userRestAccessor;
	public static TodoUser loggedInUser;

	public static final String ARG_ITEM_OBJECT = "todoUserObject";
	public static final int RESPONSE_ITEM_DELETED = 2;
	public static final int RESPONSE_ITEM_EDITED = 1;
	public static final int SERVER_STATUS_UP = 1;
	public static final int SERVER_STATUS_DOWN = 0;
	public static boolean serverReached = false;

	private final String EMAIL_REGEX = "^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$"; // etc..
	private final String PASSWORD_REGEX = "[0-9]{6}"; // only 6 cyphers

	public AutoCompleteTextView mEmail;
	public EditText mPassword;
	public Button btnRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);


		mEmail = (AutoCompleteTextView) findViewById(R.id.email);
		mPassword = (EditText) findViewById(R.id.password);
		btnRegister = (Button) findViewById(R.id.button_attemptLogin);
	
		mEmail.addTextChangedListener(getTextWatcher());
		mPassword.addTextChangedListener(getTextWatcher());

	
		btnRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				attemptLogin();
			
			}
		});
		isServerReachable();

	}// end onCreate


	/*
	 * Web Logic
	 */
	private boolean attemptLogin() {

		userRestAccessor = ((TodoApplication) getApplication()).getUserRestAccessor();
		LoginWebApp login = null;
			login = new LoginWebApp(this
					, new ProgressDialog(this)
					, mEmail.getText().toString()
					, mPassword.getText().toString());
			
			login.execute(userRestAccessor);

		return (loggedInUser != null);
	}

	/**
	 * FIXME Navigation in onPostExecute
	 * 
	 * @return
	 */
	private void isServerReachable() {
		Log.d(logger, "isServerReachable");
		String[] serverIpAndPort = ((TodoApplication) getApplication()).getWebAppIpAndPort().split(":");
		String hostName = serverIpAndPort[0];
		int hostPort = Integer.valueOf(serverIpAndPort[1]);

		ping = new PingWebAppTask(TestActivity.this
				, new ProgressDialog(TestActivity.this)
				, hostName
				, hostPort);

		ping.execute();
//		Log.e(logger, "isServerReachable : waits for timeout, incorrect value:  " + serverReached);
	}

	/*
	 * UI logic
	 */
	private boolean checkInput() {
		boolean isEmail = false, isPwd = false;
		isEmail = checkInputEmail(mEmail.getText().toString());
		isPwd = checkInputPassword(mPassword.getText().toString());
		if(!isEmail) 
		{
			btnRegister.setTextColor(Color.parseColor("red"));
			btnRegister.setText(R.string.email_bad);
		}
		else if(!isPwd)
		{
			btnRegister.setTextColor(Color.parseColor("red"));
			btnRegister.setText(R.string.password_bad);
		}
		else 
		{
			btnRegister.setTextColor(Color.parseColor("black"));
			btnRegister.setText(R.string.button_login);
		}
		
		btnRegister.setEnabled((isEmail && isPwd));

		return false;
	}

	private boolean checkInputEmail(String text) {
		boolean isEmail = false;
		Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
		Matcher emailMatcher = emailPattern.matcher(text);
		isEmail = emailMatcher.find();
		
		return isEmail;

	}

	private boolean checkInputPassword(String text) {
		boolean isPwd = false;
		Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);
		Matcher passwordMatcher = passwordPattern.matcher(text);
		isPwd = passwordMatcher.find() && text.length() == 6;
		return isPwd;
	}
	/*
	 * 
	 */
	private TextWatcher getTextWatcher(){
		return new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				checkInput();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		};
	}


}
