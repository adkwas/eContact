package com.example.econtact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class SecondTeacherStepOne extends AppCompatActivity {


    EditText nameTeacher, surnameTeacher;
    Button nextStep, backStep;
    TextView fieldTeacher;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String  nameSelected = "", surnameSelected = "", facultySelected = " ", emailSelected = " ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_teacher_step_one);

        fieldTeacher = findViewById(R.id.fieldTeacher_secondTeacherStepOne);
        nameTeacher = findViewById(R.id.nameTeacher_secondTeacherStepOne);
        surnameTeacher = findViewById(R.id.surnameTeacher_secondTeacherStepOne);
        nextStep = findViewById(R.id.nextStep_secondTeacherStepOne);
        backStep = findViewById(R.id.previousStep_secondTeacherStepOne);


        fieldTeacher.setText(getIntent().getStringExtra("fieldTeacher"));

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nameLecturerUser = nameTeacher.getText().toString();
                final String surnameLecturerUser = surnameTeacher.getText().toString();

                if (nameLecturerUser.isEmpty()) {
                    nameTeacher.setError("Name teacher is empty!");
                    return;
                }

                if (surnameLecturerUser.isEmpty()) {
                    surnameTeacher.setError("Surname teacher is empty!");
                    return;
                }


                firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseAuth = FirebaseAuth.getInstance();

                CollectionReference collectionReference = firebaseFirestore.collection("Users Accounts");

                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                String userCloud = document.getString("User");
                                String nameLecturerCloud = document.getString("Name");
                                String surnameLecturerCloud = document.getString("Surname");
                                String fieldLecturerCloud = document.getString("Field");

                                assert userCloud != null;
                                assert fieldLecturerCloud != null;
                                assert nameLecturerCloud != null;
                                assert surnameLecturerCloud != null;

                                if (userCloud.equals("Teacher") && fieldLecturerCloud.equals(getIntent().getStringExtra("fieldTeacher")) &&
                                        nameLecturerCloud.equals(nameLecturerUser) && surnameLecturerCloud.equals(surnameLecturerUser)) {
                                    nameSelected = document.getString("Name");
                                    surnameSelected = document.getString("Surname");
                                    facultySelected = document.getString("Faculty");
                                    emailSelected = document.getString("Email");
                                }
                            }

                            if (nameSelected.isEmpty() && surnameSelected.isEmpty() && facultySelected.isEmpty() && emailSelected.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "This lecturer is not avaiable in eContact", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intent = new Intent(SecondTeacherStepOne.this, SecondTeacherStepTwo.class);

                                intent.putExtra("id",getIntent().getStringExtra("id") );

                                intent.putExtra("nameTeacher",getIntent().getStringExtra("nameTeacher"));
                                intent.putExtra("surnameTeacher",getIntent().getStringExtra("surnameTeacher"));
                                intent.putExtra("facultyTeacher",getIntent().getStringExtra("facultyTeacher"));
                                intent.putExtra("fieldTeacher",getIntent().getStringExtra("fieldTeacher"));

                                intent.putExtra("nameTeacher2",nameSelected);
                                intent.putExtra("surnameTeacher2",surnameSelected);
                                intent.putExtra("facultyTeacher2",facultySelected);
                                intent.putExtra("fieldTeacher2",getIntent().getStringExtra("fieldTeacher"));
                                intent.putExtra("emailTeacher2",emailSelected);

                                intent.putExtra("nameStudent",getIntent().getStringExtra("nameStudent"));
                                intent.putExtra("surnameStudent",getIntent().getStringExtra("surnameStudent"));
                                intent.putExtra("facultyStudent",getIntent().getStringExtra("facultyStudent"));
                                intent.putExtra("fieldStudent",getIntent().getStringExtra("fieldStudent"));

                                intent.putExtra("semesterStudent",getIntent().getStringExtra("semesterStudent"));
                                intent.putExtra("degreeStudent",getIntent().getStringExtra("degreeStudent"));
                                intent.putExtra("indexNumberStudent",getIntent().getStringExtra("indexNumberStudent"));

                                intent.putExtra("minuteTicket",getIntent().getStringExtra("minuteTicket"));
                                intent.putExtra("hourTicket",getIntent().getStringExtra("hourTicket"));

                                intent.putExtra("dayTicket",getIntent().getStringExtra("dayTicket"));
                                intent.putExtra("monthTicket",getIntent().getStringExtra("monthTicket"));
                                intent.putExtra("yearTicket",getIntent().getStringExtra("yearTicket"));

                                intent.putExtra("reasonType",getIntent().getStringExtra("reasonType"));
                                intent.putExtra("typeMeet",getIntent().getStringExtra("typeMeet"));

                                intent.putExtra("informationTeacher1",getIntent().getStringExtra("informationTeacher1"));
                                intent.putExtra("informationTeacher2",getIntent().getStringExtra("informationTeacher2"));

                                intent.putExtra("Email", getIntent().getStringExtra("Email"));
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });


        backStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondTeacherStepOne.this, OngoingTicketStudent.class);
                intent.putExtra("nameStudent",getIntent().getStringExtra("nameStudent"));
                intent.putExtra("surnameStudent",getIntent().getStringExtra("surnameStudent"));
                intent.putExtra("facultyStudent",getIntent().getStringExtra("facultyStudent"));
                intent.putExtra("fieldStudent",getIntent().getStringExtra("fieldStudent"));
                startActivity(intent);
            }
        });
    }
}