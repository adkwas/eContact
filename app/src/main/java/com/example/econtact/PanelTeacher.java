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

public class PanelTeacher extends AppCompatActivity {
    TextView welcomeText, verifyMessageText;
    Button checkNewTicketButton, logoutButton, acceptedTicketsButton, resendCodeButton, ongoingButton, settings;
    String nameTeacher, surnameTeacher, facultyTeacher, fieldTeacher, emailTeacher;
    FirebaseFirestore firebaseFirestore;

    ImageView imageViewTeacher;

    StorageReference storageReference;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_teacher);
        checkNewTicketButton = findViewById(R.id.checkNewTicket_panelTeacher);
        welcomeText = findViewById(R.id.textview_panelTeacher);
        logoutButton = findViewById(R.id.logoutButton_panelTeacher);
        resendCodeButton = findViewById(R.id.verifyButton_panelTeacher);
        verifyMessageText = findViewById(R.id.verifyText_panelTeacher);
        ongoingButton = findViewById(R.id.ongoingTickets_panelTeacher2);
        acceptedTicketsButton = findViewById(R.id.acceptedTickets_panelTeacher);
        settings = findViewById(R.id.settingsPanel_panelTeacher);

        imageViewTeacher = findViewById(R.id.imageView_PanelTeacher);

        emailTeacher = getIntent().getStringExtra("Email");

        storageReference = FirebaseStorage.getInstance().getReference().child("adam.jpg");
        try {
            final File localFile = File.createTempFile("adam", "jpg");
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


        final DocumentReference documentTeacher = FirebaseFirestore.getInstance().collection("Users Accounts").document(emailTeacher);
        documentTeacher.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshot != null;
                nameTeacher = documentSnapshot.getString("Name");
                surnameTeacher = documentSnapshot.getString("Surname");
                facultyTeacher = documentSnapshot.getString("Faculty");
                fieldTeacher = documentSnapshot.getString("Field");
                welcomeText.setText("Hello" + " " + nameTeacher + " " + surnameTeacher);
            }
        });


        firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("Pending applications");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
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
                            i++;
                        }

                        if(nameVal2.equals(nameTeacher) && surnameVal2.equals(surnameTeacher)
                                && facultyVal2.equals(facultyTeacher) && fieldVal2.equals(fieldTeacher)){
                            i++;
                        }

                    }

                    if(i>0){
                        NotificationChannel channel = new NotificationChannel("channel01", "name",
                                NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                        channel.setDescription("description");

                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);

                        Notification notification = new NotificationCompat.Builder(PanelTeacher.this, "channel01")
                                .setSmallIcon(android.R.drawable.ic_dialog_info)
                                .setContentTitle("You have a new ticket " + "(" + i + ")" + "from students!")
                                .setContentText("Check the news in the \"Check New Tickets\" tab")
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                .build();
                        notificationManager.notify(0, notification);
                    }
                }
            }
        });




        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        if (!user.isEmailVerified()) {
            resendCodeButton.setVisibility(View.VISIBLE);
            verifyMessageText.setVisibility(View.VISIBLE);

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

                            Notification notification = new NotificationCompat.Builder(PanelTeacher.this, "channel01")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("Success!")
                                    .setContentText("Verification Email has been sent")
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

                            Notification notification = new NotificationCompat.Builder(PanelTeacher.this, "channel01")
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                NotificationChannel channel = new NotificationChannel("channel01", "name",
                        NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                channel.setDescription("description");

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);

                Notification notification = new NotificationCompat.Builder(PanelTeacher.this, "channel01")
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("eContact")
                        .setContentText("You logged out!")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                        .build();
                notificationManager.notify(0, notification);
                startActivity(new Intent(PanelTeacher.this, MainActivity.class));
            }
        });

        acceptedTicketsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PanelTeacher.this, AcceptedTickets.class);
                intent.putExtra("nameTeacher", nameTeacher);
                intent.putExtra("surnameTeacher", surnameTeacher);
                intent.putExtra("facultyTeacher", facultyTeacher);
                intent.putExtra("fieldTeacher", fieldTeacher);
                intent.putExtra("Email", emailTeacher);
                startActivity(intent);
            }
        });

        checkNewTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PanelTeacher.this, CheckNewTicket.class);
                intent.putExtra("nameTeacher", nameTeacher);
                intent.putExtra("surnameTeacher", surnameTeacher);
                intent.putExtra("facultyTeacher", facultyTeacher);
                intent.putExtra("fieldTeacher", fieldTeacher);
                intent.putExtra("Email", emailTeacher);
                startActivity(intent);
            }
        });

        ongoingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PanelTeacher.this, OngoingTicketTeacher.class);
                intent.putExtra("nameTeacher", nameTeacher);
                intent.putExtra("surnameTeacher", surnameTeacher);
                intent.putExtra("facultyTeacher", facultyTeacher);
                intent.putExtra("fieldTeacher", fieldTeacher);
                intent.putExtra("Email", emailTeacher);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PanelTeacher.this, SettingsPanelTeacher.class);
                intent.putExtra("Email", getIntent().getStringExtra("Email"));
                startActivity(intent);
            }
        });
    }
}
