package com.example.econtact;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class ResetPictureTeacher extends AppCompatActivity {

    Button setPicture, back;
    ImageView imageViewTeacher;
    StorageReference storageReference;
    String emailTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_picture_teacher);

        setPicture = findViewById(R.id.setPicture_ResetPictureTeacher);
        back = findViewById(R.id.back_ResetPictureTeacher);
        imageViewTeacher = findViewById(R.id.imageView_ResetPictureTeacher);

        emailTeacher = getIntent().getStringExtra("Email");


        storageReference = FirebaseStorage.getInstance().getReference().child(emailTeacher + ".jpg");

        try {
            final File localFile = File.createTempFile(emailTeacher, "jpg");
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

        setPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPictureTeacher.this, SettingsPanelTeacher.class);
                intent.putExtra("Email", emailTeacher);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri imageUri = data.getData();
                imageViewTeacher.setImageURI(imageUri);
                uploadImageToFirebase(imageUri, emailTeacher);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri, String emailString){
        //StorageReference fileRef = storageReference.child(emailString + ".jpg");
        StorageReference fileRef = storageReference;
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                NotificationChannel channel = new NotificationChannel("channel01", "name",
                        NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                channel.setDescription("description");

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);

                Notification notification = new NotificationCompat.Builder(ResetPictureTeacher.this, "channel01")
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("eContact")
                        .setContentText("Profile picture has been changed!")
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

                Notification notification = new NotificationCompat.Builder(ResetPictureTeacher.this, "channel01")
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("eContact")
                        .setContentText("Error: " + e.toString())
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                        .build();
                notificationManager.notify(0, notification);

            }
        });
    }
}