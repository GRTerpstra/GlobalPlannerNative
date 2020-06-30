package com.example.globalplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MakeDate extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button logout;
    private Button goBack;
    private Button done;
    private EditText startTimeText;
    private EditText lengthTimeText;
    private CalendarView mCalendarView;
    public String date;
    public int startTime;
    public int lengthTime;
    private static final String TAG = "test123";
    public String selectedType;
    private String welcomeMailString;
    private String jwt;
    private TextView welcomeEmail2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_date);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            welcomeMailString = extras.getString("welcomeMailKey");
            jwt = extras.getString("jwtKey");
        }

        Spinner spinner = findViewById(R.id.listOfAppointments);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.appointmentTypeList, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        mCalendarView = (CalendarView) findViewById(R.id.calendarView2);
        logout = (Button) findViewById(R.id.logoutButton2);
        goBack = (Button) findViewById(R.id.goBack);
        startTimeText = (EditText) findViewById(R.id.timeText);
        lengthTimeText = (EditText) findViewById(R.id.lengthText);
        done = (Button) findViewById(R.id.makeAppointment);
        welcomeEmail2 = (TextView)findViewById(R.id.welcomeEmail2);
        welcomeEmail2.setText(welcomeMailString);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = dayOfMonth + "/" + month + "/" + year;
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, date + " " + startTimeText.getText() +  " " + lengthTimeText.getText() + " " + selectedType);

                onGoBackClick();
            }
        });




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutClick();
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoBackClick();
            }
        });

    }

    private void onLogoutClick() {
        Intent intent = new Intent(MakeDate.this, MainActivity.class);
        startActivity(intent);
    }

    private void onGoBackClick() {
        Intent intent = new Intent(MakeDate.this, CalendarActivity.class);
        intent.putExtra("welcomeMailKey", welcomeMailString);
        intent.putExtra("jwtKey", jwt);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedType = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), selectedType, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}