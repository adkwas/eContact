package com.example.econtact;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class Login extends AppCompatActivity {
    EditText loginUser, passwordUser;
    TextView forgotTextLink;
    Button loginButton;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    String userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginUser = findViewById(R.id.email_Login);
        passwordUser = findViewById(R.id.password_Login);
        forgotTextLink = findViewById(R.id.forgotPassword_Login);
        loginButton = findViewById(R.id.loginButton_Login);
        progressBar = findViewById(R.id.bar_Login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save email and password from user
                final String emailUserString = loginUser.getText().toString();
                String passwordUserString = passwordUser.getText().toString();

                if (emailUserString.isEmpty()) {
                    loginUser.setError("Email is empty!");
                    return;
                }

                if (passwordUserString.isEmpty()) {
                    passwordUser.setError("Password is empty!");
                    return;
                }

                //Check, which user want to login to app
                DocumentReference documentStudent = FirebaseFirestore.getInstance()
                        .collection("Users Accounts").document(emailUserString);
                documentStudent.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        assert documentSnapshot != null;
                        userStatus = documentSnapshot.getString("User");
                    }
                });

                //Sign user by means of Firebase Auth
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailUserString, passwordUserString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String studentVal = "Student";
                            final String teacherVal = "Teacher";
                            //progressBar.setVisibility(View.INVISIBLE);

                            String val = String.valueOf(userStatus);
                            if (val.equals(studentVal)) {
                                Intent intent = new Intent(Login.this, PanelStudent.class);
                                intent.putExtra("Email", emailUserString);
                                startActivity(intent);
                            }
                            if (val.equals(teacherVal)) {
                                Intent intent = new Intent(Login.this, PanelTeacher.class);
                                intent.putExtra("Email", emailUserString);
                                startActivity(intent);
                            }
                        } else {
                            NotificationChannel channel = new NotificationChannel("channel01", "name",
                                    NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                            channel.setDescription("description");

                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                            notificationManager.createNotificationChannel(channel);

                            Notification notification = new NotificationCompat.Builder(Login.this, "channel01")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("eContact")
                                    .setContentText(Objects.requireNonNull(task.getException()).getMessage())
                                    .setDefaults(Notification.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                    .build();
                            notificationManager.notify(0, notification);
                        }
                    }
                });
            }
        });

        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetMail = new EditText(view.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Log in Account");
                passwordResetDialog.setMessage("Enter your mail to received reset link: ");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail = resetMail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onSuccess(Void aVoid) {
                                NotificationChannel channel = new NotificationChannel("channel01", "name",
                                        NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                                channel.setDescription("description");

                                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                notificationManager.createNotificationChannel(channel);

                                Notification notification = new NotificationCompat.Builder(Login.this, "channel01")
                                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                                        .setContentTitle("eContact")
                                        .setContentText("Reset link send to your email!")
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

                                Notification notification = new NotificationCompat.Builder(Login.this, "channel01")
                                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                                        .setContentTitle("eContact")
                                        .setContentText("Error: " + e.getMessage())
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                        .build();
                                notificationManager.notify(0, notification);
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                passwordResetDialog.create().show();
            }
        });
    }
}