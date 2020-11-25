package com.example.econtact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddNewTicketStepTwo extends AppCompatActivity {

    TextView nameLecturer, surnameLecturer, fieldLecturer, facultyLecturer, emailLecturer;
    Button nextStep, previousStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_ticket_step_two);

        //Set ID for TextViews and Buttons
        nameLecturer = findViewById(R.id.nameTeacher_addNewTicketStepTwo);
        surnameLecturer = findViewById(R.id.surnameTeacher_addNewTicketStepTwo);
        fieldLecturer = findViewById(R.id.fieldTeacher_addNewTicketStepTwo);
        facultyLecturer = findViewById(R.id.facultyTeacher_addNewTicketStepTwo);
        emailLecturer = findViewById(R.id.emailTeacher_addNewTicketStepTwo);
        nextStep = findViewById(R.id.nextStep_addNewTicketStepTwo);
        previousStep = findViewById(R.id.previousStep_addNewTicketStepTwo);

        //Download data of previous activity and set in Textviews
        nameLecturer.setText(getIntent().getStringExtra("nameTeacher"));
        surnameLecturer.setText(getIntent().getStringExtra("surnameTeacher"));
        facultyLecturer.setText(getIntent().getStringExtra("facultyTeacher"));
        fieldLecturer.setText(getIntent().getStringExtra("fieldTeacher"));
        emailLecturer.setText(getIntent().getStringExtra("emailTeacher"));

        //"Next Step" Button Action
        //Transfer user data (lecturer and Student) to next activity
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stepThree = new Intent(AddNewTicketStepTwo.this, AddNewTicketStepThree.class);
                //Send to next Activity data about Lector
                stepThree.putExtra("nameTeacher", getIntent().getStringExtra("nameTeacher"));
                stepThree.putExtra("surnameTeacher", getIntent().getStringExtra("surnameTeacher"));
                stepThree.putExtra("facultyTeacher", getIntent().getStringExtra("facultyTeacher"));
                stepThree.putExtra("fieldTeacher", getIntent().getStringExtra("fieldTeacher"));
                stepThree.putExtra("emailTeacher", getIntent().getStringExtra("emailTeacher"));

                //Send to next Activity data about student user
                stepThree.putExtra("nameStudent", getIntent().getStringExtra("nameStudent"));
                stepThree.putExtra("surnameStudent", getIntent().getStringExtra("surnameStudent"));
                stepThree.putExtra("facultyStudent", getIntent().getStringExtra("facultyStudent"));
                stepThree.putExtra("fieldStudent", getIntent().getStringExtra("fieldStudent"));
                stepThree.putExtra("degreeStudent", getIntent().getStringExtra("degreeStudent"));
                stepThree.putExtra("semesterStudent", getIntent().getStringExtra("semesterStudent"));
                stepThree.putExtra("indexNumberStudent", getIntent().getStringExtra("indexNumberStudent"));
                stepThree.putExtra("Email", getIntent().getStringExtra("Email"));
                //Start a new Activity - AddNewTicketStepThree
                startActivity(stepThree);
            }
        });

        //Back to previous activity - AddNewTicketStepOne
        previousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewTicketStepTwo.this, AddNewTicketStepOne.class));
            }
        });
    }
}