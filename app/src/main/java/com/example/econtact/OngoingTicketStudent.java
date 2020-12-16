package com.example.econtact;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class OngoingTicketStudent extends AppCompatActivity {

    Button secondTeacherAddButton, previousButton, nextButton, downloadData, addToCalendar;

    String nameStudent, surnameStudent, facultyStudent, fieldStudent;
    String nameTeacher2Canceled = " ", surnameTeacher2Canceled = " ", facultyTeacher2Canceled = " ",
            fieldTeacher2Canceled = " ";
    FirebaseFirestore firebaseFirestore;
    int indexTicket = 0;
    List<CloudFireOngoingTicketStudent> objectArrayList = new ArrayList<>();

    TextView nameTeacher1, surnameTeacher1, surnameTeacher2, nameTeacher2,
            informationTeacher1TextView, informationTeacher2TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_ticket_student);

        secondTeacherAddButton = findViewById(R.id.secondTeacherButton_ongoingTicketStudent);
        previousButton = findViewById(R.id.previous_ongoingTicketStudent);
        nextButton = findViewById(R.id.next_ongoingTicketStudent);
        downloadData = findViewById(R.id.downloadFile_ongoingTicketStudent);
        addToCalendar = findViewById(R.id.addTicketToCalendar_ongoingTicketStudent);

        nameTeacher1 = findViewById(R.id.nameTeacher1_ongoingTicketStudent);
        surnameTeacher1 = findViewById(R.id.surnameTeacher1_ongoingTicketStudent);

        nameTeacher2 = findViewById(R.id.nameTeacher2_ongoingTicketStudent);
        surnameTeacher2 = findViewById(R.id.surnameTeacher2_ongoingTicketStudent);

        informationTeacher1TextView = findViewById(R.id.informationTeacher1Text_ongoingTicketStudent);
        informationTeacher2TextView = findViewById(R.id.informationTeacher2Text_ongoingTicketStudent);

        nameStudent = getIntent().getStringExtra("nameStudent");
        surnameStudent = getIntent().getStringExtra("surnameStudent");
        facultyStudent = getIntent().getStringExtra("facultyStudent");
        fieldStudent = getIntent().getStringExtra("fieldStudent");

        firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("Accepted Applications");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                        String nameStudentCloud = document.getString("nameStudent");
                        String surnameStudentCloud = document.getString("surnameStudent");
                        String facultyStudentCloud = document.getString("facultyStudent");
                        String fieldStudentCloud = document.getString("fieldStudent");

                        if (nameStudentCloud.equals(nameStudent) && surnameStudentCloud.equals(surnameStudent)
                                && facultyStudentCloud.equals(facultyStudent) && fieldStudentCloud.equals(fieldStudent)) {
                            CloudFireOngoingTicketStudent cloudFireOngoingTicketStudent = new CloudFireOngoingTicketStudent(

                                    document.getId(),

                                    document.getString("nameTeacher"),
                                    document.getString("surnameTeacher"),
                                    document.getString("facultyTeacher"),
                                    document.getString("fieldTeacher"),

                                    document.getString("nameTeacher2"),
                                    document.getString("surnameTeacher2"),
                                    document.getString("facultyTeacher2"),
                                    document.getString("fieldTeacher2"),

                                    document.getString("semesterStudent"),
                                    document.getString("degreeStudent"),
                                    document.getString("indexNumberStudent"),

                                    document.getString("minuteTicket"),
                                    document.getString("hourTicket"),

                                    document.getString("dayTicket"),
                                    document.getString("monthTicket"),
                                    document.getString("yearTicket"),

                                    document.getString("reasonType"),
                                    document.getString("typeMeet"),

                                    document.getString("informationTeacher1"),
                                    document.getString("informationTeacher2"));

                            objectArrayList.add(cloudFireOngoingTicketStudent);
                        }
                    }
                }

                if (objectArrayList.size() == 0) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OngoingTicketStudent.this);
                    dialogBuilder.setTitle("Ongoing Ticket");
                    dialogBuilder.setMessage("No tickets to display!");
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setNeutralButton("Back to Panel", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(OngoingTicketStudent.this, PanelStudent.class);
                            intent.putExtra("Email", getIntent().getStringExtra("Email"));
                            startActivity(intent);
                        }
                    });
                    dialogBuilder.create();
                    dialogBuilder.show();
                }

                if (objectArrayList.size() == 1) {

                    ///
                    final CloudFireOngoingTicketStudent cloudFireOngoingTicketStudent = objectArrayList.get(0);
                    nameTeacher1.setText(cloudFireOngoingTicketStudent.nameTeacher);
                    surnameTeacher1.setText(cloudFireOngoingTicketStudent.surnameTeacher);
                    informationTeacher1TextView.setText(cloudFireOngoingTicketStudent.informationTeacher1);

                    if (cloudFireOngoingTicketStudent.surnameTeacher2.equals(" ") &&
                            cloudFireOngoingTicketStudent.nameTeacher2.equals(" ")) {
                        nameTeacher2.setVisibility(GONE);
                        surnameTeacher2.setVisibility(GONE);
                        informationTeacher2TextView.setText("No data!");
                        secondTeacherAddButton.setVisibility(VISIBLE);
                    } else {
                        nameTeacher2.setVisibility(VISIBLE);
                        surnameTeacher2.setVisibility(VISIBLE);
                        nameTeacher2.setText(cloudFireOngoingTicketStudent.nameTeacher2);
                        surnameTeacher2.setText(cloudFireOngoingTicketStudent.surnameTeacher2);
                        informationTeacher2TextView.setVisibility(VISIBLE);
                        informationTeacher2TextView.setText(cloudFireOngoingTicketStudent.informationTeacher2);
                        secondTeacherAddButton.setVisibility(GONE);
                    }


                    CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                    collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                    if (document.getId().equals(cloudFireOngoingTicketStudent.id)) {
                                        nameTeacher2Canceled = document.getString("nameTeacher2");
                                        surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                        facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                        fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                        if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                            if (document.getString("secondTeacher").equals("no")) {
                                                secondTeacherAddButton.setVisibility(GONE);
                                                nameTeacher2.setVisibility(VISIBLE);
                                                surnameTeacher2.setVisibility(VISIBLE);
                                                secondTeacherAddButton.setVisibility(GONE);
                                                nameTeacher2.setText("No data!");
                                                surnameTeacher2.setText("No data!");
                                                informationTeacher2TextView.setVisibility(VISIBLE);
                                                informationTeacher2TextView.setText("No data!");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
                    /////
                }

                if (objectArrayList.size() > 1) {
                    nextButton.setVisibility(VISIBLE);

                    ///
                    final CloudFireOngoingTicketStudent cloudFireOngoingTicketStudent = objectArrayList.get(0);
                    nameTeacher1.setText(cloudFireOngoingTicketStudent.nameTeacher);
                    surnameTeacher1.setText(cloudFireOngoingTicketStudent.surnameTeacher);
                    informationTeacher1TextView.setText(cloudFireOngoingTicketStudent.informationTeacher1);

                    if (cloudFireOngoingTicketStudent.surnameTeacher2.isEmpty() &&
                            cloudFireOngoingTicketStudent.nameTeacher2.isEmpty()) {
                        nameTeacher2.setVisibility(GONE);
                        surnameTeacher2.setVisibility(GONE);
                        informationTeacher2TextView.setText("No data!");
                        secondTeacherAddButton.setVisibility(VISIBLE);
                    } else {
                        nameTeacher2.setVisibility(VISIBLE);
                        surnameTeacher2.setVisibility(VISIBLE);
                        nameTeacher2.setText(cloudFireOngoingTicketStudent.nameTeacher2);
                        surnameTeacher2.setText(cloudFireOngoingTicketStudent.surnameTeacher2);
                        informationTeacher2TextView.setVisibility(VISIBLE);
                        informationTeacher2TextView.setText(cloudFireOngoingTicketStudent.informationTeacher2);
                        secondTeacherAddButton.setVisibility(GONE);
                    }


                    CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                    collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                    if (document.getId().equals(cloudFireOngoingTicketStudent.id)) {
                                        nameTeacher2Canceled = document.getString("nameTeacher2");
                                        surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                        facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                        fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                        if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                            if (document.getString("secondTeacher").equals("no")) {
                                                secondTeacherAddButton.setVisibility(GONE);
                                                nameTeacher2.setVisibility(VISIBLE);
                                                surnameTeacher2.setVisibility(VISIBLE);
                                                secondTeacherAddButton.setVisibility(GONE);
                                                nameTeacher2.setText("No data!");
                                                surnameTeacher2.setText("No data!");
                                                informationTeacher2TextView.setVisibility(VISIBLE);
                                                informationTeacher2TextView.setText("No data!");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
                    /////
                }
            }
        });

        addToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCalendar.setVisibility(GONE);
                CloudFireOngoingTicketStudent cloudFireOngoingTicketStudent = objectArrayList.get(indexTicket);
                int day = Integer.parseInt(cloudFireOngoingTicketStudent.dayTicket);
                int month = Integer.parseInt(cloudFireOngoingTicketStudent.monthTicket);
                int year = Integer.parseInt(cloudFireOngoingTicketStudent.yearTicket);

                int minute = Integer.parseInt(cloudFireOngoingTicketStudent.minuteTicket);
                int hour = Integer.parseInt(cloudFireOngoingTicketStudent.hourTicket);

                Calendar beginTime = Calendar.getInstance();
                Calendar endTime = Calendar.getInstance();
                beginTime.set(year, month - 1, day, hour, minute);
                endTime.set(year, month - 1, day, hour + 1, minute + 30);
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, "Meet " + "->" +
                                cloudFireOngoingTicketStudent.nameTeacher + " " + cloudFireOngoingTicketStudent.surnameTeacher)
                        .putExtra(CalendarContract.Events.DESCRIPTION, cloudFireOngoingTicketStudent.typeMeet)
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                startActivity(intent);
            }
        });


        secondTeacherAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloudFireOngoingTicketStudent cloudFireOngoingTicketStudent = objectArrayList.get(indexTicket);

                Intent intent = new Intent(OngoingTicketStudent.this, SecondTeacherStepOne.class);

                intent.putExtra("id", cloudFireOngoingTicketStudent.id);
                intent.putExtra("nameTeacher", cloudFireOngoingTicketStudent.nameTeacher);
                intent.putExtra("surnameTeacher", cloudFireOngoingTicketStudent.surnameTeacher);
                intent.putExtra("facultyTeacher", cloudFireOngoingTicketStudent.facultyTeacher);
                intent.putExtra("fieldTeacher", cloudFireOngoingTicketStudent.fieldTeacher);

                intent.putExtra("nameTeacher2", cloudFireOngoingTicketStudent.nameTeacher2);
                intent.putExtra("surnameTeacher2", cloudFireOngoingTicketStudent.surnameTeacher2);
                intent.putExtra("facultyTeacher2", cloudFireOngoingTicketStudent.facultyTeacher2);
                intent.putExtra("fieldTeacher2", cloudFireOngoingTicketStudent.fieldTeacher2);

                intent.putExtra("nameStudent", nameStudent);
                intent.putExtra("surnameStudent", surnameStudent);
                intent.putExtra("facultyStudent", facultyStudent);
                intent.putExtra("fieldStudent", fieldStudent);
                intent.putExtra("degreeStudent", cloudFireOngoingTicketStudent.degreeStudent);
                intent.putExtra("semesterStudent", cloudFireOngoingTicketStudent.semesterStudent);
                intent.putExtra("indexNumberStudent", cloudFireOngoingTicketStudent.indexNumberStudent);

                intent.putExtra("minuteTicket", cloudFireOngoingTicketStudent.minuteTicket);
                intent.putExtra("hourTicket", cloudFireOngoingTicketStudent.hourTicket);

                intent.putExtra("dayTicket", cloudFireOngoingTicketStudent.dayTicket);
                intent.putExtra("monthTicket", cloudFireOngoingTicketStudent.monthTicket);
                intent.putExtra("yearTicket", cloudFireOngoingTicketStudent.yearTicket);

                intent.putExtra("reasonType", cloudFireOngoingTicketStudent.reasonType);
                intent.putExtra("typeMeet", cloudFireOngoingTicketStudent.typeMeet);

                intent.putExtra("informationTeacher1", cloudFireOngoingTicketStudent.informationTeacher1);
                intent.putExtra("informationTeacher2", cloudFireOngoingTicketStudent.informationTeacher2);

                intent.putExtra("Email", getIntent().getStringExtra("Email"));
                startActivity(intent);
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousButton.setVisibility(View.VISIBLE);
                addToCalendar.setVisibility(VISIBLE);
                int sizeList = objectArrayList.size();
                indexTicket++;

                if (indexTicket == (sizeList - 1)) {
                    nextButton.setVisibility(GONE);
                    final CloudFireOngoingTicketStudent cloudFireOngoingTicketStudent = objectArrayList.get(indexTicket);


                    ///
                    nameTeacher1.setText(cloudFireOngoingTicketStudent.nameTeacher);
                    surnameTeacher1.setText(cloudFireOngoingTicketStudent.surnameTeacher);
                    informationTeacher1TextView.setText(cloudFireOngoingTicketStudent.informationTeacher1);

                    if (cloudFireOngoingTicketStudent.surnameTeacher2.equals(" ") &&
                            cloudFireOngoingTicketStudent.nameTeacher2.equals(" ")) {
                        nameTeacher2.setVisibility(GONE);
                        surnameTeacher2.setVisibility(GONE);
                        informationTeacher2TextView.setText("No data!");
                        secondTeacherAddButton.setVisibility(VISIBLE);
                    } else {
                        nameTeacher2.setVisibility(VISIBLE);
                        surnameTeacher2.setVisibility(VISIBLE);
                        nameTeacher2.setText(cloudFireOngoingTicketStudent.nameTeacher2);
                        surnameTeacher2.setText(cloudFireOngoingTicketStudent.surnameTeacher2);
                        informationTeacher2TextView.setVisibility(VISIBLE);
                        informationTeacher2TextView.setText(cloudFireOngoingTicketStudent.informationTeacher2);
                        secondTeacherAddButton.setVisibility(GONE);
                    }


                    CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                    collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                    if (document.getId().equals(cloudFireOngoingTicketStudent.id)) {
                                        nameTeacher2Canceled = document.getString("nameTeacher2");
                                        surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                        facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                        fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                        if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                            if (document.getString("secondTeacher").equals("no")) {
                                                secondTeacherAddButton.setVisibility(GONE);
                                                nameTeacher2.setVisibility(VISIBLE);
                                                surnameTeacher2.setVisibility(VISIBLE);
                                                secondTeacherAddButton.setVisibility(GONE);
                                                nameTeacher2.setText("No data!");
                                                surnameTeacher2.setText("No data!");
                                                informationTeacher2TextView.setVisibility(VISIBLE);
                                                informationTeacher2TextView.setText("No data!");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
                    /////
                }


                if (indexTicket < sizeList) {
                    final CloudFireOngoingTicketStudent cloudFireOngoingTicketStudent = objectArrayList.get(indexTicket);


                    ///
                    nameTeacher1.setText(cloudFireOngoingTicketStudent.nameTeacher);
                    surnameTeacher1.setText(cloudFireOngoingTicketStudent.surnameTeacher);
                    informationTeacher1TextView.setText(cloudFireOngoingTicketStudent.informationTeacher1);

                    if (cloudFireOngoingTicketStudent.surnameTeacher2.equals(" ") &&
                            cloudFireOngoingTicketStudent.nameTeacher2.equals(" ")) {
                        nameTeacher2.setVisibility(GONE);
                        surnameTeacher2.setVisibility(GONE);
                        informationTeacher2TextView.setText("No data!");
                        secondTeacherAddButton.setVisibility(VISIBLE);
                    } else {
                        nameTeacher2.setVisibility(VISIBLE);
                        surnameTeacher2.setVisibility(VISIBLE);
                        nameTeacher2.setText(cloudFireOngoingTicketStudent.nameTeacher2);
                        surnameTeacher2.setText(cloudFireOngoingTicketStudent.surnameTeacher2);
                        informationTeacher2TextView.setVisibility(VISIBLE);
                        informationTeacher2TextView.setText(cloudFireOngoingTicketStudent.informationTeacher2);
                        secondTeacherAddButton.setVisibility(GONE);
                    }


                    CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                    collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                    if (document.getId().equals(cloudFireOngoingTicketStudent.id)) {
                                        nameTeacher2Canceled = document.getString("nameTeacher2");
                                        surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                        facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                        fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                        if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                            if (document.getString("secondTeacher").equals("no")) {
                                                secondTeacherAddButton.setVisibility(GONE);
                                                nameTeacher2.setVisibility(VISIBLE);
                                                surnameTeacher2.setVisibility(VISIBLE);
                                                secondTeacherAddButton.setVisibility(GONE);
                                                nameTeacher2.setText("No data!");
                                                surnameTeacher2.setText("No data!");
                                                informationTeacher2TextView.setVisibility(VISIBLE);
                                                informationTeacher2TextView.setText("No data!");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
                    /////

                } else {
                    nextButton.setVisibility(GONE);
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexTicket--;
                addToCalendar.setVisibility(VISIBLE);
                if (indexTicket == 0) {
                    previousButton.setVisibility(GONE);
                    final CloudFireOngoingTicketStudent cloudFireOngoingTicketStudent = objectArrayList.get(indexTicket);


                    ///
                    nameTeacher1.setText(cloudFireOngoingTicketStudent.nameTeacher);
                    surnameTeacher1.setText(cloudFireOngoingTicketStudent.surnameTeacher);
                    informationTeacher1TextView.setText(cloudFireOngoingTicketStudent.informationTeacher1);

                    if (cloudFireOngoingTicketStudent.surnameTeacher2.equals(" ") &&
                            cloudFireOngoingTicketStudent.nameTeacher2.equals(" ")) {
                        nameTeacher2.setVisibility(GONE);
                        surnameTeacher2.setVisibility(GONE);
                        informationTeacher2TextView.setText("No data!");
                        secondTeacherAddButton.setVisibility(VISIBLE);
                    } else {
                        nameTeacher2.setVisibility(VISIBLE);
                        surnameTeacher2.setVisibility(VISIBLE);
                        nameTeacher2.setText(cloudFireOngoingTicketStudent.nameTeacher2);
                        surnameTeacher2.setText(cloudFireOngoingTicketStudent.surnameTeacher2);
                        informationTeacher2TextView.setVisibility(VISIBLE);
                        informationTeacher2TextView.setText(cloudFireOngoingTicketStudent.informationTeacher2);
                        secondTeacherAddButton.setVisibility(GONE);
                    }


                    CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                    collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                    if (document.getId().equals(cloudFireOngoingTicketStudent.id)) {
                                        nameTeacher2Canceled = document.getString("nameTeacher2");
                                        surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                        facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                        fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                        if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                            if (document.getString("secondTeacher").equals("no")) {
                                                secondTeacherAddButton.setVisibility(GONE);
                                                nameTeacher2.setVisibility(VISIBLE);
                                                surnameTeacher2.setVisibility(VISIBLE);
                                                secondTeacherAddButton.setVisibility(GONE);
                                                nameTeacher2.setText("No data!");
                                                surnameTeacher2.setText("No data!");
                                                informationTeacher2TextView.setVisibility(VISIBLE);
                                                informationTeacher2TextView.setText("No data!");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
                    /////

                    nextButton.setVisibility(VISIBLE);
                }

                if (indexTicket > 0) {
                    final CloudFireOngoingTicketStudent cloudFireOngoingTicketStudent = objectArrayList.get(indexTicket);


                    ///
                    nameTeacher1.setText(cloudFireOngoingTicketStudent.nameTeacher);
                    surnameTeacher1.setText(cloudFireOngoingTicketStudent.surnameTeacher);
                    informationTeacher1TextView.setText(cloudFireOngoingTicketStudent.informationTeacher1);

                    if (cloudFireOngoingTicketStudent.surnameTeacher2.equals(" ") &&
                            cloudFireOngoingTicketStudent.nameTeacher2.equals(" ")) {
                        nameTeacher2.setVisibility(GONE);
                        surnameTeacher2.setVisibility(GONE);
                        informationTeacher2TextView.setText("No data!");
                        secondTeacherAddButton.setVisibility(VISIBLE);
                    } else {
                        nameTeacher2.setVisibility(VISIBLE);
                        surnameTeacher2.setVisibility(VISIBLE);
                        nameTeacher2.setText(cloudFireOngoingTicketStudent.nameTeacher2);
                        surnameTeacher2.setText(cloudFireOngoingTicketStudent.surnameTeacher2);
                        informationTeacher2TextView.setVisibility(VISIBLE);
                        informationTeacher2TextView.setText(cloudFireOngoingTicketStudent.informationTeacher2);
                        secondTeacherAddButton.setVisibility(GONE);
                    }


                    CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                    collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                    if (document.getId().equals(cloudFireOngoingTicketStudent.id)) {
                                        nameTeacher2Canceled = document.getString("nameTeacher2");
                                        surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                        facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                        fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                        if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                            if (document.getString("secondTeacher").equals("no")) {
                                                secondTeacherAddButton.setVisibility(GONE);
                                                nameTeacher2.setVisibility(VISIBLE);
                                                surnameTeacher2.setVisibility(VISIBLE);
                                                secondTeacherAddButton.setVisibility(GONE);
                                                nameTeacher2.setText("No data!");
                                                surnameTeacher2.setText("No data!");
                                                informationTeacher2TextView.setVisibility(VISIBLE);
                                                informationTeacher2TextView.setText("No data!");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
                    /////
                    nextButton.setVisibility(VISIBLE);
                } else {
                    previousButton.setVisibility(GONE);
                }
            }
        });


    }

    public void retrievePDF(View view) {
        final CloudFireOngoingTicketStudent cloudFireOngoingTicketStudent = objectArrayList.get(indexTicket);
        Intent intent = new Intent(getApplicationContext(), RetrievePDF.class);
        String val = nameStudent + surnameStudent + "to" +
                cloudFireOngoingTicketStudent.nameTeacher + cloudFireOngoingTicketStudent.surnameTeacher
                + cloudFireOngoingTicketStudent.dayTicket + cloudFireOngoingTicketStudent.monthTicket + cloudFireOngoingTicketStudent.yearTicket
                + cloudFireOngoingTicketStudent.minuteTicket + cloudFireOngoingTicketStudent.hourTicket;
        intent.putExtra("val", val);
        startActivity(intent);
    }
}

class CloudFireOngoingTicketStudent {

    String id = " ";

    String nameTeacher = " ";
    String surnameTeacher = " ";
    String facultyTeacher = " ";
    String fieldTeacher = " ";

    String nameTeacher2 = " ";
    String surnameTeacher2 = " ";
    String facultyTeacher2 = " ";
    String fieldTeacher2 = " ";

    String semesterStudent = " ";
    String degreeStudent = " ";
    String indexNumberStudent = " ";

    String minuteTicket = " ";
    String hourTicket = " ";
    String dayTicket = " ";

    String monthTicket = " ";
    String yearTicket = " ";

    String reasonType = " ";
    String typeMeet = " ";

    String informationTeacher1 = " ";
    String informationTeacher2 = " ";

    public CloudFireOngoingTicketStudent(String id, String nameTeacher, String surnameTeacher, String facultyTeacher, String fieldTeacher,
                                         String nameTeacher2, String surnameTeacher2, String facultyTeacher2, String fieldTeacher2,
                                         String semesterStudent, String degreeStudent, String indexNumberStudent,
                                         String minuteTicket, String hourTicket, String dayTicket, String monthTicket, String yearTicket,
                                         String reasonType, String typeMeet, String informationTeacher1, String informationTeacher2) {

        this.id = id;
        this.nameTeacher = nameTeacher;
        this.surnameTeacher = surnameTeacher;
        this.facultyTeacher = facultyTeacher;
        this.fieldTeacher = fieldTeacher;
        this.nameTeacher2 = nameTeacher2;
        this.surnameTeacher2 = surnameTeacher2;
        this.facultyTeacher2 = facultyTeacher2;
        this.fieldTeacher2 = fieldTeacher2;
        this.semesterStudent = semesterStudent;
        this.degreeStudent = degreeStudent;
        this.indexNumberStudent = indexNumberStudent;
        this.minuteTicket = minuteTicket;
        this.hourTicket = hourTicket;
        this.dayTicket = dayTicket;
        this.monthTicket = monthTicket;
        this.yearTicket = yearTicket;
        this.reasonType = reasonType;
        this.typeMeet = typeMeet;
        this.informationTeacher1 = informationTeacher1;
        this.informationTeacher2 = informationTeacher2;
    }
}