package com.example.globalplanner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private int ddayOfMonth;
    private int mmonth;
    private int yyear;
    private int startingTime;
    private String type;
    private static final String FILE_NAME = "appointment.txt";

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://witwerts.com/globalplanner/api/")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_date);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }

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
        yyear = 0;
        mmonth = 0;
        ddayOfMonth = 0;
        startingTime = 0;


        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = dayOfMonth + "/" + month + "/" + year;
                ddayOfMonth = dayOfMonth;
                mmonth = month;
                yyear = year;
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, date + " " + startTimeText.getText() +  " " + lengthTimeText.getText() + " " + selectedType);
                startingTime = Integer.parseInt(startTimeText.getText().toString());
                type = selectedType;
                saveTextAsFile();
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


    public void saveTextAsFile() {
        String fileName = "Datum.txt";
        String content = ddayOfMonth + "/" + mmonth + "/" + yyear + "/" + startingTime + "/" + Integer.parseInt(lengthTimeText.getText().toString()) + " " + type;
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
            Log.i("opgeslagen in", getFilesDir().toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onLogoutClick() {
        Intent intent = new Intent(MakeDate.this, MainActivity.class);
        startActivity(intent);
    }

    private void onGoBackClick() {
        Intent intent = new Intent(MakeDate.this, CalendarActivity.class);
        intent.putExtra("welcomeMailKey", welcomeMailString);
        intent.putExtra("jwtKey", jwt);
        if(yyear != 0) {
            intent.putExtra("dayOfMonth", ddayOfMonth);
            intent.putExtra("month", mmonth);
            intent.putExtra("year", yyear);
            intent.putExtra("startingTime", startingTime);
            intent.putExtra("type", type);
            intent.putExtra("length", Integer.parseInt(lengthTimeText.getText().toString()));
            postTheAppointment();
        }
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

    private void postTheAppointment() {
        String responseJWT = "JWT " + jwt;
        String startTimme = Integer.toString(startingTime).substring(0, 2);
        String startTimmeMinute = Integer.toString(startingTime).substring(2,4);
        int startTimmee = Integer.parseInt(startTimme);
        int startTimmeeMinute = Integer.parseInt(startTimmeMinute);
        Log.i("unixyear", Integer.toString(yyear));
        Log.i("unixmonth", Integer.toString(mmonth + 1));
        Log.i("unixday", Integer.toString(ddayOfMonth));
        Log.i("unixhour", Integer.toString(startTimmee));
        Log.i("unixminute", Integer.toString(startTimmeeMinute));
        Date aDate = new Date(yyear, mmonth + 1, ddayOfMonth, startTimmee, startTimmeeMinute);
        long start_time = aDate.getTime() / 1000L;
        Log.i("unix", Long.toString(start_time));

        AppointmentInfo appointmentInfo = new AppointmentInfo(start_time, 1);
        Call<ResponseBody> call = userClient.postAppointment(responseJWT, appointmentInfo);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i("response", response.body().toString());
                }
                else {
                    Log.i("response", "Niet gelukt om afpsraak door te sturen");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MakeDate.this, "Verkeerde login.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}