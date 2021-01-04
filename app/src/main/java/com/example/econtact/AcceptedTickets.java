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

import static android.view.View.VISIBLE;

public class AcceptedTickets extends AppCompatActivity {
    Button previousButton, nextButton;
    TextView nameStudent, surnameStudent, facultyStudent, fieldStudent, degreeStudent, semesterStudent, typeMeet, dataMeet, timeMeet, reasonMeet, indexNumberStudent;
    String nameTeacher, surnameTeacher, facultyTeacher, fieldTeacher;
    FirebaseFirestore firebaseFirestore;
    String day, month, year, minute, hour;
    int indexTicket = 0;

    ImageView imageViewStudent;

    StorageReference storageReference;

    String nameVal, surnameVal, facultyVal, fieldVal, nameVal2, surnameVal2, facultyVal2, fieldVal2;

    List<CloudFireAcceptedTicket> objectArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_tickets);

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

        imageViewStudent = findViewById(R.id.imageView_AcceptedTickets);

        nameTeacher = getIntent().getStringExtra("nameTeacher");
        surnameTeacher = getIntent().getStringExtra("surnameTeacher");
        facultyTeacher = getIntent().getStringExtra("facultyTeacher");
        fieldTeacher = getIntent().getStringExtra("fieldTeacher");

        firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("Accepted Applications");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                        nameVal = document.getString("nameTeacher");
                        surnameVal = document.getString("surnameTeacher");
                        facultyVal = document.getString("facultyTeacher");
                        fieldVal = document.getString("fieldTeacher");

                        nameVal2 = document.getString("nameTeacher2");
                        surnameVal2 = document.getString("surnameTeacher2");
                        facultyVal2 = document.getString("facultyTeacher2");
                        fieldVal2 = document.getString("fieldTeacher2");


                        if (nameVal.equals(nameTeacher) && surnameVal.equals(surnameTeacher) &&
                                facultyVal.equals(facultyTeacher) && fieldVal.equals(fieldTeacher)) {

                            day = document.getString("dayTicket");
                            month = document.getString("monthTicket");
                            year = document.getString("yearTicket");
                            minute = document.getString("minuteTicket");
                            hour = document.getString("hourTicket");
                            CloudFireAcceptedTicket cloudFireAcceptedTicket = new CloudFireAcceptedTicket(document.getString("nameStudent"), document.getString("surnameStudent"), document.getString("facultyStudent"),
                                    document.getString("fieldStudent"), document.getString("degreeStudent"), document.getString("semesterStudent"),document.getString("emailStudent"),  document.getString("typeMeet"), day + "." + month + "." + year,
                                    hour + ":" + minute, document.getString("reasonType"), document.getString("indexNumberStudent"));
                            objectArrayList.add(cloudFireAcceptedTicket);
                        }

                        if (nameVal2.equals(nameTeacher) && surnameVal2.equals(surnameTeacher) &&
                                facultyVal2.equals(facultyTeacher) && fieldVal2.equals(fieldTeacher)) {
                            day = document.getString("dayTicket");
                            month = document.getString("monthTicket");
                            year = document.getString("yearTicket");
                            minute = document.getString("minuteTicket");
                            hour = document.getString("hourTicket");
                            CloudFireAcceptedTicket cloudFireAcceptedTicket = new CloudFireAcceptedTicket(document.getString("nameStudent"), document.getString("surnameStudent"), document.getString("facultyStudent"),
                                    document.getString("fieldStudent"), document.getString("degreeStudent"), document.getString("semesterStudent"), document.getString("emailStudent"),document.getString("typeMeet"), day + "." + month + "." + year,
                                    hour + ":" + minute, document.getString("reasonType"), document.getString("indexNumberStudent"));
                            objectArrayList.add(cloudFireAcceptedTicket);
                        }


                    }

                    if (objectArrayList.size() == 0) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AcceptedTickets.this);
                        dialogBuilder.setTitle("Accepted applications");
                        dialogBuilder.setMessage("No tickets to display!");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setNeutralButton("Back to Panel", new Dialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(AcceptedTickets.this, PanelTeacher.class);
                                intent.putExtra("Email", getIntent().getStringExtra("Email"));
                                startActivity(intent);
                            }
                        });
                        dialogBuilder.create();
                        dialogBuilder.show();
                    }
                    if (objectArrayList.size() == 1) {
                        CloudFireAcceptedTicket cloudFireAcceptedTicket = objectArrayList.get(0);
                        if (nameVal.equals(nameTeacher) && surnameVal.equals(surnameTeacher) &&
                                facultyVal.equals(facultyTeacher) && fieldVal.equals(fieldTeacher)) {

                            storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireAcceptedTicket.emailStudent+ ".jpg");
                            try {
                                final File localFile = File.createTempFile(cloudFireAcceptedTicket.emailStudent, "jpg");
                                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        imageViewStudent.setImageBitmap(bitmap);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


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
                        if (nameVal2.equals(nameTeacher) && surnameVal2.equals(surnameTeacher) &&
                                facultyVal2.equals(facultyTeacher) && fieldVal2.equals(fieldTeacher)) {

                            storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireAcceptedTicket.emailStudent+ ".jpg");
                            try {
                                final File localFile = File.createTempFile(cloudFireAcceptedTicket.emailStudent, "jpg");
                                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        imageViewStudent.setImageBitmap(bitmap);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

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

                    if (objectArrayList.size() > 1) {
                        nextButton.setVisibility(VISIBLE);
                        CloudFireAcceptedTicket cloudFireAcceptedTicket = objectArrayList.get(0);

                        if (nameVal.equals(nameTeacher) && surnameVal.equals(surnameTeacher) &&
                                facultyVal.equals(facultyTeacher) && fieldVal.equals(fieldTeacher)) {

                            storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireAcceptedTicket.emailStudent+ ".jpg");
                            try {
                                final File localFile = File.createTempFile(cloudFireAcceptedTicket.emailStudent, "jpg");
                                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        imageViewStudent.setImageBitmap(bitmap);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

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
                        if (nameVal2.equals(nameTeacher) && surnameVal2.equals(surnameTeacher) &&
                                facultyVal2.equals(facultyTeacher) && fieldVal2.equals(fieldTeacher)) {

                            storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireAcceptedTicket.emailStudent+ ".jpg");
                            try {
                                final File localFile = File.createTempFile(cloudFireAcceptedTicket.emailStudent, "jpg");
                                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        imageViewStudent.setImageBitmap(bitmap);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

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
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousButton.setVisibility(View.VISIBLE);
                int sizeList = objectArrayList.size();
                indexTicket++;

                if (indexTicket == (sizeList - 1)) {
                    nextButton.setVisibility(View.GONE);
                    CloudFireAcceptedTicket cloudFireAcceptedTicket = objectArrayList.get(indexTicket);

                    if (nameVal.equals(nameTeacher) && surnameVal.equals(surnameTeacher) &&
                            facultyVal.equals(facultyTeacher) && fieldVal.equals(fieldTeacher)) {
                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireAcceptedTicket.emailStudent+ ".jpg");
                        try {
                            final File localFile = File.createTempFile(cloudFireAcceptedTicket.emailStudent, "jpg");
                            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    imageViewStudent.setImageBitmap(bitmap);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                    if (nameVal2.equals(nameTeacher) && surnameVal2.equals(surnameTeacher) &&
                            facultyVal2.equals(facultyTeacher) && fieldVal2.equals(fieldTeacher)) {
                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireAcceptedTicket.emailStudent+ ".jpg");
                        try {
                            final File localFile = File.createTempFile(cloudFireAcceptedTicket.emailStudent, "jpg");
                            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    imageViewStudent.setImageBitmap(bitmap);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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


                if (indexTicket < sizeList) {
                    CloudFireAcceptedTicket cloudFireAcceptedTicket = objectArrayList.get(indexTicket);

                    if (nameVal.equals(nameTeacher) && surnameVal.equals(surnameTeacher) &&
                            facultyVal.equals(facultyTeacher) && fieldVal.equals(fieldTeacher)) {

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireAcceptedTicket.emailStudent+ ".jpg");
                        try {
                            final File localFile = File.createTempFile(cloudFireAcceptedTicket.emailStudent, "jpg");
                            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    imageViewStudent.setImageBitmap(bitmap);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                    if (nameVal2.equals(nameTeacher) && surnameVal2.equals(surnameTeacher) &&
                            facultyVal2.equals(facultyTeacher) && fieldVal2.equals(fieldTeacher)) {
                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireAcceptedTicket.emailStudent+ ".jpg");
                        try {
                            final File localFile = File.createTempFile(cloudFireAcceptedTicket.emailStudent, "jpg");
                            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    imageViewStudent.setImageBitmap(bitmap);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                } else {
                    nextButton.setVisibility(View.GONE);
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexTicket--;

                if (indexTicket == 0) {
                    previousButton.setVisibility(View.GONE);
                    CloudFireAcceptedTicket cloudFireAcceptedTicket = objectArrayList.get(indexTicket);

                    if (nameVal.equals(nameTeacher) && surnameVal.equals(surnameTeacher) &&
                            facultyVal.equals(facultyTeacher) && fieldVal.equals(fieldTeacher)) {

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireAcceptedTicket.emailStudent+ ".jpg");
                        try {
                            final File localFile = File.createTempFile(cloudFireAcceptedTicket.emailStudent, "jpg");
                            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    imageViewStudent.setImageBitmap(bitmap);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

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
                    if (nameVal2.equals(nameTeacher) && surnameVal2.equals(surnameTeacher) &&
                            facultyVal2.equals(facultyTeacher) && fieldVal2.equals(fieldTeacher)) {

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireAcceptedTicket.emailStudent+ ".jpg");
                        try {
                            final File localFile = File.createTempFile(cloudFireAcceptedTicket.emailStudent, "jpg");
                            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    imageViewStudent.setImageBitmap(bitmap);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                    nextButton.setVisibility(VISIBLE);
                }

                if (indexTicket > 0) {
                    CloudFireAcceptedTicket cloudFireAcceptedTicket = objectArrayList.get(indexTicket);
                    if (nameVal.equals(nameTeacher) && surnameVal.equals(surnameTeacher) &&
                            facultyVal.equals(facultyTeacher) && fieldVal.equals(fieldTeacher)) {

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireAcceptedTicket.emailStudent+ ".jpg");
                        try {
                            final File localFile = File.createTempFile(cloudFireAcceptedTicket.emailStudent, "jpg");
                            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    imageViewStudent.setImageBitmap(bitmap);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

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
                    if (nameVal2.equals(nameTeacher) && surnameVal2.equals(surnameTeacher) &&
                            facultyVal2.equals(facultyTeacher) && fieldVal2.equals(fieldTeacher)) {

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireAcceptedTicket.emailStudent+ ".jpg");
                        try {
                            final File localFile = File.createTempFile(cloudFireAcceptedTicket.emailStudent, "jpg");
                            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    imageViewStudent.setImageBitmap(bitmap);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                    nextButton.setVisibility(VISIBLE);
                } else {
                    previousButton.setVisibility(View.GONE);
                }
            }
        });


    }

    public static class CloudFireAcceptedTicket {
        String nameStudent = " ";
        String surnameStudent = " ";

        String facultyStudent = " ";
        String fieldStudent = " ";

        String degreeStudent = " ";
        String semesterStudent = " ";

        String emailStudent = " ";

        String meetType = " ";
        String dataMeet = " ";

        String timeMeet = " ";

        String reason = " ";
        String indexNumber = " ";

        public CloudFireAcceptedTicket(String nameStudent, String surnameStudent, String facultyStudent,
                                       String fieldStudent, String degreeStudent, String semesterStudent,
                                       String emailStudent,
                                       String meetType, String dataMeet, String timeMeet, String reason, String indexNumber) {
            this.nameStudent = nameStudent;
            this.surnameStudent = surnameStudent;
            this.facultyStudent = facultyStudent;
            this.fieldStudent = fieldStudent;
            this.degreeStudent = degreeStudent;
            this.semesterStudent = semesterStudent;
            this.emailStudent = emailStudent;
            this.meetType = meetType;
            this.dataMeet = dataMeet;
            this.timeMeet = timeMeet;
            this.reason = reason;
            this.indexNumber = indexNumber;
        }
    }
}