package com.example.econtact;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class PendingTickets extends AppCompatActivity {

    Button previousButton, nextButton;
    TextView nameTeacher, surnameTeacher, facultyTeacher, fieldTeacher, typeMeet, dataMeet, timeMeet, reasonMeet;
    String nameStudent, surnameStudent, facultyStudent, fieldStudent, day, month, year, minute, hour;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    ImageView imageViewTeacher;
    int indexTicket = 0;
    List<CloudFirePendingTickets> objectArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_tickets);

        //Get ID for all Textviews and Buttons
        previousButton = findViewById(R.id.previous_pendingTickets);
        nextButton = findViewById(R.id.next_pendingTickets);
        nameTeacher = findViewById(R.id.nameLecturer_pendingTickets);
        surnameTeacher = findViewById(R.id.surnameLecturer_pendingTickets);
        facultyTeacher = findViewById(R.id.facultyLecturer_pendingTickets);
        fieldTeacher = findViewById(R.id.fieldLecturer_pendingTickets);
        typeMeet = findViewById(R.id.typeMeet_pendingTickets);
        dataMeet = findViewById(R.id.dataMeet_pendingTickets);
        timeMeet = findViewById(R.id.timeMeet_pendingTickets);
        reasonMeet = findViewById(R.id.reasonMeet_pendingTickets);

        imageViewTeacher = findViewById(R.id.imageView_PendingTickets);

        //Get user data with Panel Student
        nameStudent = getIntent().getStringExtra("nameStudent");
        surnameStudent = getIntent().getStringExtra("surnameStudent");
        facultyStudent = getIntent().getStringExtra("facultyStudent");
        fieldStudent = getIntent().getStringExtra("fieldStudent");

        //Get data from the Pending Application collection.
        //Data from a given document - tickets are saved in the cloudFirePendingTickets object
        //Saved in the objectArrayList
        firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("Pending applications");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
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
                                        CloudFirePendingTickets cloudFirePendingTickets = new CloudFirePendingTickets(document.getString("nameTeacher"), document.getString("surnameTeacher"), document.getString("facultyTeacher"),
                                                document.getString("fieldTeacher"), document.getString("emailTeacher1"), document.getString("typeMeet"), day + "." + month + "." + year,
                                                hour + ":" + minute, document.getString("reasonType"));
                                        objectArrayList.add(cloudFirePendingTickets);
                                    }
                                }
                            }
                        }
                    }

                    //If the list of applications is late -
                    //display a banner about no applications with a button to return to Panel Student
                    if (objectArrayList.size() == 0) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PendingTickets.this);
                        dialogBuilder.setTitle("Pending Tickets");
                        dialogBuilder.setMessage("No tickets to display!");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setNeutralButton("Back to Panel", new Dialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(PendingTickets.this, PanelStudent.class);
                                intent.putExtra("Email", getIntent().getStringExtra("Email"));
                                startActivity(intent);
                            }
                        });
                        dialogBuilder.create();
                        dialogBuilder.show();
                    } else {
                        CloudFirePendingTickets cloudFirePendingTickets = objectArrayList.get(0);
                        //If the list of tickets has one ticket - display it on the application's desktop
                        if (objectArrayList.size() == 1) {
                            nextButton.setVisibility(GONE);
                            previousButton.setVisibility(GONE);

                            storageReference = FirebaseStorage.getInstance().getReference().child(cloudFirePendingTickets.emailTeacher + ".jpg");

                            try {
                                final File localFile = File.createTempFile(cloudFirePendingTickets.emailTeacher, "jpg");
                                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        imageViewTeacher.setImageBitmap(bitmap);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            nameTeacher.setText("Name: " + cloudFirePendingTickets.nameTeacher);
                            surnameTeacher.setText("Surname: " + cloudFirePendingTickets.surnameTeacher);
                            facultyTeacher.setText(cloudFirePendingTickets.facultyTeacher);
                            fieldTeacher.setText("Field: " + cloudFirePendingTickets.fieldTeacher);
                            typeMeet.setText("Type Meet: " + cloudFirePendingTickets.meetType);
                            dataMeet.setText("Date: " + cloudFirePendingTickets.dataMeet);
                            timeMeet.setText("Time: " + cloudFirePendingTickets.timeMeet);
                            reasonMeet.setText("Reason: " + cloudFirePendingTickets.reason);
                        }

                        //If the ticket list has several tickets - display them on the application's desktop
                        //Activate NextButton
                        if (objectArrayList.size() > 1) {
                            nextButton.setVisibility(VISIBLE);

                            storageReference = FirebaseStorage.getInstance().getReference().child(cloudFirePendingTickets.emailTeacher + ".jpg");

                            try {
                                final File localFile = File.createTempFile(cloudFirePendingTickets.emailTeacher, "jpg");
                                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        imageViewTeacher.setImageBitmap(bitmap);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            nameTeacher.setText("Name: " + cloudFirePendingTickets.nameTeacher);
                            surnameTeacher.setText("Surname: " + cloudFirePendingTickets.surnameTeacher);
                            facultyTeacher.setText(cloudFirePendingTickets.facultyTeacher);
                            fieldTeacher.setText("Field: " + cloudFirePendingTickets.fieldTeacher);
                            typeMeet.setText("Type Meet: " + cloudFirePendingTickets.meetType);
                            dataMeet.setText("Date: " + cloudFirePendingTickets.dataMeet);
                            timeMeet.setText("Time: " + cloudFirePendingTickets.timeMeet);
                            reasonMeet.setText("Reason: " + cloudFirePendingTickets.reason);
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
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                previousButton.setVisibility(View.VISIBLE);
                int sizeList = objectArrayList.size();
                indexTicket++;

                if (indexTicket == (sizeList - 1)) {
                    nextButton.setVisibility(GONE);
                    CloudFirePendingTickets cloudFirePendingTickets = objectArrayList.get(indexTicket);
                    storageReference = FirebaseStorage.getInstance().getReference().child(cloudFirePendingTickets.emailTeacher + ".jpg");

                    try {
                        final File localFile = File.createTempFile(cloudFirePendingTickets.emailTeacher, "jpg");
                        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                imageViewTeacher.setImageBitmap(bitmap);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    nameTeacher.setText("Name: " + cloudFirePendingTickets.nameTeacher);
                    surnameTeacher.setText("Surname: " + cloudFirePendingTickets.surnameTeacher);
                    facultyTeacher.setText(cloudFirePendingTickets.facultyTeacher);
                    fieldTeacher.setText("Field: " + cloudFirePendingTickets.fieldTeacher);
                    typeMeet.setText("Type Meet: " + cloudFirePendingTickets.meetType);
                    dataMeet.setText("Date: " + cloudFirePendingTickets.dataMeet);
                    timeMeet.setText("Time: " + cloudFirePendingTickets.timeMeet);
                    reasonMeet.setText("Reason: " + cloudFirePendingTickets.reason);
                }

                if (indexTicket < sizeList) {
                    CloudFirePendingTickets cloudFirePendingTickets = objectArrayList.get(indexTicket);

                    storageReference = FirebaseStorage.getInstance().getReference().child(cloudFirePendingTickets.emailTeacher + ".jpg");

                    try {
                        final File localFile = File.createTempFile(cloudFirePendingTickets.emailTeacher, "jpg");
                        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                imageViewTeacher.setImageBitmap(bitmap);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    nameTeacher.setText("Name: " + cloudFirePendingTickets.nameTeacher);
                    surnameTeacher.setText("Surname: " + cloudFirePendingTickets.surnameTeacher);
                    facultyTeacher.setText(cloudFirePendingTickets.facultyTeacher);
                    fieldTeacher.setText("Field: " + cloudFirePendingTickets.fieldTeacher);
                    typeMeet.setText("Type Meet: " + cloudFirePendingTickets.meetType);
                    dataMeet.setText("Date: " + cloudFirePendingTickets.dataMeet);
                    timeMeet.setText("Time: " + cloudFirePendingTickets.timeMeet);
                    reasonMeet.setText("Reason: " + cloudFirePendingTickets.reason);
                } else {
                    nextButton.setVisibility(GONE);
                }

            }
        });


        //If click a previous button
        //Go to the previous request and set the parameters on the display.
        //If we get to the first notification - turn off the previous button
        //Activate Next Button
        previousButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                indexTicket--;
                if (indexTicket == 0) {
                    previousButton.setVisibility(GONE);
                    CloudFirePendingTickets cloudFirePendingTickets = objectArrayList.get(indexTicket);

                    storageReference = FirebaseStorage.getInstance().getReference().child(cloudFirePendingTickets.emailTeacher + ".jpg");

                    try {
                        final File localFile = File.createTempFile(cloudFirePendingTickets.emailTeacher, "jpg");
                        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                imageViewTeacher.setImageBitmap(bitmap);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    nameTeacher.setText("Name: " + cloudFirePendingTickets.nameTeacher);
                    surnameTeacher.setText("Surname: " + cloudFirePendingTickets.surnameTeacher);
                    facultyTeacher.setText(cloudFirePendingTickets.facultyTeacher);
                    fieldTeacher.setText("Field: " + cloudFirePendingTickets.fieldTeacher);
                    typeMeet.setText("Type Meet: " + cloudFirePendingTickets.meetType);
                    dataMeet.setText("Date: " + cloudFirePendingTickets.dataMeet);
                    timeMeet.setText("Time: " + cloudFirePendingTickets.timeMeet);
                    reasonMeet.setText("Reason: " + cloudFirePendingTickets.reason);
                    nextButton.setVisibility(VISIBLE);
                }

                if (indexTicket > 0) {
                    CloudFirePendingTickets cloudFirePendingTickets = objectArrayList.get(indexTicket);

                    storageReference = FirebaseStorage.getInstance().getReference().child(cloudFirePendingTickets.emailTeacher + ".jpg");

                    try {
                        final File localFile = File.createTempFile(cloudFirePendingTickets.emailTeacher, "jpg");
                        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                imageViewTeacher.setImageBitmap(bitmap);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    nameTeacher.setText("Name: " + cloudFirePendingTickets.nameTeacher);
                    surnameTeacher.setText("Surname: " + cloudFirePendingTickets.surnameTeacher);
                    facultyTeacher.setText(cloudFirePendingTickets.facultyTeacher);
                    fieldTeacher.setText("Field: " + cloudFirePendingTickets.fieldTeacher);
                    typeMeet.setText("Type Meet: " + cloudFirePendingTickets.meetType);
                    dataMeet.setText("Date: " + cloudFirePendingTickets.dataMeet);
                    timeMeet.setText("Time: " + cloudFirePendingTickets.timeMeet);
                    reasonMeet.setText("Reason: " + cloudFirePendingTickets.reason);
                    nextButton.setVisibility(VISIBLE);
                } else {
                    previousButton.setVisibility(GONE);
                }
            }
        });
    }

    //Class CloudFirePendingTickets
    //The class is used to save read student data from Cloud Fire.
    //Class objects are single entries to the same teacher.
    //The class contains all the data concerning the application and the data of a single student.
    //The class objects are then listed in the list.
    public static class CloudFirePendingTickets {
        String nameTeacher;
        String surnameTeacher;
        String facultyTeacher;
        String fieldTeacher;
        String emailTeacher;
        String meetType;
        String dataMeet;
        String timeMeet;
        String reason;

        public CloudFirePendingTickets(String nameTeacher, String surnameTeacher, String facultyTeacher,
                                       String fieldTeacher, String emailTeacher, String meetType, String dataMeet, String timeMeet, String reason) {
            this.nameTeacher = nameTeacher;
            this.surnameTeacher = surnameTeacher;
            this.facultyTeacher = facultyTeacher;
            this.fieldTeacher = fieldTeacher;
            this.emailTeacher = emailTeacher;
            this.meetType = meetType;
            this.dataMeet = dataMeet;
            this.timeMeet = timeMeet;
            this.reason = reason;
        }
    }
}