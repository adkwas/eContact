package com.example.econtact;

import android.annotation.SuppressLint;
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

public class AcceptedTickets extends AppCompatActivity {
    Button previousButton, nextButton;
    TextView nameStudent, surnameStudent, facultyStudent, fieldStudent, degreeStudent, semesterStudent, typeMeet, dataMeet, timeMeet, reasonMeet, indexNumberStudent;
    String nameTeacher, surnameTeacher, facultyTeacher, fieldTeacher;
    FirebaseFirestore firebaseFirestore;
    String day, month, year, minute, hour;
    int indexTicket = 0;
    List<CloudFireAcceptedTicket> objectArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_tickets);

        //Get ID for all Textviews and Buttons
        previousButton = findViewById(R.id.previous_acceptedTickets);
        nextButton = findViewById(R.id.next_acceptedTickets);
        nameStudent = findViewById(R.id.nameStudent_acceptedTickets);
        surnameStudent = findViewById(R.id.surnameStudent_acceptedTickets);
        facultyStudent = findViewById(R.id.facultyStudent_acceptedTickets);
        fieldStudent = findViewById(R.id.fieldStudent_acceptedTickets);
        degreeStudent = findViewById(R.id.degreeStudent_acceptedTickets);
        semesterStudent = findViewById(R.id.semesterStudent_acceptedTickets);
        typeMeet = findViewById(R.id.typeMeet_acceptedTickets);
        dataMeet = findViewById(R.id.dataMeet_acceptedTickets);
        timeMeet = findViewById(R.id.timeMeet_acceptedTickets);
        reasonMeet = findViewById(R.id.reasonMeet_acceptedTickets);
        indexNumberStudent = findViewById(R.id.indexNumberStudent_acceptedTickets);

        //Get data teacher with Panel Teacher
        nameTeacher = getIntent().getStringExtra("nameTeacher");
        surnameTeacher = getIntent().getStringExtra("surnameTeacher");
        facultyTeacher = getIntent().getStringExtra("facultyTeacher");
        fieldTeacher = getIntent().getStringExtra("fieldTeacher");

        //Search for tickets that are specific to a given teacher.
        firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("Accepted Applications");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                        //Downloading teacher data from a single application from Cloud Fire
                        String nameVal = document.getString("nameTeacher");
                        String surnameVal = document.getString("surnameTeacher");
                        String facultyVal = document.getString("facultyTeacher");
                        String fieldVal = document.getString("fieldTeacher");

                        //If the data retrieved from a previous activity (containing teacher data) and data from Cloud Fire
                        //Save the data to the CloudFireCheckNewTicket object and
                        //Enter it into the list of ticket objects.
                        assert nameVal != null;
                        if (nameVal.equals(nameTeacher)) {
                            assert surnameVal != null;
                            if (surnameVal.equals(surnameTeacher)) {
                                assert facultyVal != null;
                                if (facultyVal.equals(facultyTeacher)) {
                                    assert fieldVal != null;
                                    if (fieldVal.equals(fieldTeacher)) {
                                        day = document.getString("dayTicket");
                                        month = document.getString("monthTicket");
                                        year = document.getString("yearTicket");
                                        minute = document.getString("minuteTicket");
                                        hour = document.getString("hourTicket");
                                        CloudFireAcceptedTicket cloudFireAcceptedTicket = new CloudFireAcceptedTicket(document.getString("nameStudent"), document.getString("surnameStudent"), document.getString("facultyStudent"),
                                                document.getString("fieldStudent"), document.getString("degreeStudent"), document.getString("semesterStudent"), document.getString("typeMeet"), day + "." + month + "." + year,
                                                hour + ":" + minute, document.getString("reasonType"), document.getString("indexNumberStudent"));
                                        objectArrayList.add(cloudFireAcceptedTicket);
                                    }
                                }
                            }
                        }
                    }

                    //If the list of applications is late -
                    //display a banner about no applications with a button to return to Panel Teacher
                    if (objectArrayList.size() == 0) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AcceptedTickets.this);
                        dialogBuilder.setTitle("Accepted applications");
                        dialogBuilder.setMessage("No tickets to display!");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setNeutralButton("Back to Panel", new Dialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {

                                startActivity(new Intent(AcceptedTickets.this, PanelTeacher.class));
                            }
                        });
                        dialogBuilder.create();
                        dialogBuilder.show();
                    }
                    //If the list of tickets has one ticket - display it on the application's desktop
                    if (objectArrayList.size() == 1) {
                        CloudFireAcceptedTicket cloudFireAcceptedTicket = objectArrayList.get(0);
                        nameStudent.setText("Name: " + cloudFireAcceptedTicket.nameStudent);
                        surnameStudent.setText("Surname: " + cloudFireAcceptedTicket.surnameStudent);
                        facultyStudent.setText(cloudFireAcceptedTicket.facultyStudent);
                        fieldStudent.setText("Field: " + cloudFireAcceptedTicket.fieldStudent);
                        degreeStudent.setText(cloudFireAcceptedTicket.degreeStudent);
                        semesterStudent.setText("Semester: " + cloudFireAcceptedTicket.semesterStudent);
                        indexNumberStudent.setText("Index: " + cloudFireAcceptedTicket.indexNumber);
                        typeMeet.setText("Type Meet: " + cloudFireAcceptedTicket.meetType);
                        dataMeet.setText("Date: " + cloudFireAcceptedTicket.dataMeet);
                        timeMeet.setText("Time: " + cloudFireAcceptedTicket.timeMeet);
                        reasonMeet.setText("Reason: " + cloudFireAcceptedTicket.reason);
                    }

                    //If the ticket list has several tickets - display them on the application's desktop
                    //Activate NextButton
                    if (objectArrayList.size() > 1) {
                        nextButton.setVisibility(VISIBLE);
                        CloudFireAcceptedTicket cloudFireAcceptedTicket = objectArrayList.get(0);
                        nameStudent.setText("Name: " + cloudFireAcceptedTicket.nameStudent);
                        surnameStudent.setText("Surname: " + cloudFireAcceptedTicket.surnameStudent);
                        facultyStudent.setText(cloudFireAcceptedTicket.facultyStudent);
                        fieldStudent.setText("Field: " + cloudFireAcceptedTicket.fieldStudent);
                        degreeStudent.setText(cloudFireAcceptedTicket.degreeStudent);
                        semesterStudent.setText("Semester: " + cloudFireAcceptedTicket.semesterStudent);
                        indexNumberStudent.setText("Index: " + cloudFireAcceptedTicket.indexNumber);
                        typeMeet.setText("Type Meet: " + cloudFireAcceptedTicket.meetType);
                        dataMeet.setText("Date: " + cloudFireAcceptedTicket.dataMeet);
                        timeMeet.setText("Time: " + cloudFireAcceptedTicket.timeMeet);
                        reasonMeet.setText("Reason: " + cloudFireAcceptedTicket.reason);
                    }
                }
            }
        });

        //If click a next button:
        //Set the previous button to visible.
        //If there are still issues on the list,
        //display another issue until the last one.
        //When we get to the last report - turn off the next Button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousButton.setVisibility(View.VISIBLE);
                int sizeList = objectArrayList.size();
                indexTicket++;

                if (indexTicket == (sizeList - 1)) {
                    nextButton.setVisibility(View.GONE);
                    CloudFireAcceptedTicket cloudFireAcceptedTicket = objectArrayList.get(indexTicket);
                    nameStudent.setText("Name: " + cloudFireAcceptedTicket.nameStudent);
                    surnameStudent.setText("Surname: " + cloudFireAcceptedTicket.surnameStudent);
                    facultyStudent.setText(cloudFireAcceptedTicket.facultyStudent);
                    fieldStudent.setText("Field: " + cloudFireAcceptedTicket.fieldStudent);
                    degreeStudent.setText(cloudFireAcceptedTicket.degreeStudent);
                    semesterStudent.setText("Semester: " + cloudFireAcceptedTicket.semesterStudent);
                    indexNumberStudent.setText("Index: " + cloudFireAcceptedTicket.indexNumber);
                    typeMeet.setText("Type Meet: " + cloudFireAcceptedTicket.meetType);
                    dataMeet.setText("Date: " + cloudFireAcceptedTicket.dataMeet);
                    timeMeet.setText("Time: " + cloudFireAcceptedTicket.timeMeet);
                    reasonMeet.setText("Reason: " + cloudFireAcceptedTicket.reason);
                }


                if (indexTicket < sizeList) {
                    CloudFireAcceptedTicket cloudFireAcceptedTicket = objectArrayList.get(indexTicket);
                    nameStudent.setText("Name: " + cloudFireAcceptedTicket.nameStudent);
                    surnameStudent.setText("Surname: " + cloudFireAcceptedTicket.surnameStudent);
                    facultyStudent.setText(cloudFireAcceptedTicket.facultyStudent);
                    fieldStudent.setText("Field: " + cloudFireAcceptedTicket.fieldStudent);
                    degreeStudent.setText(cloudFireAcceptedTicket.degreeStudent);
                    semesterStudent.setText("Semester: " + cloudFireAcceptedTicket.semesterStudent);
                    indexNumberStudent.setText("Index: " + cloudFireAcceptedTicket.indexNumber);
                    typeMeet.setText("Type Meet: " + cloudFireAcceptedTicket.meetType);
                    dataMeet.setText("Date: " + cloudFireAcceptedTicket.dataMeet);
                    timeMeet.setText("Time: " + cloudFireAcceptedTicket.timeMeet);
                    reasonMeet.setText("Reason: " + cloudFireAcceptedTicket.reason);
                } else {
                    nextButton.setVisibility(View.GONE);
                }
            }
        });

        //If click a previous button
        //Go to the previous request and set the parameters on the display.
        //If we get to the first notification - turn off the previous button
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexTicket--;

                if (indexTicket == 0) {
                    previousButton.setVisibility(View.GONE);
                    CloudFireAcceptedTicket cloudFireAcceptedTicket = objectArrayList.get(indexTicket);
                    nameStudent.setText("Name: " + cloudFireAcceptedTicket.nameStudent);
                    surnameStudent.setText("Surname: " + cloudFireAcceptedTicket.surnameStudent);
                    facultyStudent.setText(cloudFireAcceptedTicket.facultyStudent);
                    fieldStudent.setText("Field: " + cloudFireAcceptedTicket.fieldStudent);
                    degreeStudent.setText(cloudFireAcceptedTicket.degreeStudent);
                    semesterStudent.setText("Semester: " + cloudFireAcceptedTicket.semesterStudent);
                    indexNumberStudent.setText("Index: " + cloudFireAcceptedTicket.indexNumber);
                    typeMeet.setText("Type Meet: " + cloudFireAcceptedTicket.meetType);
                    dataMeet.setText("Date: " + cloudFireAcceptedTicket.dataMeet);
                    timeMeet.setText("Time: " + cloudFireAcceptedTicket.timeMeet);
                    reasonMeet.setText("Reason: " + cloudFireAcceptedTicket.reason);
                    nextButton.setVisibility(VISIBLE);
                }

                if (indexTicket > 0) {
                    CloudFireAcceptedTicket cloudFireAcceptedTicket = objectArrayList.get(indexTicket);
                    nameStudent.setText("Name: " + cloudFireAcceptedTicket.nameStudent);
                    surnameStudent.setText("Surname: " + cloudFireAcceptedTicket.surnameStudent);
                    facultyStudent.setText(cloudFireAcceptedTicket.facultyStudent);
                    fieldStudent.setText("Field: " + cloudFireAcceptedTicket.fieldStudent);
                    degreeStudent.setText( cloudFireAcceptedTicket.degreeStudent);
                    semesterStudent.setText("Semester: " + cloudFireAcceptedTicket.semesterStudent);
                    indexNumberStudent.setText("Index: " + cloudFireAcceptedTicket.indexNumber);
                    typeMeet.setText("Type Meet: " + cloudFireAcceptedTicket.meetType);
                    dataMeet.setText("Date: " + cloudFireAcceptedTicket.dataMeet);
                    timeMeet.setText("Time: " + cloudFireAcceptedTicket.timeMeet);
                    reasonMeet.setText("Reason: " + cloudFireAcceptedTicket.reason);
                    nextButton.setVisibility(VISIBLE);
                } else {
                    previousButton.setVisibility(View.GONE);
                }
            }
        });


    }

    //Class CloudFireCheckNewTicket
    //The class is used to save read student data from Cloud Fire.
    //Class objects are single entries to the same teacher.
    //The class contains all the data concerning the application and the data of a single student.
    //The class objects are then listed in the list.
    public static class CloudFireAcceptedTicket {
        String nameStudent;
        String surnameStudent;
        String facultyStudent;
        String fieldStudent;
        String degreeStudent;
        String semesterStudent;
        String meetType;
        String dataMeet;
        String timeMeet;
        String reason;
        String indexNumber;

        public CloudFireAcceptedTicket(String nameStudent, String surnameStudent, String facultyStudent,
                                       String fieldStudent, String degreeStudent, String semesterStudent, String meetType, String dataMeet, String timeMeet, String reason, String indexNumber) {
            this.nameStudent = nameStudent;
            this.surnameStudent = surnameStudent;
            this.facultyStudent = facultyStudent;
            this.fieldStudent = fieldStudent;
            this.degreeStudent = degreeStudent;
            this.semesterStudent = semesterStudent;
            this.meetType = meetType;
            this.dataMeet = dataMeet;
            this.timeMeet = timeMeet;
            this.reason = reason;
            this.indexNumber = indexNumber;
        }
    }
}