package com.example.econtact;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.loginMainActivity);
        registerButton = findViewById(R.id.registerMainActivity);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this, Login.class);

                //DZIALA
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(2020, 11, 24);
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, "Yoga")
                        .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                        .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
                startActivity(intent);

                //startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterAs.class));
            }
        });
    }
}

