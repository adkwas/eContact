package com.example.econtact;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class ResetDataStudent extends AppCompatActivity {

    Button resetFacultyAndField, resetSemesterAndDegree, resetIndexNumber, back;
    String emailStudent, nameStudent, surnameStudent, facultyStudent, fieldStudent,degreeStudent,semesterStudent;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_data_student);

        resetIndexNumber = findViewById(R.id.resetIndexNumber_resetDataStudent);
        resetFacultyAndField = findViewById(R.id.resetFacultyAndField_resetDataStudent);
        resetSemesterAndDegree = findViewById(R.id.resetDegreeAndSemester);
        back = findViewById(R.id.back_resetDataStudent);

        emailStudent = getIntent().getStringExtra("Email");
        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference documentStudent = firebaseFirestore.collection("Users Accounts").document(emailStudent);
        documentStudent.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshot != null;

                nameStudent = documentSnapshot.getString("Name");
                surnameStudent = documentSnapshot.getString("Surname");
                facultyStudent = documentSnapshot.getString("Faculty");
                fieldStudent = documentSnapshot.getString("Field");
                degreeStudent = documentSnapshot.getString("Degree");
                semesterStudent = documentSnapshot.getString("Semester");
            }
        });

        resetIndexNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetIndexNumber = new EditText(v.getContext());
                final AlertDialog.Builder emailResetDialog = new AlertDialog.Builder(v.getContext());
                emailResetDialog.setTitle("Settings Panel");
                emailResetDialog.setMessage("Enter new Index (only number!): ");
                emailResetDialog.setView(resetIndexNumber);

                emailResetDialog.setPositiveButton("Send new Index Number", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newEmail = resetIndexNumber.getText().toString();
                        DocumentReference documentReference = firebaseFirestore.collection("Users Accounts").document(emailStudent);
                        Map<String, Object> user = new HashMap<>();
                        user.put("Name", nameStudent);
                        user.put("Surname", surnameStudent);
                        user.put("Faculty", facultyStudent);
                        user.put("Field", fieldStudent);
                        user.put("Semester", semesterStudent);
                        user.put("Degree", degreeStudent);
                        user.put("Email", emailStudent);
                        user.put("IndexNumber", newEmail);
                        user.put("User", "Student");
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onSuccess(Void aVoid) {
                                NotificationChannel channel = new NotificationChannel("channel01", "name",
                                        NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                                channel.setDescription("description");

                                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                notificationManager.createNotificationChannel(channel);

                                Notification notification = new NotificationCompat.Builder(ResetDataStudent.this, "channel01")
                                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                                        .setContentTitle("eContact")
                                        .setContentText("Index number changed!")
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                        .build();
                                notificationManager.notify(0, notification);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                NotificationChannel channel = new NotificationChannel("channel01", "name",
                                        NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                                channel.setDescription("description");

                                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                notificationManager.createNotificationChannel(channel);

                                Notification notification = new NotificationCompat.Builder(ResetDataStudent.this, "channel01")
                                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                                        .setContentTitle("eContact")
                                        .setContentText("Error: " + e.toString())
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                        .build();
                                notificationManager.notify(0, notification);
                            }
                        });

                    }
                });

                emailResetDialog.setNegativeButton("Back to ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                emailResetDialog.create().show();
            }
        });

        resetFacultyAndField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (ResetDataStudent.this, ResetFacultyAndFiledStudent.class);
                intent.putExtra("Email", emailStudent);
                startActivity(intent);
            }
        });

        resetSemesterAndDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (ResetDataStudent.this, ResetSemesterAndDegreeStudent.class);
                intent.putExtra("Email", emailStudent);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetDataStudent.this, SettingsPanelStudent.class);
                intent.putExtra("Email", emailStudent);
                startActivity(intent);
            }
        });



    }
}