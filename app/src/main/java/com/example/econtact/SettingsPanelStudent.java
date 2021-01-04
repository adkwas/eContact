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

public class SettingsPanelStudent extends AppCompatActivity {

    Button resetDataStudent, resetPassword, back, resetPicture;
    String email;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_panel_student);

        resetDataStudent = findViewById(R.id.resetProfilePicture_SettingPanelStudent);
        resetPassword = findViewById(R.id.resetPassword_settingPanel);
        resetPicture = findViewById(R.id.resetPicture_SettingsPanelStudent);
        back = findViewById(R.id.back_settingsPanelStudent);

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

                                Notification notification = new NotificationCompat.Builder(SettingsPanelStudent.this, "channel01")
                                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                                        .setContentTitle("Settings")
                                        .setContentText("Change password correct!")
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                        .build();
                                notificationManager.notify(0, notification);


                                startActivity(new Intent(SettingsPanelStudent.this, MainActivity.class));
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

                                Notification notification = new NotificationCompat.Builder(SettingsPanelStudent.this, "channel01")
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

        resetDataStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsPanelStudent.this, ResetDataStudent.class);
                intent.putExtra("Email", email);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsPanelStudent.this, PanelStudent.class);
                intent.putExtra("Email", email);
                startActivity(intent);
            }
        });

        resetPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsPanelStudent.this, ResetPictureStudent.class);
                intent.putExtra("Email", email);
                startActivity(intent);
            }
        });
    }
}