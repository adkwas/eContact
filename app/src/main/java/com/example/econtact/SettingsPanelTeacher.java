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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsPanelTeacher extends AppCompatActivity {

    Button resetPassword, resetFacultyAndField, back;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_panel_teacher);

        resetPassword = findViewById(R.id.resetPassword_settingPanelTeacher);
        resetFacultyAndField = findViewById(R.id.resetFacultyAndField_settingsPanelTeacher);
        back = findViewById(R.id.back_settingsPanelTeacher);

        firebaseAuth = FirebaseAuth.getInstance();

        email = getIntent().getStringExtra("Email");

        user = firebaseAuth.getCurrentUser();


        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetPassword = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Settings Panel");
                passwordResetDialog.setMessage("Enter new password: (6 character long)");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Send new password", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newPassword = resetPassword.getText().toString();
                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onSuccess(Void aVoid) {
                                NotificationChannel channel = new NotificationChannel("channel01", "name",
                                        NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                                channel.setDescription("description");

                                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                notificationManager.createNotificationChannel(channel);

                                Notification notification = new NotificationCompat.Builder(SettingsPanelTeacher.this, "channel01")
                                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                                        .setContentTitle("Change password correct! ")
                                        .setContentText("Please log in again")
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                        .build();
                                notificationManager.notify(0, notification);
                                startActivity(new Intent(SettingsPanelTeacher.this, MainActivity.class));
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

                                Notification notification = new NotificationCompat.Builder(SettingsPanelTeacher.this, "channel01")
                                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                                        .setContentTitle("Settings")
                                        .setContentText("Change password failure!")
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                        .build();
                                notificationManager.notify(0, notification);
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("Back to ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                passwordResetDialog.create().show();
            }
        });


        resetFacultyAndField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsPanelTeacher.this, ResetFacultyAndFieldTeacher.class);
                intent.putExtra("Email", email);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsPanelTeacher.this, PanelTeacher.class);
                intent.putExtra("Email", email);
                startActivity(intent);
            }
        });
    }
}