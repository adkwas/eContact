package com.example.econtact;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SecondTeacherStepTwo extends AppCompatActivity {

    TextView nameTeacher2, surnameTeacher2, fieldTeacher2, facultyTeacher2, emailTeacher2;
    Button sendTicket, previousStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_teacher_step_two);

        nameTeacher2 = findViewById(R.id.nameTeacher_secondTeacherStepTwo);
        surnameTeacher2 = findViewById(R.id.surnameTeacher_secondTeacherStepTwo);
        fieldTeacher2 = findViewById(R.id.fieldTeacher_secondTeacherStepTwo);
        facultyTeacher2 = findViewById(R.id.facultyTeacher_secondTeacherStepTwo);
        emailTeacher2 = findViewById(R.id.emailTeacher_secondTeacherStepTwo);
        sendTicket = findViewById(R.id.nextStep_secondTeacherStepTwo);
        previousStep = findViewById(R.id.previousStep_secondTeacherStepTwo);

        nameTeacher2.setText(getIntent().getStringExtra("nameTeacher2"));
        surnameTeacher2.setText(getIntent().getStringExtra("surnameTeacher2"));
        facultyTeacher2.setText(getIntent().getStringExtra("facultyTeacher2"));
        fieldTeacher2.setText(getIntent().getStringExtra("fieldTeacher2"));
        emailTeacher2.setText(getIntent().getStringExtra("emailTeacher2"));


        sendTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTeacher = getIntent().getStringExtra("nameTeacher");
                String surnameTeacher = getIntent().getStringExtra("surnameTeacher");
                String facultyTeacher = getIntent().getStringExtra("facultyTeacher");
                String fieldTeacher = getIntent().getStringExtra("fieldTeacher");

                String nameTeacher2 = getIntent().getStringExtra("nameTeacher2");
                String surnameTeacher2 = getIntent().getStringExtra("surnameTeacher2");
                String facultyTeacher2 = getIntent().getStringExtra("facultyTeacher2");
                String fieldTeacher2 = getIntent().getStringExtra("fieldTeacher2");

                String nameStudent = getIntent().getStringExtra("nameStudent");
                String surnameStudent = getIntent().getStringExtra("surnameStudent");
                String facultyStudent = getIntent().getStringExtra("facultyStudent");
                String fieldStudent = getIntent().getStringExtra("fieldStudent");
                String degreeStudent = getIntent().getStringExtra("degreeStudent");
                String semesterStudent = getIntent().getStringExtra("semesterStudent");
                String indexNumberStudent = getIntent().getStringExtra("indexNumberStudent");



                FirebaseFirestore firebaseFirestore;
                firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference documentReference = firebaseFirestore.collection("Pending applications")
                        .document(getIntent().getStringExtra("id"));
                Map<String, Object> user = new HashMap<>();

                user.put("nameTeacher", nameTeacher);
                user.put("surnameTeacher", surnameTeacher);
                user.put("facultyTeacher", facultyTeacher);
                user.put("fieldTeacher", fieldTeacher);

                user.put("nameTeacher2", nameTeacher2);
                user.put("surnameTeacher2", surnameTeacher2);
                user.put("facultyTeacher2", facultyTeacher2);
                user.put("fieldTeacher2", fieldTeacher2);

                user.put("nameStudent", nameStudent);
                user.put("surnameStudent", surnameStudent);
                user.put("facultyStudent", facultyStudent);
                user.put("fieldStudent", fieldStudent);
                user.put("degreeStudent", degreeStudent);
                user.put("semesterStudent", semesterStudent);
                user.put("indexNumberStudent", indexNumberStudent);

                user.put("minuteTicket", getIntent().getStringExtra("minuteTicket"));
                user.put("hourTicket", getIntent().getStringExtra("hourTicket"));

                user.put("dayTicket", getIntent().getStringExtra("dayTicket"));
                user.put("monthTicket", getIntent().getStringExtra("monthTicket"));
                user.put("yearTicket", getIntent().getStringExtra("yearTicket"));

                user.put("typeMeet", getIntent().getStringExtra("typeMeet"));
                user.put("reasonType", getIntent().getStringExtra("reasonType"));

                user.put("informationTeacher1", getIntent().getStringExtra("informationTeacher1"));
                user.put("informationTeacher2", getIntent().getStringExtra("informationTeacher2"));

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SecondTeacherStepTwo.this, "Ticket has been sent to the teacher!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Error!: " + e.toString());
                    }
                });
                Intent intent = new Intent(SecondTeacherStepTwo.this, PanelStudent.class);
                intent.putExtra("Email", getIntent().getStringExtra("Email"));
                startActivity(intent);
            }
        });

        previousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondTeacherStepTwo.this, AddNewTicketStepOne.class));
            }
        });

    }
}