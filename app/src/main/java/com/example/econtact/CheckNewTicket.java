package com.example.econtact;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CheckNewTicket extends AppCompatActivity {

    Button cancelButton, acceptButton;
    TextView nameStudent, surnameStudent, facultyStudent, fieldStudent, degreeStudent, semesterStudent, indexNumberStudent, typeMeet, dataMeet, timeMeet, reasonMeet;
    FirebaseFirestore firebaseFirestore;
    String nameTeacher, surnameTeacher, facultyTeacher, fieldTeacher, dayTicket, monthTicket, yearTicket, minuteTicket, hourTicket;

    List<CloudFireCheckNewTicket> objectList = new ArrayList<>();

    List<String> IDArrayList = new ArrayList<>();
    int indexTicket = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_new_ticket);

        degreeStudent = findViewById(R.id.degreeStudent_checkNewTicket);
        semesterStudent = findViewById(R.id.yearStudent_checkNewTicket);
        cancelButton = findViewById(R.id.cancelticket_checkNewTicket);
        acceptButton = findViewById(R.id.accept_ticket_checkNewTicket);
        nameStudent = findViewById(R.id.nameStudent_checkNewTicket);
        surnameStudent = findViewById(R.id.surnameStudent_checkNewTicket);
        facultyStudent = findViewById(R.id.facultyStudent_checkNewTicket);
        fieldStudent = findViewById(R.id.fieldStudent_checkNewTicket);
        indexNumberStudent = findViewById(R.id.indexNumberStudent_checkNewTicket);
        typeMeet = findViewById(R.id.typeMeet_checkNewTicket);
        dataMeet = findViewById(R.id.dataMeet_checkNewTicket);
        timeMeet = findViewById(R.id.timeMeet_checkNewTicket);
        reasonMeet = findViewById(R.id.reasonMeet_checkNewTicket);

        nameTeacher = getIntent().getStringExtra("nameTeacher");
        surnameTeacher = getIntent().getStringExtra("surnameTeacher");
        facultyTeacher = getIntent().getStringExtra("facultyTeacher");
        fieldTeacher = getIntent().getStringExtra("fieldTeacher");

        firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("Pending applications");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                        String nameVal = document.getString("nameTeacher");
                        String surnameVal = document.getString("surnameTeacher");
                        String facultyVal = document.getString("facultyTeacher");
                        String fieldVal = document.getString("fieldTeacher");

                        String nameVal2 = document.getString("nameTeacher2");
                        String surnameVal2 = document.getString("surnameTeacher2");
                        String facultyVal2 = document.getString("facultyTeacher2");
                        String fieldVal2 = document.getString("fieldTeacher2");

                        assert nameVal != null;
                        assert surnameVal != null;
                        assert facultyVal != null;
                        assert fieldVal != null;

                        assert nameVal2 != null;
                        assert surnameVal2 != null;
                        assert facultyVal2 != null;
                        assert fieldVal2 != null;

                        if (nameVal.equals(nameTeacher) && surnameVal.equals(surnameTeacher)
                                && facultyVal.equals(facultyTeacher) && fieldVal.equals(fieldTeacher)) {
                            dayTicket = document.getString("dayTicket");
                            monthTicket = document.getString("monthTicket");
                            yearTicket = document.getString("yearTicket");
                            minuteTicket = document.getString("minuteTicket");
                            hourTicket = document.getString("hourTicket");
                            CloudFireCheckNewTicket cloudFireCheckNewTicket = new CloudFireCheckNewTicket
                                    (
                                            document.getString("nameTeacher"),
                                            document.getString("surnameTeacher"),
                                            document.getString("facultyTeacher"),
                                            document.getString("fieldTeacher"),

                                            " ",
                                            " ",
                                            " ",
                                            " ",

                                            document.getString("nameStudent"),
                                            document.getString("surnameStudent"),
                                            document.getString("facultyStudent"),
                                            document.getString("fieldStudent"),
                                            document.getString("degreeStudent"),
                                            document.getString("semesterStudent"),
                                            document.getString("indexNumberStudent"),

                                            document.getString("typeMeet"),
                                            dayTicket + "." + monthTicket + "." + yearTicket,
                                            hourTicket + ":" + minuteTicket,
                                            document.getString("reasonType"));
                            objectList.add(cloudFireCheckNewTicket);
                            IDArrayList.add(document.getId());
                        }

                        if (nameVal2.equals(nameTeacher) && surnameVal2.equals(surnameTeacher)
                                && facultyVal2.equals(facultyTeacher) && fieldVal2.equals(fieldTeacher)) {
                            dayTicket = document.getString("dayTicket");
                            monthTicket = document.getString("monthTicket");
                            yearTicket = document.getString("yearTicket");
                            minuteTicket = document.getString("minuteTicket");
                            hourTicket = document.getString("hourTicket");
                            CloudFireCheckNewTicket cloudFireCheckNewTicket = new CloudFireCheckNewTicket(
                                    document.getString("nameTeacher"),
                                    document.getString("surnameTeacher"),
                                    document.getString("facultyTeacher"),
                                    document.getString("fieldTeacher"),

                                    document.getString("nameTeacher2"),
                                    document.getString("surnameTeacher2"),
                                    document.getString("facultyTeacher2"),
                                    document.getString("fieldTeacher2"),

                                    document.getString("nameStudent"),
                                    document.getString("surnameStudent"),
                                    document.getString("facultyStudent"),
                                    document.getString("fieldStudent"),
                                    document.getString("degreeStudent"),
                                    document.getString("semesterStudent"),
                                    document.getString("indexNumberStudent"),

                                    document.getString("typeMeet"),
                                    dayTicket + "." + monthTicket + "." + yearTicket,
                                    hourTicket + ":" + minuteTicket,
                                    document.getString("reasonType"));
                            objectList.add(cloudFireCheckNewTicket);
                            IDArrayList.add(document.getId());
                        }
                    }

                    if (objectList.isEmpty()) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CheckNewTicket.this);
                        dialogBuilder.setTitle("Check New Tickets");
                        dialogBuilder.setMessage("No tickets to display!");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setNeutralButton("Back to Panel", new Dialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(CheckNewTicket.this, PanelTeacher.class);
                                intent.putExtra("Email", getIntent().getStringExtra("Email"));
                                startActivity(intent);
                            }
                        });
                        dialogBuilder.create();
                        dialogBuilder.show();
                    }

                    if (objectList.size() == 1) {
                        CloudFireCheckNewTicket cloudFireCheckNewTicket = objectList.get(0);
                        nameStudent.setText("Name: " + cloudFireCheckNewTicket.nameStudent);
                        surnameStudent.setText("Surname: " + cloudFireCheckNewTicket.surnameStudent);
                        facultyStudent.setText(cloudFireCheckNewTicket.facultyStudent);
                        fieldStudent.setText("Field: " + cloudFireCheckNewTicket.fieldStudent);
                        degreeStudent.setText(cloudFireCheckNewTicket.degreeStudent);
                        semesterStudent.setText("Semester: " + cloudFireCheckNewTicket.semesterStudent);
                        indexNumberStudent.setText("Index: " + cloudFireCheckNewTicket.indexNumberStudent);
                        typeMeet.setText("Type Meet: " + cloudFireCheckNewTicket.typeMeet);
                        dataMeet.setText("Date: " + cloudFireCheckNewTicket.dataMeet);
                        timeMeet.setText("Time: " + cloudFireCheckNewTicket.timeMeet);
                        reasonMeet.setText("Reason: " + cloudFireCheckNewTicket.reason);
                    }

                    if (objectList.size() > 1) {
                        CloudFireCheckNewTicket cloudFireCheckNewTicket = objectList.get(0);
                        nameStudent.setText("Name: " + cloudFireCheckNewTicket.nameStudent);
                        surnameStudent.setText("Surname: " + cloudFireCheckNewTicket.surnameStudent);
                        facultyStudent.setText(cloudFireCheckNewTicket.facultyStudent);
                        fieldStudent.setText("Field: " + cloudFireCheckNewTicket.fieldStudent);
                        degreeStudent.setText(cloudFireCheckNewTicket.degreeStudent);
                        semesterStudent.setText("Semester: " + cloudFireCheckNewTicket.semesterStudent);
                        indexNumberStudent.setText("Index: " + cloudFireCheckNewTicket.indexNumberStudent);
                        typeMeet.setText("Type Meet: " + cloudFireCheckNewTicket.typeMeet);
                        dataMeet.setText("Date: " + cloudFireCheckNewTicket.dataMeet);
                        timeMeet.setText("Time: " + cloudFireCheckNewTicket.timeMeet);
                        reasonMeet.setText("Reason: " + cloudFireCheckNewTicket.reason);
                    }
                }
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                firebaseFirestore = FirebaseFirestore.getInstance();
                CloudFireCheckNewTicket cloudFireCheckNewTicket = objectList.get(indexTicket);
                final DocumentReference documentReference = firebaseFirestore.collection("Accepted Applications")
                        .document(cloudFireCheckNewTicket.nameStudent + " " + cloudFireCheckNewTicket.surnameStudent + " to " +
                                cloudFireCheckNewTicket.nameTeacher + " " + cloudFireCheckNewTicket.surnameTeacher + " date: " + cloudFireCheckNewTicket.dataMeet + " time: " + cloudFireCheckNewTicket.timeMeet);
                Map<String, Object> user = new HashMap<>();

                if (cloudFireCheckNewTicket.nameTeacher2.isEmpty() &&
                        cloudFireCheckNewTicket.surnameTeacher2.isEmpty() &&
                        cloudFireCheckNewTicket.facultyTeacher2.isEmpty() &&
                        cloudFireCheckNewTicket.fieldTeacher2.isEmpty()) {
                    user.put("nameTeacher", cloudFireCheckNewTicket.nameTeacher);
                    user.put("surnameTeacher", cloudFireCheckNewTicket.surnameTeacher);
                    user.put("facultyTeacher", cloudFireCheckNewTicket.facultyTeacher);
                    user.put("fieldTeacher", cloudFireCheckNewTicket.fieldTeacher);

                    user.put("nameTeacher2", " ");
                    user.put("surnameTeacher2", " ");
                    user.put("facultyTeacher2", " ");
                    user.put("fieldTeacher2", " ");

                    user.put("nameStudent", cloudFireCheckNewTicket.nameStudent);
                    user.put("surnameStudent", cloudFireCheckNewTicket.surnameStudent);
                    user.put("facultyStudent", cloudFireCheckNewTicket.facultyStudent);
                    user.put("fieldStudent", cloudFireCheckNewTicket.fieldStudent);
                    user.put("degreeStudent", cloudFireCheckNewTicket.degreeStudent);
                    user.put("semesterStudent", cloudFireCheckNewTicket.semesterStudent);
                    user.put("indexNumberStudent", cloudFireCheckNewTicket.indexNumberStudent);

                    String minuteString = String.valueOf(minuteTicket);
                    String hourString = String.valueOf(hourTicket);
                    user.put("minuteTicket", minuteString);
                    user.put("hourTicket", hourString);

                    String dayString = String.valueOf(dayTicket);
                    String monthString = String.valueOf(monthTicket);
                    String yearString = String.valueOf(yearTicket);
                    user.put("dayTicket", dayString);
                    user.put("monthTicket", monthString);
                    user.put("yearTicket", yearString);

                    user.put("typeMeet", cloudFireCheckNewTicket.typeMeet);
                    user.put("reasonType", cloudFireCheckNewTicket.reason);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onSuccess(Void aVoid) {
                            NotificationChannel channel = new NotificationChannel("channel01", "name",
                                    NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                            channel.setDescription("description");

                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                            notificationManager.createNotificationChannel(channel);

                            Notification notification = new NotificationCompat.Builder(CheckNewTicket.this, "channel01")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("eContact")
                                    .setContentText("The ticket has been accepted!")
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

                            Notification notification = new NotificationCompat.Builder(CheckNewTicket.this, "channel01")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("eContact")
                                    .setContentText("Error!: " + e.toString())
                                    .setDefaults(Notification.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                    .build();
                            notificationManager.notify(0, notification);
                        }
                    });

                    //CALENDAR

                    objectList.remove(indexTicket);
                    int sizeList = objectList.size();
                    final CollectionReference collectionReference = firebaseFirestore.collection("Pending applications");
                    collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                String val = IDArrayList.get(indexTicket);
                                collectionReference.document(val).delete();
                                IDArrayList.remove(indexTicket);
                            }
                        }
                    });

                    if (indexTicket < sizeList || indexTicket == (sizeList - 1)) {
                        cloudFireCheckNewTicket = objectList.get(indexTicket);
                        nameStudent.setText("Name: " + cloudFireCheckNewTicket.nameStudent);
                        surnameStudent.setText("Surname: " + cloudFireCheckNewTicket.surnameStudent);
                        facultyStudent.setText(cloudFireCheckNewTicket.facultyStudent);
                        fieldStudent.setText("Field: " + cloudFireCheckNewTicket.fieldStudent);
                        degreeStudent.setText(cloudFireCheckNewTicket.degreeStudent);
                        semesterStudent.setText("Semester: " + cloudFireCheckNewTicket.semesterStudent);
                        indexNumberStudent.setText("Index: " + cloudFireCheckNewTicket.indexNumberStudent);
                        typeMeet.setText("Type Meet: " + cloudFireCheckNewTicket.typeMeet);
                        dataMeet.setText("Date: " + cloudFireCheckNewTicket.dataMeet);
                        timeMeet.setText("Time: " + cloudFireCheckNewTicket.timeMeet);
                        reasonMeet.setText("Reason: " + cloudFireCheckNewTicket.reason);
                    } else {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CheckNewTicket.this);
                        dialogBuilder.setTitle("Check New Ticket");
                        dialogBuilder.setMessage("No tickets to display!");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setNeutralButton("Back to Panel", new Dialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(CheckNewTicket.this, PanelTeacher.class);
                                intent.putExtra("Email", getIntent().getStringExtra("Email"));
                                startActivity(intent);
                            }
                        });
                        dialogBuilder.create();
                        dialogBuilder.show();
                    }
                }

                if ((!(cloudFireCheckNewTicket.nameTeacher2.isEmpty())) &&
                        (!(cloudFireCheckNewTicket.surnameTeacher2.isEmpty())) &&
                        (!(cloudFireCheckNewTicket.facultyTeacher2.isEmpty())) &&
                        (!(cloudFireCheckNewTicket.fieldTeacher2.isEmpty()))) {
                    user.put("nameTeacher", cloudFireCheckNewTicket.nameTeacher);
                    user.put("surnameTeacher", cloudFireCheckNewTicket.surnameTeacher);
                    user.put("facultyTeacher", cloudFireCheckNewTicket.facultyTeacher);
                    user.put("fieldTeacher", cloudFireCheckNewTicket.fieldTeacher);

                    user.put("nameTeacher2", cloudFireCheckNewTicket.nameTeacher2);
                    user.put("surnameTeacher2", cloudFireCheckNewTicket.surnameTeacher2);
                    user.put("facultyTeacher2", cloudFireCheckNewTicket.facultyTeacher2);
                    user.put("fieldTeacher2", cloudFireCheckNewTicket.fieldTeacher2);

                    user.put("nameStudent", cloudFireCheckNewTicket.nameStudent);
                    user.put("surnameStudent", cloudFireCheckNewTicket.surnameStudent);
                    user.put("facultyStudent", cloudFireCheckNewTicket.facultyStudent);
                    user.put("fieldStudent", cloudFireCheckNewTicket.fieldStudent);
                    user.put("degreeStudent", cloudFireCheckNewTicket.degreeStudent);
                    user.put("semesterStudent", cloudFireCheckNewTicket.semesterStudent);
                    user.put("indexNumberStudent", cloudFireCheckNewTicket.indexNumberStudent);

                    String minuteString = String.valueOf(minuteTicket);
                    String hourString = String.valueOf(hourTicket);
                    user.put("minuteTicket", minuteString);
                    user.put("hourTicket", hourString);

                    String dayString = String.valueOf(dayTicket);
                    String monthString = String.valueOf(monthTicket);
                    String yearString = String.valueOf(yearTicket);
                    user.put("dayTicket", dayString);
                    user.put("monthTicket", monthString);
                    user.put("yearTicket", yearString);

                    user.put("typeMeet", cloudFireCheckNewTicket.typeMeet);
                    user.put("reasonType", cloudFireCheckNewTicket.reason);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onSuccess(Void aVoid) {
                            NotificationChannel channel = new NotificationChannel("channel01", "name",
                                    NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                            channel.setDescription("description");

                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                            notificationManager.createNotificationChannel(channel);

                            Notification notification = new NotificationCompat.Builder(CheckNewTicket.this, "channel01")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("eContact")
                                    .setContentText("The application has been accepted")
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

                            Notification notification = new NotificationCompat.Builder(CheckNewTicket.this, "channel01")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("eContact")
                                    .setContentText("Error!: " + e.toString())
                                    .setDefaults(Notification.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                    .build();
                            notificationManager.notify(0, notification);
                        }
                    });

                    objectList.remove(indexTicket);
                    int sizeList = objectList.size();
                    final CollectionReference collectionReference = firebaseFirestore.collection("Pending applications");
                    collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                String val = IDArrayList.get(indexTicket);
                                collectionReference.document(val).delete();
                                IDArrayList.remove(indexTicket);
                            }
                        }
                    });

                    if (indexTicket < sizeList || indexTicket == (sizeList - 1)) {
                        cloudFireCheckNewTicket = objectList.get(indexTicket);
                        nameStudent.setText("Name: " + cloudFireCheckNewTicket.nameStudent);
                        surnameStudent.setText("Surname: " + cloudFireCheckNewTicket.surnameStudent);
                        facultyStudent.setText(cloudFireCheckNewTicket.facultyStudent);
                        fieldStudent.setText("Field: " + cloudFireCheckNewTicket.fieldStudent);
                        degreeStudent.setText(cloudFireCheckNewTicket.degreeStudent);
                        semesterStudent.setText("Semester: " + cloudFireCheckNewTicket.semesterStudent);
                        indexNumberStudent.setText("Index: " + cloudFireCheckNewTicket.indexNumberStudent);
                        typeMeet.setText("Type Meet: " + cloudFireCheckNewTicket.typeMeet);
                        dataMeet.setText("Date: " + cloudFireCheckNewTicket.dataMeet);
                        timeMeet.setText("Time: " + cloudFireCheckNewTicket.timeMeet);
                        reasonMeet.setText("Reason: " + cloudFireCheckNewTicket.reason);
                    } else {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CheckNewTicket.this);
                        dialogBuilder.setTitle("Check New Ticket");
                        dialogBuilder.setMessage("No tickets to display!");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setNeutralButton("Back to Panel", new Dialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //showToast("You picked positive button");
                                Intent intent = new Intent(CheckNewTicket.this, PanelTeacher.class);
                                intent.putExtra("Email", getIntent().getStringExtra("Email"));
                                startActivity(intent);
                            }
                        });
                        dialogBuilder.create();
                        dialogBuilder.show();
                    }
                }


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                firebaseFirestore = FirebaseFirestore.getInstance();
                CloudFireCheckNewTicket cloudFireCheckNewTicket = objectList.get(indexTicket);
                DocumentReference documentReference = firebaseFirestore.collection("Canceled Applications")
                        .document(cloudFireCheckNewTicket.nameStudent + " " + cloudFireCheckNewTicket.surnameStudent + " to " +
                                cloudFireCheckNewTicket.nameTeacher + " " +
                                cloudFireCheckNewTicket.surnameTeacher + " date: " + cloudFireCheckNewTicket.dataMeet +
                                " time: " + cloudFireCheckNewTicket.timeMeet);
                Map<String, Object> user = new HashMap<>();

                if (cloudFireCheckNewTicket.nameTeacher2.isEmpty() &&
                        cloudFireCheckNewTicket.surnameTeacher2.isEmpty() &&
                        cloudFireCheckNewTicket.facultyTeacher2.isEmpty() &&
                        cloudFireCheckNewTicket.fieldTeacher2.isEmpty()) {

                    user.put("nameTeacher", cloudFireCheckNewTicket.nameTeacher);
                    user.put("surnameTeacher", cloudFireCheckNewTicket.surnameTeacher);
                    user.put("facultyTeacher", cloudFireCheckNewTicket.facultyTeacher);
                    user.put("fieldTeacher", cloudFireCheckNewTicket.fieldTeacher);

                    user.put("nameStudent", cloudFireCheckNewTicket.nameStudent);
                    user.put("surnameStudent", cloudFireCheckNewTicket.surnameStudent);
                    user.put("facultyStudent", cloudFireCheckNewTicket.facultyStudent);
                    user.put("fieldStudent", cloudFireCheckNewTicket.fieldStudent);
                    user.put("degreeStudent", cloudFireCheckNewTicket.degreeStudent);
                    user.put("semesterStudent", cloudFireCheckNewTicket.semesterStudent);
                    user.put("indexNumberStudent", cloudFireCheckNewTicket.indexNumberStudent);

                    String minuteString = String.valueOf(minuteTicket);
                    String hourString = String.valueOf(hourTicket);
                    user.put("minuteTicket", minuteString);
                    user.put("hourTicket", hourString);

                    String dayString = String.valueOf(dayTicket);
                    String monthString = String.valueOf(monthTicket);
                    String yearString = String.valueOf(yearTicket);
                    user.put("dayTicket", dayString);
                    user.put("monthTicket", monthString);
                    user.put("yearTicket", yearString);

                    user.put("typeMeet", cloudFireCheckNewTicket.typeMeet);
                    user.put("reasonType", cloudFireCheckNewTicket.reason);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onSuccess(Void aVoid) {

                            NotificationChannel channel = new NotificationChannel("channel01", "name",
                                    NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                            channel.setDescription("description");

                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                            notificationManager.createNotificationChannel(channel);

                            Notification notification = new NotificationCompat.Builder(CheckNewTicket.this, "channel01")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("eContact")
                                    .setContentText("The application has been canceled")
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

                            Notification notification = new NotificationCompat.Builder(CheckNewTicket.this, "channel01")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("eContact")
                                    .setContentText("Error: " + e.toString())
                                    .setDefaults(Notification.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                    .build();
                            notificationManager.notify(0, notification);
                        }
                    });

                    objectList.remove(indexTicket);
                    int sizeList = objectList.size();
                    final CollectionReference collectionReference = firebaseFirestore.collection("Pending applications");
                    collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                String val = IDArrayList.get(indexTicket);
                                collectionReference.document(val).delete();
                                IDArrayList.remove(indexTicket);
                            }
                        }
                    });

                    if (indexTicket < sizeList || indexTicket == (sizeList - 1)) {
                        cloudFireCheckNewTicket = objectList.get(indexTicket);
                        nameStudent.setText("Name: " + cloudFireCheckNewTicket.nameStudent);
                        surnameStudent.setText("Surname: " + cloudFireCheckNewTicket.surnameStudent);
                        facultyStudent.setText(cloudFireCheckNewTicket.facultyStudent);
                        fieldStudent.setText("Field: " + cloudFireCheckNewTicket.fieldStudent);
                        degreeStudent.setText(cloudFireCheckNewTicket.degreeStudent);
                        semesterStudent.setText("Semester: " + cloudFireCheckNewTicket.semesterStudent);
                        indexNumberStudent.setText("Index: " + cloudFireCheckNewTicket.indexNumberStudent);
                        typeMeet.setText("Type Meet: " + cloudFireCheckNewTicket.typeMeet);
                        dataMeet.setText("Date: " + cloudFireCheckNewTicket.dataMeet);
                        timeMeet.setText("Time: " + cloudFireCheckNewTicket.timeMeet);
                        reasonMeet.setText("Reason: " + cloudFireCheckNewTicket.reason);
                    } else {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CheckNewTicket.this);
                        dialogBuilder.setTitle("Pending applications");
                        dialogBuilder.setMessage("No tickets to display!");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setNeutralButton("Back to Panel", new Dialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //showToast("You picked positive button");
                                Intent intent = new Intent(CheckNewTicket.this, PanelTeacher.class);
                                intent.putExtra("Email", getIntent().getStringExtra("Email"));
                                startActivity(intent);
                            }
                        });
                        dialogBuilder.create();
                        dialogBuilder.show();
                    }
                }

                if ((!(cloudFireCheckNewTicket.nameTeacher2.isEmpty())) &&
                        (!(cloudFireCheckNewTicket.surnameTeacher2.isEmpty())) &&
                        (!(cloudFireCheckNewTicket.facultyTeacher2.isEmpty())) &&
                        (!(cloudFireCheckNewTicket.fieldTeacher2.isEmpty()))) {
                    user.put("nameTeacher", cloudFireCheckNewTicket.nameTeacher);
                    user.put("surnameTeacher", cloudFireCheckNewTicket.nameTeacher);
                    user.put("facultyTeacher", cloudFireCheckNewTicket.facultyTeacher);
                    user.put("fieldTeacher", cloudFireCheckNewTicket.fieldTeacher);

                    user.put("nameTeacher2", cloudFireCheckNewTicket.nameTeacher2);
                    user.put("surnameTeacher2", cloudFireCheckNewTicket.surnameTeacher2);
                    user.put("facultyTeacher2", cloudFireCheckNewTicket.facultyTeacher2);
                    user.put("fieldTeacher2", cloudFireCheckNewTicket.fieldTeacher2);

                    user.put("nameStudent", cloudFireCheckNewTicket.nameStudent);
                    user.put("surnameStudent", cloudFireCheckNewTicket.surnameStudent);
                    user.put("facultyStudent", cloudFireCheckNewTicket.facultyStudent);
                    user.put("fieldStudent", cloudFireCheckNewTicket.fieldStudent);
                    user.put("degreeStudent", cloudFireCheckNewTicket.degreeStudent);
                    user.put("semesterStudent", cloudFireCheckNewTicket.semesterStudent);
                    user.put("indexNumberStudent", cloudFireCheckNewTicket.indexNumberStudent);

                    user.put("secondTeacher", "no");

                    String minuteString = String.valueOf(minuteTicket);
                    String hourString = String.valueOf(hourTicket);
                    user.put("minuteTicket", minuteString);
                    user.put("hourTicket", hourString);

                    String dayString = String.valueOf(dayTicket);
                    String monthString = String.valueOf(monthTicket);
                    String yearString = String.valueOf(yearTicket);
                    user.put("dayTicket", dayString);
                    user.put("monthTicket", monthString);
                    user.put("yearTicket", yearString);

                    user.put("typeMeet", cloudFireCheckNewTicket.typeMeet);
                    user.put("reasonType", cloudFireCheckNewTicket.reason);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onSuccess(Void aVoid) {
                            NotificationChannel channel = new NotificationChannel("channel01", "name",
                                    NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                            channel.setDescription("description");

                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                            notificationManager.createNotificationChannel(channel);

                            Notification notification = new NotificationCompat.Builder(CheckNewTicket.this, "channel01")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("eContact")
                                    .setContentText("The application has been canceled")
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

                            Notification notification = new NotificationCompat.Builder(CheckNewTicket.this, "channel01")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("eContact")
                                    .setContentText("Error: " + e.toString())
                                    .setDefaults(Notification.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                    .build();
                            notificationManager.notify(0, notification);
                        }
                    });

                    objectList.remove(indexTicket);
                    int sizeList = objectList.size();
                    final CollectionReference collectionReference = firebaseFirestore.collection("Pending applications");
                    collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                String val = IDArrayList.get(indexTicket);
                                collectionReference.document(val).delete();
                                IDArrayList.remove(indexTicket);
                            }
                        }
                    });

                    if (indexTicket < sizeList || indexTicket == (sizeList - 1)) {
                        cloudFireCheckNewTicket = objectList.get(indexTicket);
                        nameStudent.setText("Name: " + cloudFireCheckNewTicket.nameStudent);
                        surnameStudent.setText("Surname: " + cloudFireCheckNewTicket.surnameStudent);
                        facultyStudent.setText(cloudFireCheckNewTicket.facultyStudent);
                        fieldStudent.setText("Field: " + cloudFireCheckNewTicket.fieldStudent);
                        degreeStudent.setText(cloudFireCheckNewTicket.degreeStudent);
                        semesterStudent.setText("Semester: " + cloudFireCheckNewTicket.semesterStudent);
                        indexNumberStudent.setText("Index: " + cloudFireCheckNewTicket.indexNumberStudent);
                        typeMeet.setText("Type Meet: " + cloudFireCheckNewTicket.typeMeet);
                        dataMeet.setText("Date: " + cloudFireCheckNewTicket.dataMeet);
                        timeMeet.setText("Time: " + cloudFireCheckNewTicket.timeMeet);
                        reasonMeet.setText("Reason: " + cloudFireCheckNewTicket.reason);
                    } else {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CheckNewTicket.this);
                        dialogBuilder.setTitle("Pending applications");
                        dialogBuilder.setMessage("No tickets to display!");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setNeutralButton("Back to Panel", new Dialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //showToast("You picked positive button");
                                Intent intent = new Intent(CheckNewTicket.this, PanelTeacher.class);
                                intent.putExtra("Email", getIntent().getStringExtra("Email"));
                                startActivity(intent);
                            }
                        });
                        dialogBuilder.create();
                        dialogBuilder.show();
                    }
                }


            }
        });
    }

    public static class CloudFireCheckNewTicket {

        String nameTeacher = " ";
        String surnameTeacher = " ";
        String facultyTeacher = " ";
        String fieldTeacher = " ";

        String nameTeacher2 = " ";
        String surnameTeacher2 = " ";
        String facultyTeacher2 = " ";
        String fieldTeacher2 = " ";

        String nameStudent = " ";
        String surnameStudent = " ";
        String facultyStudent = " ";
        String fieldStudent = " ";
        String degreeStudent = " ";
        String semesterStudent = " ";
        String indexNumberStudent = " ";

        String typeMeet = " ";
        String dataMeet = " ";
        String timeMeet = " ";
        String reason = " ";

        public CloudFireCheckNewTicket(String nameTeacher, String surnameTeacher, String facultyTeacher, String fieldTeacher,
                                       String nameTeacher2, String surnameTeacher2, String facultyTeacher2, String fieldTeacher2,
                                       String nameStudent, String surnameStudent, String facultyStudent,
                                       String fieldStudent, String degreeStudent, String semesterStudent, String indexNumberStudent,
                                       String typeMeet, String dataMeet, String timeMeet, String reason) {
            this.nameTeacher = nameTeacher;
            this.surnameTeacher = surnameTeacher;
            this.facultyTeacher = facultyTeacher;
            this.fieldTeacher = fieldTeacher;

            this.nameTeacher2 = nameTeacher2;
            this.surnameTeacher2 = surnameTeacher2;
            this.facultyTeacher2 = facultyTeacher2;
            this.fieldTeacher2 = fieldTeacher2;

            this.nameStudent = nameStudent;
            this.surnameStudent = surnameStudent;
            this.facultyStudent = facultyStudent;
            this.fieldStudent = fieldStudent;
            this.typeMeet = typeMeet;
            this.degreeStudent = degreeStudent;
            this.semesterStudent = semesterStudent;
            this.indexNumberStudent = indexNumberStudent;

            this.dataMeet = dataMeet;
            this.timeMeet = timeMeet;
            this.reason = reason;
        }
    }
}