package com.sdp.movemeet;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Backend.BackendActivityManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UploadActivityActivity extends AppCompatActivity {

    private Spinner spinner;

    private Calendar calendar;
    private EditText startTimeText;
    private EditText dateText;
    private int year, month, day;

    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText nParticipantsEditText;


    private EditText durationText;
    private int hours = 0;
    private int minutes = 0;

    private EditText addressText;
    private double latitude = 0;
    private double longitude = 0;

    public boolean validParticipants, validDate, validStartTime, validLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_activity);

        setupSportSpinner(this);

        titleEditText = findViewById(R.id.editTextTitle);
        descriptionEditText = findViewById(R.id.editTextDescription);
        nParticipantsEditText = findViewById(R.id.editTextNParticipants);
        validParticipants = false;

        setupDateInput(this);
        validDate = false;

        startTimeText = findViewById(R.id.editTextStartTime);
        validStartTime = false;

        durationText = findViewById(R.id.editTextTime);

        addressText = findViewById(R.id.editTextLocation);
        validLocation = false;

    }

    private void setupSportSpinner(Context context) {
        spinner = (Spinner) findViewById(R.id.spinnerSportType);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_dropdown_item, Sport.values());

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }


    // Helper methods for date picker

    private void setupDateInput(Context context) {
        dateText = findViewById(R.id.editTextDate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month + 1);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            showDate(year, month + 1, dayOfMonth);
        }
    };

    private void showDate(int year, int month, int day) {
        dateText.setText(day + "." + month + "." + year);
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }


    // Helper methods for start time picker
    private TimePickerDialog.OnTimeSetListener startTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            showStartTime(hourOfDay, minute);
        }
    };

    private void showStartTime(int hours, int minutes) {
        startTimeText.setText(hours + ":" + ((minutes < 10) ? "0" + minutes : minutes));
    }

    @SuppressWarnings("deprecation")
    public void setStartTime(View view) {
        showDialog(222);
    }


    // Helper methods for duration picker
    private TimePickerDialog.OnTimeSetListener durationListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            showDuration(hourOfDay, minute);
        }
    };

    private void showDuration(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
        calendar.set(Calendar.SECOND, hours);
        calendar.set(Calendar.MILLISECOND, minutes);
        durationText.setText(hours + ":" + ((minutes < 10) ? "0" + minutes : minutes));
    }

    @SuppressWarnings("deprecation")
    public void setDuration(View view) {
        showDialog(444);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    dateListener, year, month, day);
        } else if (id == 444) {
            return new TimePickerDialog(this,
                    durationListener, hours, minutes, false);
        } else if (id == 222) {
            return new TimePickerDialog(this,
                    startTimeListener,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true);
        }
        return null;
    }


    // Helper methods for address geocoding
    private void tryLocatingAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(this);

        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) {
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();
            } else {
                throw new IOException();
            }
        } catch (IOException e) {

        }
    }


    private Activity validateActivity() {
        Sport sport = Sport.valueOf(spinner.getSelectedItem().toString());

        String title = titleEditText.getText().toString();
        if (title.isEmpty()) title = sport.name();

        String nptext = nParticipantsEditText.getText().toString();
        if (nptext.isEmpty() || Integer.parseInt(nptext) <= 0) {
            Toast.makeText(this, "Please enter a positive number of participants", Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
        int nParticipants = Integer.parseInt(nptext);
        validParticipants = true;

        String address = addressText.getText().toString();
        if (address.isEmpty()) {
            Toast.makeText(this, "Please enter a valid location", Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
        tryLocatingAddress(this, address);
        validLocation = true;

        String description = descriptionEditText.getText().toString();

        if (startTimeText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a start time", Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
        validStartTime = true;

        if (durationText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a duration", Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
        double duration = (double) (hours) + (double) (minutes) / 60;
        Date date = calendar.getTime();
        validDate = true;

        String organizerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Activity activity = new Activity(
                organizerId + " || " +  date, organizerId, title, nParticipants,
                new ArrayList<String>(), longitude, latitude, description, date, duration,
                sport, address
        );

        return activity;
    }

    public void confirmActivityUpload(View view) {
        Activity toUpload = validateActivity();

        if (toUpload == null) return;

        BackendActivityManager bam = new BackendActivityManager(FirebaseFirestore.getInstance(),
                BackendActivityManager.ACTIVITIES_COLLECTION);

        bam.uploadActivity(toUpload, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Activity successfully uploaded!",
                                Toast.LENGTH_SHORT).show();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to upload activity",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


}