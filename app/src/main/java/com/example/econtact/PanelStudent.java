package com.example.econtact;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PanelStudent extends AppCompatActivity {

    TextView welcomeText, verifyMessage;
    Button addNewTicketButton, confirmTicketsButton,
            logoutButton, pendingTicketsButton, rejectTicketsButton, resendCodeButton, ongoingTickets, settings;
    String nameStudent, surnameStudent, facultyStudent,
            fieldStudent, degreeStudent, semesterStudent, indexNumberStudent, emailStudent;
    int i=0;
    int j=0;

    ImageView imageViewStudent;

    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_student);
        addNewTicketButton = findViewById(R.id.addNewTicket_panelStudent);
        confirmTicketsButton = findViewById(R.id.confirmTicket_panelStudent);
        logoutButton = findViewById(R.id.logoutButton_panelStudent);
        resendCodeButton = findViewById(R.id.verifyButton_panelStudent);
        welcomeText = findViewById(R.id.textview_panelStudent);
        verifyMessage = findViewById(R.id.verifyText_panelStudent);
        pendingTicketsButton = findViewById(R.id.pendingTickets_panelStudent);
        rejectTicketsButton = findViewById(R.id.rejectTickets_panelStudent);
        ongoingTickets = findViewById(R.id.ongoingTicket_panelStudent);
        settings = findViewById(R.id.settings_panelStudent);

        imageViewStudent = findViewById(R.id.imageView_PanelStudent);

        emailStudent = getIntent().getStringExtra("Email");

        firebaseFirestore = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference().child(emailStudent + ".jpg");

        try {
            final File localFile = File.createTempFile(emailStudent, "jpg");
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


        //Searching data in collection "User Accounts" in document name UID student user
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
                indexNumberStudent = documentSnapshot.getString("IndexNumber");
                welcomeText.setText("Hello " + nameStudent + " " + surnameStudent);
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("Accepted Applications");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                        String nameVal = document.getString("nameStudent");
                        String surnameVal = document.getString("surnameStudent");
                        String facultyVal = document.getString("facultyStudent");
                        String fieldVal = document.getString("fieldStudent");

                        assert nameVal != null;
                        assert surnameVal != null;
                        assert facultyVal != null;
                        assert fieldVal != null;

                        if (nameVal.equals(nameStudent) && surnameVal.equals(surnameStudent)
                                && facultyVal.equals(facultyStudent) && fieldVal.equals(fieldStudent)) {
                            i++;
                        }
                    }
                }

                if(i>0){
                    NotificationChannel channel = new NotificationChannel("channel01", "name",
                            NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                    channel.setDescription("description");

                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);

                    Notification notification = new NotificationCompat.Builder(PanelStudent.this, "channel01")
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentTitle("You have new accepted "+ "(" + i + ")" +"teachers' tickets!")
                            .setContentText("Check the tickets in the accepted or ongoing tab")
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                            .build();
                    notificationManager.notify(0, notification);
                }
            }
        });

        CollectionReference collectionReference1 = firebaseFirestore.collection("Canceled Applications");
        collectionReference1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                        String nameVal = document.getString("nameStudent");
                        String surnameVal = document.getString("surnameStudent");
                        String facultyVal = document.getString("facultyStudent");
                        String fieldVal = document.getString("fieldStudent");

                        assert nameVal != null;
                        assert surnameVal != null;
                        assert facultyVal != null;
                        assert fieldVal != null;

                        if (nameVal.equals(nameStudent) && surnameVal.equals(surnameStudent)
                                && facultyVal.equals(facultyStudent) && fieldVal.equals(fieldStudent)) {
                            j++;
                        }
                    }
                }
                if(j>0){
                    NotificationChannel channel = new NotificationChannel("channel01", "name",
                            NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                    channel.setDescription("description");

                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);

                    Notification notification = new NotificationCompat.Builder(PanelStudent.this, "channel01")
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentTitle("You have new canceled "+ "(" + j + ")" +"teachers' tickets!")
                            .setContentText("Check the tickets in the canceled tab")
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                            .build();
                    notificationManager.notify(0, notification);
                }
            }
        });

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;

        if (!user.isEmailVerified()) {
            resendCodeButton.setVisibility(View.VISIBLE);
            verifyMessage.setVisibility(View.VISIBLE);
            resendCodeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onSuccess(Void aVoid) {
                            NotificationChannel channel = new NotificationChannel("channel01", "name",
                                    NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                            channel.setDescription("description");

                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                            notificationManager.createNotificationChannel(channel);

                            Notification notification = new NotificationCompat.Builder(PanelStudent.this, "channel01")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("eContact")
                                    .setContentText("A new verification e-mail has been sent")
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

                            Notification notification = new NotificationCompat.Builder(PanelStudent.this, "channel01")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("Error: ")
                                    .setContentText(e.getMessage())
                                    .setDefaults(Notification.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                    .build();
                            notificationManager.notify(0, notification);
                        }
                    });
                }
            });
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationChannel channel = new NotificationChannel("channel01", "name",
                        NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                channel.setDescription("description");

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);

                Notification notification = new NotificationCompat.Builder(PanelStudent.this, "channel01")
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("eContact")
                        .setContentText("You logged out!")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                        .build();
                notificationManager.notify(0, notification);
                startActivity(new Intent(PanelStudent.this, MainActivity.class));
            }
        });

        addNewTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNewTicketStepOne.class);
                intent.putExtra("nameStudent", nameStudent);
                intent.putExtra("surnameStudent", surnameStudent);
                intent.putExtra("facultyStudent", facultyStudent);
                intent.putExtra("fieldStudent", fieldStudent);
                intent.putExtra("degreeStudent", degreeStudent);
                intent.putExtra("semesterStudent", semesterStudent);
                intent.putExtra("indexNumberStudent", indexNumberStudent);
                intent.putExtra("Email", emailStudent);
                startActivity(intent);
            }
        });

        confirmTicketsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PanelStudent.this, ConfirmTickets.class);
                intent.putExtra("nameStudent", nameStudent);
                intent.putExtra("surnameStudent", surnameStudent);
                intent.putExtra("facultyStudent", facultyStudent);
                intent.putExtra("fieldStudent", fieldStudent);
                intent.putExtra("Email", emailStudent);
                startActivity(intent);
            }
        });

        pendingTicketsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PanelStudent.this, PendingTickets.class);
                intent.putExtra("nameStudent", nameStudent);
                intent.putExtra("surnameStudent", surnameStudent);
                intent.putExtra("facultyStudent", facultyStudent);
                intent.putExtra("fieldStudent", fieldStudent);
                intent.putExtra("Email", emailStudent);
                startActivity(intent);
            }
        });

        rejectTicketsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PanelStudent.this, RejectTickets.class);
                intent.putExtra("nameStudent", nameStudent);
                intent.putExtra("surnameStudent", surnameStudent);
                intent.putExtra("facultyStudent", facultyStudent);
                intent.putExtra("fieldStudent", fieldStudent);
                intent.putExtra("Email", emailStudent);
                startActivity(intent);
            }
        });

        ongoingTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PanelStudent.this, OngoingTicketStudent.class);
                intent.putExtra("nameStudent", nameStudent);
                intent.putExtra("surnameStudent", surnameStudent);
                intent.putExtra("facultyStudent", facultyStudent);
                intent.putExtra("fieldStudent", fieldStudent);
                intent.putExtra("Email", emailStudent);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PanelStudent.this, SettingsPanelStudent.class);
                intent.putExtra("Email", emailStudent);
                startActivity(intent);
            }
        });




    }
}