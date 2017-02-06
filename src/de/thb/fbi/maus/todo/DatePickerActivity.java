package de.thb.fbi.maus.todo;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class DatePickerActivity extends Activity {
	protected static final String logger = DatePickerActivity.class.getName();
	DatePicker picker;
	GregorianCalendar cal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datepicker);
		
		picker = (DatePicker) findViewById(R.id.picker);
		cal = new GregorianCalendar(); //FIXME : not null
		cal = (GregorianCalendar) getIntent().getExtras().get(TodoDetailsActivity.ARG_DATE);
		// das Textfeld zur textuellen Darstellung des Datums
		
		final TextView dateAsText = (TextView) findViewById(R.id.dateAsText);
		dateAsText.setText(cal.toString());
		
		picker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),  cal.get(Calendar.DAY_OF_MONTH));
		picker.init(picker.getYear(), picker.getMonth(), picker.getDayOfMonth()
				,new DatePicker.OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						// initialisiere das Datum, berücksichtige die
						// Unterschiede bei den Konventionen für die
						// Repräsentation von Jahr / Monat / Tag als
						// Integer-Werte
						
						// setze das Datum auf dem Textfeld
						cal.set(Calendar.YEAR, year);
						cal.set(Calendar.MONTH, monthOfYear);
						cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						String pressed = String.valueOf(dayOfMonth)+ String.valueOf(monthOfYear)+ String.valueOf(year);
						dateAsText.setText(cal.toString());
						Toast.makeText(DatePickerActivity.this
								, "date"+pressed
								, Toast.LENGTH_SHORT).show();

						processDateChanged();

					}
				});

	} // end onCreate

	@Override
	public void onBackPressed() {
		// super.onBackPressed(); //FIXME self : override only if commented ...
		// wtf
		Intent returnIntent = new Intent();
		returnIntent.putExtra(TodoDetailsActivity.ARG_DATE, this.cal);
		super.setResult(TodoDetailsActivity.RESPONSE_DATE_EDITED, returnIntent);
		finish();
	}

	protected void processDateChanged() {
		picker.setVisibility(DatePicker.INVISIBLE);
		TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
		timePicker.setCurrentHour(10); // FIXME: our api is .. deprekaked
		timePicker.setCurrentMinute(10);
		timePicker.setVisibility(TimePicker.VISIBLE);
		Toast.makeText(this, "Back, wenn fertig", Toast.LENGTH_SHORT).show();
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				cal.set(Calendar.HOUR, hourOfDay);
				cal.set(Calendar.MINUTE, minute);
			}
		});
	}

}
