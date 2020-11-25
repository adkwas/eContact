package com.example.econtact;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.List;
import java.util.Objects;

import static android.view.View.VISIBLE;

public class RejectTickets extends AppCompatActivity {
    Button previousButton, nextButton;
    TextView nameTeacher, surnameTeacher, facultyTeacher, fieldTeacher, typeMeet, dataMeet, timeMeet, reasonMeet;
    String nameStudent, surnameStudent, facultyStudent, fieldStudent;
    FirebaseFirestore firebaseFirestore;
    String day, month, year, minute, hour;
    int val = 0;
    List<CloudFireRejectTickets> objectArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_tickets);

        //Get ID for all Textviews and Buttons
        previousButton = findViewById(R.id.previous_rejectTickets);
        nextButton = findViewById(R.id.next_rejectTickets);
        nameTeacher = findViewById(R.id.nameLecturer_rejectTickets);
        surnameTeacher = findViewById(R.id.surnameLecturer_rejectTickets);
        facultyTeacher = findViewById(R.id.facultyLecturer_rejectTickets);
        fieldTeacher = findViewById(R.id.fieldLecturer_rejectTickets);
        typeMeet = findViewById(R.id.typeMeet_rejectTickets);
        dataMeet = findViewById(R.id.dataMeet_rejectTickets);
        timeMeet = findViewById(R.id.timeMeet_rejectTickets);
        reasonMeet = findViewById(R.id.reasonMeet_rejectTickets);

        //Get user data with Panel Student
        nameStudent = getIntent().getStringExtra("nameStudent");
        surnameStudent = getIntent().getStringExtra("surnameStudent");
        facultyStudent = getIntent().getStringExtra("facultyStudent");
        fieldStudent = getIntent().getStringExtra("fieldStudent");

        //Get data from the Canceled Application collection.
        //Data from a given document - tickets are saved in the cloudFirePendingTickets object
        //Saved in the objectArrayList
        firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                        //Retrieving student data from a specific document
                        String nameVal = document.getString("nameStudent");
                        String surnameVal = document.getString("surnameStudent");
                        String facultyVal = document.getString("facultyStudent");
                        String fieldVal = document.getString("fieldStudent");

                        //Comparison of data from a specific document and data downloaded from the Student Panel.
                        // If they are correct, write the data to the object and then to the list
                        assert nameVal != null;
                        if (nameVal.equals(nameStudent)) {
                            assert surnameVal != null;
                            if (surnameVal.equals(surnameStudent)) {
                                assert facultyVal != null;
                                if (facultyVal.equals(facultyStudent)) {
                                    assert fieldVal != null;
                                    if (fieldVal.equals(fieldStudent)) {
                                        day = document.getString("dayTicket");
                                        month = document.getString("monthTicket");
                                        year = document.getString("yearTicket");
                                        minute = document.getString("minuteTicket");
                                        hour = document.getString("hourTicket");
                                        CloudFireRejectTickets cloudFireConfirmTickets = new CloudFireRejectTickets(document.getString("nameTeacher"), document.getString("surnameTeacher"), document.getString("facultyTeacher"),
                                                document.getString("fieldTeacher"), document.getString("typeMeet"), day + "." + month + "." + year,
                                                hour + ":" + minute, document.getString("reasonType"));
                                        objectArrayList.add(cloudFireConfirmTickets);
                                    }
                                }
                            }
                        }
                    }

                    //If the list of applications is late -
                    //display a banner about no applications with a button to return to Panel Student
                    if (objectArrayList.size() == 0) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RejectTickets.this);
                        dialogBuilder.setTitle("Confirm Tickets");
                        dialogBuilder.setMessage("No tickets to display!");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setNeutralButton("Back to Panel", new Dialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(RejectTickets.this, PanelStudent.class);
                                intent.putExtra("Email", getIntent().getStringExtra("Email"));
                                startActivity(intent);
                            }
                        });
                        dialogBuilder.create();
                        dialogBuilder.show();
                    } else {
                        CloudFireRejectTickets cloudFireRejectTickets = objectArrayList.get(0);
                        //If the list of tickets has one ticket - display it on the application's desktop
                        if (objectArrayList.size() == 1) {
                            nameTeacher.setText("Name: " + cloudFireRejectTickets.nameTeacher);
                            surnameTeacher.setText("Surname: " + cloudFireRejectTickets.surnameTeacher);
                            facultyTeacher.setText(cloudFireRejectTickets.facultyTeacher);
                            fieldTeacher.setText("Field: " + cloudFireRejectTickets.fieldTeacher);
                            typeMeet.setText("Type Meet: " + cloudFireRejectTickets.meetType);
                            dataMeet.setText("Date: " + cloudFireRejectTickets.dataMeet);
                            timeMeet.setText("Time: " + cloudFireRejectTickets.timeMeet);
                            reasonMeet.setText("Reason: " + cloudFireRejectTickets.reason);
                        }

                        //If the ticket list has several tickets - display them on the application's desktop
                        //Activate NextButton
                        if (objectArrayList.size() > 1) {
                            nextButton.setVisibility(VISIBLE);
                            nameTeacher.setText("Name: " + cloudFireRejectTickets.nameTeacher);
                            surnameTeacher.setText("Surname: " + cloudFireRejectTickets.surnameTeacher);
                            facultyTeacher.setText(cloudFireRejectTickets.facultyTeacher);
                            fieldTeacher.setText("Field: " + cloudFireRejectTickets.fieldTeacher);
                            typeMeet.setText("Type Meet: " + cloudFireRejectTickets.meetType);
                            dataMeet.setText("Date: " + cloudFireRejectTickets.dataMeet);
                            timeMeet.setText("Time: " + cloudFireRejectTickets.timeMeet);
                            reasonMeet.setText("Reason: " + cloudFireRejectTickets.reason);
                        }
                    }
                }
            }
        });


        //If click a next button:
        //Set the previous button to visible.
        //If there are still issues on the list,
        //Display another issue until the last one.
        //When we get to the last report - turn off the next Button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousButton.setVisibility(VISIBLE);
                int sizeList = objectArrayList.size();
                val++;

                if (val == (sizeList - 1)) {
                    nextButton.setVisibility(View.GONE);
                    CloudFireRejectTickets cloudFireRejectTickets = objectArrayList.get(val);
                    nameTeacher.setText("Name: " + cloudFireRejectTickets.nameTeacher);
                    surnameTeacher.setText("Surname: " + cloudFireRejectTickets.surnameTeacher);
                    facultyTeacher.setText(cloudFireRejectTickets.facultyTeacher);
                    fieldTeacher.setText("Field: " + cloudFireRejectTickets.fieldTeacher);
                    typeMeet.setText("Type Meet: " + cloudFireRejectTickets.meetType);
                    dataMeet.setText("Date: " + cloudFireRejectTickets.dataMeet);
                    timeMeet.setText("Time: " + cloudFireRejectTickets.timeMeet);
                    reasonMeet.setText("Reason: " + cloudFireRejectTickets.reason);
                }

                if (val < sizeList) {
                    CloudFireRejectTickets cloudFireRejectTickets = objectArrayList.get(val);
                    nameTeacher.setText("Name: " + cloudFireRejectTickets.nameTeacher);
                    surnameTeacher.setText("Surname: " + cloudFireRejectTickets.surnameTeacher);
                    facultyTeacher.setText(cloudFireRejectTickets.facultyTeacher);
                    fieldTeacher.setText("Field: " + cloudFireRejectTickets.fieldTeacher);
                    typeMeet.setText("Type Meet: " + cloudFireRejectTickets.meetType);
                    dataMeet.setText("Date: " + cloudFireRejectTickets.dataMeet);
                    timeMeet.setText("Time: " + cloudFireRejectTickets.timeMeet);
                    reasonMeet.setText("Reason: " + cloudFireRejectTickets.reason);
                } else {
                    nextButton.setVisibility(View.GONE);
                }

            }
        });


        //If click a previous button
        //Go to the previous request and set the parameters on the display.
        //If we get to the first notification - turn off the previous button
        //Activate Next Button
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val--;
                if (val == 0) {
                    previousButton.setVisibility(View.GONE);
                    CloudFireRejectTickets cloudFireRejectTickets = objectArrayList.get(val);
                    nameTeacher.setText("Name: " + cloudFireRejectTickets.nameTeacher);
                    surnameTeacher.setText("Surname: " + cloudFireRejectTickets.surnameTeacher);
                    facultyTeacher.setText(cloudFireRejectTickets.facultyTeacher);
                    fieldTeacher.setText("Field: " + cloudFireRejectTickets.fieldTeacher);
                    typeMeet.setText("Type Meet: " + cloudFireRejectTickets.meetType);
                    dataMeet.setText("Date: " + cloudFireRejectTickets.dataMeet);
                    timeMeet.setText("Time: " + cloudFireRejectTickets.timeMeet);
                    reasonMeet.setText("Reason: " + cloudFireRejectTickets.reason);
                    nextButton.setVisibility(VISIBLE);
                }

                if (val > 0) {
                    CloudFireRejectTickets cloudFireRejectTickets= objectArrayList.get(val);
                    nameTeacher.setText("Name: " + cloudFireRejectTickets.nameTeacher);
                    surnameTeacher.setText("Surname: " + cloudFireRejectTickets.surnameTeacher);
                    facultyTeacher.setText(cloudFireRejectTickets.facultyTeacher);
                    fieldTeacher.setText("Field: " + cloudFireRejectTickets.fieldTeacher);
                    typeMeet.setText("Type Meet: " + cloudFireRejectTickets.meetType);
                    dataMeet.setText("Date: " + cloudFireRejectTickets.dataMeet);
                    timeMeet.setText("Time: " + cloudFireRejectTickets.timeMeet);
                    reasonMeet.setText("Reason: " + cloudFireRejectTickets.reason);
                    nextButton.setVisibility(VISIBLE);
                } else {
                    previousButton.setVisibility(View.GONE);
                }
            }
        });
    }

    //Class CloudFireConfirmTickets
    //The class is used to save read student data from Cloud Fire.
    //Class objects are single entries to the same teacher.
    //The class contains all the data concerning the application and the data of a single student.
    //The class objects are then listed in the list.
    public static class CloudFireRejectTickets {
        String nameTeacher;
        String surnameTeacher;
        String facultyTeacher;
        String fieldTeacher;
        String meetType;
        String dataMeet;
        String timeMeet;
        String reason;

        public CloudFireRejectTickets(String nameTeacher, String surnameTeacher, String facultyTeacher,
                                      String fieldTeacher, String meetType, String dataMeet, String timeMeet, String reason) {
            this.nameTeacher = nameTeacher;
            this.surnameTeacher = surnameTeacher;
            this.facultyTeacher = facultyTeacher;
            this.fieldTeacher = fieldTeacher;
            this.meetType = meetType;
            this.dataMeet = dataMeet;
            this.timeMeet = timeMeet;
            this.reason = reason;
        }
    }
}