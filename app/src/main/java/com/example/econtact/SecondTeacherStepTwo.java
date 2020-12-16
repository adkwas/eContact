package com.example.econtact;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(Void aVoid) {

                        NotificationChannel channel = new NotificationChannel("channel01", "name",
                                NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                        channel.setDescription("description");

                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);

                        Notification notification = new NotificationCompat.Builder(SecondTeacherStepTwo.this, "channel01")
                                .setSmallIcon(android.R.drawable.ic_dialog_info)
                                .setContentTitle("Ticket has been sent to the teacher!")
                                .setContentText("Wait for the teacher's decision")
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                .build();
                        notificationManager.notify(0, notification);

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
                Intent intent = new Intent(SecondTeacherStepTwo.this, SecondTeacherStepOne.class);
                intent.putExtra("id", getIntent().getStringExtra("id"));

                intent.putExtra("nameTeacher", getIntent().getStringExtra("nameTeacher"));
                intent.putExtra("surnameTeacher", getIntent().getStringExtra("surnameTeacher"));
                intent.putExtra("facultyTeacher", getIntent().getStringExtra("facultyTeacher"));
                intent.putExtra("fieldTeacher", getIntent().getStringExtra("fieldTeacher"));

                intent.putExtra("nameTeacher2", getIntent().getStringExtra("nameTeacher2"));
                intent.putExtra("surnameTeacher2", getIntent().getStringExtra("surnameTeacher2"));
                intent.putExtra("facultyTeacher2", getIntent().getStringExtra("facultyTeacher2"));
                intent.putExtra("fieldTeacher2", getIntent().getStringExtra("fieldTeacher2"));

                intent.putExtra("nameStudent", getIntent().getStringExtra("nameStudent"));
                intent.putExtra("surnameStudent", getIntent().getStringExtra("surnameStudent"));
                intent.putExtra("facultyStudent", getIntent().getStringExtra("facultyStudent"));
                intent.putExtra("fieldStudent", getIntent().getStringExtra("fieldStudent"));
                intent.putExtra("degreeStudent", getIntent().getStringExtra("degreeStudent"));
                intent.putExtra("semesterStudent", getIntent().getStringExtra("semesterStudent"));
                intent.putExtra("indexNumberStudent", getIntent().getStringExtra("indexNumberStudent"));

                intent.putExtra("minuteTicket", getIntent().getStringExtra("minuteTicket"));
                intent.putExtra("hourTicket", getIntent().getStringExtra("hourTicket"));

                intent.putExtra("dayTicket", getIntent().getStringExtra("dayTicket"));
                intent.putExtra("monthTicket", getIntent().getStringExtra("monthTicket"));
                intent.putExtra("yearTicket", getIntent().getStringExtra("yearTicket"));

                intent.putExtra("reasonType", getIntent().getStringExtra("reasonType"));
                intent.putExtra("typeMeet", getIntent().getStringExtra("typeMeet"));

                intent.putExtra("informationTeacher1", getIntent().getStringExtra("informationTeacher1"));
                intent.putExtra("informationTeacher2", getIntent().getStringExtra("informationTeacher2"));

                intent.putExtra("Email", getIntent().getStringExtra("Email"));

                startActivity(intent);
            }
        });

    }
}