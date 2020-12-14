package com.example.econtact;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

public class ResetSemesterAndDegreeStudent extends AppCompatActivity {

    Spinner resetDegree, resetSemester;
    Button reset, back;

    FirebaseFirestore firebaseFirestore;

    String[] degreeTable = {"Degree of Study", "Engineering degree", "Master's degree"};
    String[] semesterEngineering = {"Semester of Study", "I", "II", "III", "IV", "V", "VI", "VII", "VIII"};
    String[] semesterMaster = {"Semester of Study", "I", "II", "III", "IV"};
    String[] default2 = {"Semester of Study"};

    String emailStudent, nameStudent, surnameStudent,facultyStudent,fieldStudent,indexNumberStudent;

    String  studentDegree, studentSemester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_semester_and_degree_student);

        reset = findViewById(R.id.reset_ResetSemesterAndDegreeStudent);
        resetDegree = findViewById(R.id.degree_ResetSemesterAndDegreeStudent);
        resetSemester = findViewById(R.id.semester_ResetSemesterAndDegreeStudent);
        back = findViewById(R.id.back_ResetSemesterAndDegreeStudent);

        emailStudent = getIntent().getStringExtra("Email");

        firebaseFirestore = FirebaseFirestore.getInstance();

        final DocumentReference documentStudent = firebaseFirestore.collection("Users Accounts").document(emailStudent);
        documentStudent.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshot != null;

                nameStudent = documentSnapshot.getString("Name");
                surnameStudent = documentSnapshot.getString("Surname");
                facultyStudent = documentSnapshot.getString("Faculty");
                fieldStudent = documentSnapshot.getString("Field");
                indexNumberStudent = documentSnapshot.getString("IndexNumber");
            }
        });


        final ArrayAdapter<String> adapterEngineering = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, semesterEngineering);
        final ArrayAdapter<String> adapterMaster = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, semesterMaster);
        final ArrayAdapter<String> adapterDefault2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, default2);
        final ArrayAdapter<String> adapterDegree = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, degreeTable);

        resetDegree.setAdapter(adapterDegree);
        resetDegree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch ((int) l) {
                    case 0:
                        studentDegree = "Degree of Study";
                        resetSemester.setAdapter(adapterDefault2);
                        break;
                    case 1:
                        studentDegree = "Engineering degree";
                        resetSemester.setAdapter(adapterEngineering);
                        resetSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        studentSemester = "Semester of Study";
                                        break;
                                    case 1:
                                        studentSemester = "I";
                                        break;
                                    case 2:
                                        studentSemester = "II";
                                        break;
                                    case 3:
                                        studentSemester = "III";
                                        break;
                                    case 4:
                                        studentSemester = "IV";
                                        break;
                                    case 5:
                                        studentSemester = "V";
                                        break;
                                    case 6:
                                        studentSemester = "VI";
                                        break;
                                    case 7:
                                        studentSemester = "VII";
                                        break;
                                    case 8:
                                        studentSemester = "VIII";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                    case 2:
                        studentDegree = "Master's degree";
                        resetSemester.setAdapter(adapterMaster);
                        resetSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        studentSemester = "Semester of Study";
                                        break;
                                    case 1:
                                        studentSemester = "I";
                                        break;
                                    case 2:
                                        studentSemester = "II";
                                        break;
                                    case 3:
                                        studentSemester = "III";
                                        break;
                                    case 4:
                                        studentSemester = "IV";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                studentSemester = "Semester of Study";
                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String degreeString = studentDegree;
                final String semesterString = studentSemester;

                final String checkDegree = "Degree of Study";
                final String checkYear = "Semester of Study";

                if (degreeString.equals(checkDegree)) {
                    Toast.makeText(getApplicationContext(), "Please check a semester pole!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (semesterString.equals(checkYear)) {
                    Toast.makeText(getApplicationContext(), "Please check a semester pole!", Toast.LENGTH_SHORT).show();
                    return;
                }

                DocumentReference documentReference = firebaseFirestore.collection("Users Accounts").document(emailStudent);
                Map<String, Object> user = new HashMap<>();
                user.put("Name", nameStudent);
                user.put("Surname", surnameStudent);
                user.put("Faculty", facultyStudent);
                user.put("Field", fieldStudent);
                user.put("Semester", semesterString);
                user.put("Degree", degreeString);
                user.put("Email", emailStudent);
                user.put("IndexNumber", indexNumberStudent);
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

                        Notification notification = new NotificationCompat.Builder(ResetSemesterAndDegreeStudent.this, "channel01")
                                .setSmallIcon(android.R.drawable.ic_dialog_info)
                                .setContentTitle("eContact")
                                .setContentText("Degree and Semester changed!")
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

                        Notification notification = new NotificationCompat.Builder(ResetSemesterAndDegreeStudent.this, "channel01")
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetSemesterAndDegreeStudent.this, ResetDataStudent.class);
                intent.putExtra("Email", emailStudent);
                startActivity(intent);
            }
        });
    }
}