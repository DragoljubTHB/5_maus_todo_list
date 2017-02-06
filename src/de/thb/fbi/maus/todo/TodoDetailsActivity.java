package de.thb.fbi.maus.todo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import de.thb.fbi.maus.todo.accessors.api.ITodoAccessor;
import de.thb.fbi.maus.todo.accessors.impl.AbstractActivityDataAccessor;
import de.thb.fbi.maus.todo.accessors.impl.SQLiteTodoAccessorImpl;
import de.thb.fbi.maus.todo.model.Todo;
import de.thb.fbi.maus.todo.model.Todo.Rang;

public class TodoDetailsActivity extends Activity {

	private static final int REQUEST_DATE = 2;
	public static final String ARG_DATE = "argDate";

	public static final int RESPONSE_DATE_EDITED = 3;

	Calendar cal; // FIXME: scope
	TextView dueDateTextView;

	private ITodoAccessor accessor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemview);

		final ImageView isDoneStar = (ImageView) findViewById(R.id.isDone_image);
		final EditText itemName = (EditText) findViewById(R.id.item_name);
		final EditText itemDescription = (EditText) findViewById(R.id.item_description);
		dueDateTextView = (TextView) findViewById(R.id.item_dueDate);
		Button saveButton = (Button) findViewById(R.id.saveButton);
		Button deleteButton = (Button) findViewById(R.id.deleteButton);
		Spinner spinnerRang = (Spinner) findViewById(R.id.itemview_spinner_rang);

		List<Rang> rangs = new ArrayList<Rang>(Arrays.asList(Todo.Rang.values()));

		ArrayAdapter<Rang> spinnerAdapter = new ArrayAdapter<Rang>(this, R.layout.support_simple_spinner_dropdown_item,
				rangs); // FIXME : RANG !!!
		spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
		spinnerRang.setAdapter(spinnerAdapter);

		spinnerRang.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				String selectedItem = ((TextView) selectedItemView).getText().toString();
				accessor.readItem().setRang(Rang.valueOf(selectedItem));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		accessor = new SQLiteTodoAccessorImpl();
		((AbstractActivityDataAccessor) accessor).setActivity(this);
		if (accessor.hasItem()) {
			// set name and description
			itemName.setText(accessor.readItem().getName());
			itemDescription.setText(accessor.readItem().getDescription());
			isDoneStar.setImageResource(accessor.readItem().isDone() ? R.drawable.star_grey : R.drawable.star_yellow);
		} else {
			accessor.createItem();
			isDoneStar.setImageResource(R.drawable.star_yellow);
		}

		isDoneStar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				accessor.readItem().setDone(!accessor.readItem().isDone());
				Toast.makeText(TodoDetailsActivity.this, accessor.readItem().isDone() ? "erlaedischt" : "todo!",
						Toast.LENGTH_SHORT).show();
				isDoneStar
						.setImageResource(accessor.readItem().isDone() ? R.drawable.star_grey : R.drawable.star_yellow);
			}
		});

		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				processItemSave(accessor, itemName, itemDescription);
			}
		});
		deleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(TodoDetailsActivity.this);
				builder.setMessage("Loeschen?");
				builder.setCancelable(true);
				builder.setPositiveButton("Jö", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						processItemDelete(accessor);
						finish();
					}
				});
				builder.setNegativeButton("Nop", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();

					}
				});
				AlertDialog alert = builder.create();
				alert.setCanceledOnTouchOutside(true);
				alert.show();

			}
		});
		GregorianCalendar greg = new GregorianCalendar();
		greg.setTimeInMillis(accessor.readItem().getDueDate());

		dueDateTextView.setText(greg.getTime().toString());
		dueDateTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				processDatePickerRequest();

			}
		});

	}// end onCreate

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (resultCode == RESPONSE_DATE_EDITED) {
				cal = (Calendar) data.getSerializableExtra(ARG_DATE);
				setDueDate(cal);
			}
		}
	}

	/**
	 * Convertierung von Calender auf DB-spezifische long Darstellung also :
	 * long
	 * 
	 * @param date
	 */
	private void setDueDate(Calendar date) {
		accessor.readItem().setDueDate(date.getTimeInMillis());
		dueDateTextView.setText(new Date(accessor.readItem().getDueDate()).toString());

	}

	protected void processDatePickerRequest() {
		Intent intent = new Intent(TodoDetailsActivity.this, DatePickerActivity.class);
		intent.putExtra(ARG_DATE, Calendar.getInstance(Locale.GERMANY));
		startActivityForResult(intent, REQUEST_DATE);
		onPause();
	}

	protected void processItemDelete(ITodoAccessor accessor) {
		accessor.deleteItem();
		finish();
	}

	protected void processItemSave(ITodoAccessor accessor, EditText itemName, EditText itemDescription) {
		// re-set the fields of the item
		accessor.readItem().setName(itemName.getText().toString());
		accessor.readItem().setDescription(itemDescription.getText().toString());

		// save the item
		accessor.writeItem();
		finish();
	}
}
