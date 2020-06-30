package com.example.globalplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalendarActivity extends AppCompatActivity {

    private Button logout;
    private Button makeDate;
    private TextView welcomeEmail;
    private String welcomeMailString;
    private String jwt;
    private static final String TAG = "tester";


    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://witwerts.com/globalplanner/api/")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        welcomeMailString = intent.getStringExtra("welcomeMailKey");
        jwt = intent.getStringExtra("jwtKey");



        Log.d("tag JWT", "" + jwt);
        Log.i("tag mail", welcomeMailString);

        getTypes();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        welcomeEmail = (TextView)findViewById(R.id.welcomeEmail);
        welcomeEmail.setText(welcomeMailString);
        logout = (Button)findViewById(R.id.logoutButton);
        makeDate = (Button)findViewById(R.id.makeDate);

        Date today = new Date();
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        CalendarPickerView datePicker = findViewById(R.id.calendar);
        datePicker.init(today, nextYear.getTime()).withSelectedDate(today);

        datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                //String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date);

                Calendar calSelected = Calendar.getInstance();
                calSelected.setTime(date);

                String selectedDate = "" + calSelected.get(Calendar.DAY_OF_MONTH)
                        + " " + (calSelected.get(Calendar.MONTH) + 1)
                        + " " + calSelected.get(Calendar.YEAR);

                //Toast.makeText(CalendarActivity.this, selectedDate, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutClick();
            }
        });

        makeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMakeDateClick();
            }
        });


    }

    private void getTypes() {
        String responseJWT = "JWT " + jwt;
        Call<ResponseBody> call = userClient.getTypes(responseJWT);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Log.i(TAG, response.body().toString());
                }
                else {
                    Log.i(TAG, "nope");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void onLogoutClick() {
        Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void onMakeDateClick() {
        Intent intent = new Intent(CalendarActivity.this, MakeDate.class);
        intent.putExtra("welcomeMailKey", welcomeMailString);
        intent.putExtra("jwtKey", jwt);
        startActivity(intent);
    }

}