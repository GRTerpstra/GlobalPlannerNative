package com.example.globalplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.MessagePattern;
import android.icu.text.UnicodeSetSpanner;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonParser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText editText_login_username;
    private EditText getEditText_login_password;
    private Button button_login_login;
    private String welcomeMailString;
    private static final String TAG = "tester";
    public static final String EXTRA_MAIL = "com.example.globalplanner.MainActivity.EXTRA_MAIL";

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://witwerts.com/globalplanner/api/")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_login_username = (EditText) findViewById(R.id.editText_login_username);
        getEditText_login_password = (EditText) findViewById(R.id.editText_login_password);
        button_login_login = (Button) findViewById(R.id.button_login_login);

        button_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });
    }

    private static String jwt;

    private void login() {

        Login login = new Login(editText_login_username.getText().toString(), getEditText_login_password.getText().toString());

        Call<User> call = userClient.login(login);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    jwt = response.body().getData().getJwt();
                    Log.i(TAG, response.body().getData().getJwt());
                    welcomeMailString = editText_login_username.getText().toString();
                    onLoginClick();

                }
                else {
                    Toast.makeText(MainActivity.this, "Verkeerde login gegevens", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Verkeerde login.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onLoginClick() {
        Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
        intent.putExtra("jwtKey", jwt);
        intent.putExtra("welcomeMailKey", welcomeMailString);
        startActivity(intent);
    }


}
