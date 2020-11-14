package com.example.econtact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterAs extends AppCompatActivity {
    Button studentRegister, teacherRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as);
        studentRegister = findViewById(R.id.studentButton_registerAs);
        teacherRegister = findViewById(R.id.teacherButton_registerAs);

        studentRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterAs.this, RegisterStudent.class));
            }
        });

        teacherRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterAs.this, RegisterTeacher.class));
            }
        });
    }
}