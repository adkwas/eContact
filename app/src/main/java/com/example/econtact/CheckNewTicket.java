package com.example.econtact;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    //A list of objects that are single applications to the teacher.
    List<CloudFireCheckNewTicket> objectList = new ArrayList<>();
    //List containing ID of tickets.
    //This is needed to delete requests from Cloud Fire
    List<String> IDArrayList = new ArrayList<>();
    int indexTicket = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_new_ticket);

        //Set ID for Buttons and TextViews
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

        //Get data teacher with Panel Teacher
        nameTeacher = getIntent().getStringExtra("nameTeacher");
        surnameTeacher = getIntent().getStringExtra("surnameTeacher");
        facultyTeacher = getIntent().getStringExtra("facultyTeacher");
        fieldTeacher = getIntent().getStringExtra("fieldTeacher");

        //Search for tickets that are specific to a given teacher.
        firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("Pending applications");
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

                        //Protection
                        assert nameVal != null;
                        assert surnameVal != null;
                        assert facultyVal != null;
                        assert fieldVal != null;

                        //If the data retrieved from a previous activity (containing teacher data) and data from Cloud Fire
                        //Save the data to the CloudFireCheckNewTicket object and
                        //Enter it into the list of ticket objects.
                        if (nameVal.equals(nameTeacher) && surnameVal.equals(surnameTeacher)
                                && facultyVal.equals(facultyTeacher) && fieldVal.equals(fieldTeacher)) {
                            dayTicket = document.getString("dayTicket");
                            monthTicket = document.getString("monthTicket");
                            yearTicket = document.getString("yearTicket");
                            minuteTicket = document.getString("minuteTicket");
                            hourTicket = document.getString("hourTicket");
                            CloudFireCheckNewTicket cloudFireCheckNewTicket = new CloudFireCheckNewTicket(document.getString("nameStudent"), document.getString("surnameStudent"),
                                    document.getString("facultyStudent"),
                                    document.getString("fieldStudent"), document.getString("degreeStudent"), document.getString("semesterStudent"), document.getString("indexNumberStudent"), document.getString("typeMeet"), dayTicket + "." + monthTicket + "." + yearTicket,
                                    hourTicket + ":" + minuteTicket, document.getString("reasonType"));
                            objectList.add(cloudFireCheckNewTicket);
                            //The ticket IDs are also saved to the second list in order to later
                            //verify which tickets are to be deleted
                            IDArrayList.add(document.getId());
                        }
                    }

                    //If the list of applications is late -
                    //display a banner about no applications with a button to return to Panel Teacher
                    if (objectList.isEmpty()) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CheckNewTicket.this);
                        dialogBuilder.setTitle("Check New Tickets");
                        dialogBuilder.setMessage("No tickets to display!");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setNeutralButton("Back to Panel", new Dialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                startActivity(new Intent(CheckNewTicket.this, PanelTeacher.class));
                            }
                        });
                        dialogBuilder.create();
                        dialogBuilder.show();
                    }

                    //If the list of tickets has one ticket - display it on the application's desktop
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

                    //If the ticket list has several tickets - display them on the application's desktop
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

        //If user accept ticket
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                firebaseFirestore = FirebaseFirestore.getInstance();
                //Get a specific object from the list of objects.
                //Place all object data in the "Accepted applications" collection
                CloudFireCheckNewTicket cloudFireCheckNewTicket = objectList.get(indexTicket);
                final DocumentReference documentReference = firebaseFirestore.collection("Accepted Applications")
                        .document(cloudFireCheckNewTicket.nameStudent + " " + cloudFireCheckNewTicket.surnameStudent + " to " +
                                nameTeacher + " " + surnameTeacher + " date: " + cloudFireCheckNewTicket.dataMeet + " time: " + cloudFireCheckNewTicket.timeMeet);
                Map<String, Object> user = new HashMap<>();
                //Data Teacher
                user.put("nameTeacher", nameTeacher);
                user.put("surnameTeacher", surnameTeacher);
                user.put("facultyTeacher", facultyTeacher);
                user.put("fieldTeacher", fieldTeacher);

                //Data student
                user.put("nameStudent", cloudFireCheckNewTicket.nameStudent);
                user.put("surnameStudent", cloudFireCheckNewTicket.surnameStudent);
                user.put("facultyStudent", cloudFireCheckNewTicket.facultyStudent);
                user.put("fieldStudent", cloudFireCheckNewTicket.fieldStudent);
                user.put("degreeStudent", cloudFireCheckNewTicket.degreeStudent);
                user.put("semesterStudent", cloudFireCheckNewTicket.semesterStudent);
                user.put("indexNumberStudent", cloudFireCheckNewTicket.indexNumberStudent);

                //Data ticket
                //Time meet
                String minuteString = String.valueOf(minuteTicket);
                String hourString = String.valueOf(hourTicket);
                user.put("minuteTicket", minuteString);
                user.put("hourTicket", hourString);

                //Date meet
                String dayString = String.valueOf(dayTicket);
                String monthString = String.valueOf(monthTicket);
                String yearString = String.valueOf(yearTicket);
                user.put("dayTicket", dayString);
                user.put("monthTicket", monthString);
                user.put("yearTicket", yearString);

                //
                user.put("typeMeet", cloudFireCheckNewTicket.typeMeet);
                user.put("reasonType", cloudFireCheckNewTicket.reason);

                //Check if ticket is saved in Accepted Applications collection
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CheckNewTicket.this, "The application has been accepted", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Error!: " + e.toString());
                    }
                });

                //Remove object applications of list objects
                objectList.remove(indexTicket);
                int sizeList = objectList.size();
                //Removal of a specific request from the waiting list
                final CollectionReference collectionReference = firebaseFirestore.collection("Pending Applications");
                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                if (document.getId().equals(IDArrayList.get(indexTicket))) {
                                    collectionReference.document(document.getId()).delete();
                                    IDArrayList.remove(indexTicket);
                                }
                            }
                        }
                    }
                });

                //If there are new tickets on the list, display the next ones.
                // If there are no tickets already, display a banner that says there are no tickets
                //with a button to return to the Teacher Panel
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
                            startActivity(intent);
                        }
                    });
                    dialogBuilder.create();
                    dialogBuilder.show();
                }
            }
        });

        //If user canceled ticket
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                firebaseFirestore = FirebaseFirestore.getInstance();
                //Get a specific object from the list of objects.
                //Place all object data in the "Canceled applications" collection
                CloudFireCheckNewTicket cloudFireCheckNewTicket = objectList.get(indexTicket);
                DocumentReference documentReference = firebaseFirestore.collection("Canceled Applications")
                        .document(cloudFireCheckNewTicket.nameStudent + " " + cloudFireCheckNewTicket.surnameStudent + " to " +
                                nameTeacher + " " + surnameTeacher + " date: " + cloudFireCheckNewTicket.dataMeet + " time: " + cloudFireCheckNewTicket.timeMeet);
                Map<String, Object> user = new HashMap<>();
                //Data Teacher
                user.put("nameTeacher", nameTeacher);
                user.put("surnameTeacher", surnameTeacher);
                user.put("facultyTeacher", facultyTeacher);
                user.put("fieldTeacher", fieldTeacher);

                //Data student
                user.put("nameStudent", cloudFireCheckNewTicket.nameStudent);
                user.put("surnameStudent", cloudFireCheckNewTicket.surnameStudent);
                user.put("facultyStudent", cloudFireCheckNewTicket.facultyStudent);
                user.put("fieldStudent", cloudFireCheckNewTicket.fieldStudent);
                user.put("degreeStudent", cloudFireCheckNewTicket.degreeStudent);
                user.put("yearStudent", cloudFireCheckNewTicket.semesterStudent);
                user.put("indexNumberStudent", cloudFireCheckNewTicket.indexNumberStudent);

                //Data ticket
                //Time meet
                String minuteString = String.valueOf(minuteTicket);
                String hourString = String.valueOf(hourTicket);
                user.put("minuteTicket", minuteString);
                user.put("hourTicket", hourString);

                //Date meet
                String dayString = String.valueOf(dayTicket);
                String monthString = String.valueOf(monthTicket);
                String yearString = String.valueOf(yearTicket);
                user.put("dayTicket", dayString);
                user.put("monthTicket", monthString);
                user.put("yearTicket", yearString);

                //
                user.put("typeMeet", cloudFireCheckNewTicket.typeMeet);
                user.put("reasonType", cloudFireCheckNewTicket.reason);

                //Check if ticket is saved in Canceled Applications collection
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CheckNewTicket.this, "The application has been canceled", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Error: " + e.toString());
                    }
                });

                //Remove object applications of list objects
                objectList.remove(indexTicket);
                int sizeList = objectList.size();
                //Removal of a specific request from the waiting list
                final CollectionReference collectionReference = firebaseFirestore.collection("Pending Applications");
                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                if (document.getId().equals(IDArrayList.get(indexTicket))) {
                                    collectionReference.document(document.getId()).delete();
                                    IDArrayList.remove(indexTicket);
                                }
                            }
                        }
                    }
                });

                //If there are new tickets on the list, display the next ones.
                // If there are no tickets already, display a banner that says there are no tickets
                //with a button to return to the Teacher Panel
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
                            startActivity(intent);
                        }
                    });
                    dialogBuilder.create();
                    dialogBuilder.show();
                }
            }
        });
    }

    //Class CloudFireCheckNewTicket
    //The class is used to save read student data from Cloud Fire.
    //Class objects are single entries to the same teacher.
    //The class contains all the data concerning the application and the data of a single student.
    //The class objects are then listed in the list.
    public static class CloudFireCheckNewTicket {
        String nameStudent;
        String surnameStudent;
        String facultyStudent;
        String fieldStudent;
        String degreeStudent;
        String semesterStudent;
        String indexNumberStudent;
        String typeMeet;
        String dataMeet;
        String timeMeet;
        String reason;

        public CloudFireCheckNewTicket(String nameStudent, String surnameStudent, String facultyStudent,
                                       String fieldStudent, String degreeStudent, String semesterStudent, String indexNumberStudent, String typeMeet, String dataMeet, String timeMeet, String reason) {
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