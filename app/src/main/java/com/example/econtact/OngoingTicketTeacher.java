package com.example.econtact;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class OngoingTicketTeacher extends AppCompatActivity {

    Button previousButton, nextButton, save1, save2, deleteTicket, uploadFile;
    EditText informationTeacherOne, informationTeacherTwo, selectFile;


    TextView nameStudent, surnameStudent, nameTeacher2, surnameTeacher2,
            informationTeacher1Text, informationTeacher2Text, textView;

    String nameTeacher2Canceled = " ", surnameTeacher2Canceled = " ", facultyTeacher2Canceled = " ",
            fieldTeacher2Canceled = " ";

    String databaseReferenceNamePath;
    putPDF uploadedPDF;
    String nameTeacherLogin, surnameTeacherLogin, facultyTeacherLogin, fieldTeacherLogin, emailTeacherLogin;
    FirebaseFirestore firebaseFirestore;
    int indexTicket = 0;
    int valueType = 0;

    String nameFile, valueFile, namePathFileView;
    List<CloudFireOngoingTicket> objectArrayList = new ArrayList<>();

    ImageView imageViewStudent, imageViewTeacher2;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_ticket_teacher);

        previousButton = findViewById(R.id.previous_ongoingTicket);
        nextButton = findViewById(R.id.next_ongoingTicket);
        save1 = findViewById(R.id.save1_ongoingTicketTeacher);
        save2 = findViewById(R.id.save2_ongoingTicketTeacher);
        deleteTicket = findViewById(R.id.delete_ongoingTicketTeacher);
        uploadFile = findViewById(R.id.uploadFile_ongoingTicketTeacher);

        nameStudent = findViewById(R.id.nameStudent_ongoingTicketTeacher);
        surnameStudent = findViewById(R.id.surnameStudent_ongoingTicketTeacher);

        nameTeacher2 = findViewById(R.id.nameTeacher2_ongoingTicketTeacher);
        surnameTeacher2 = findViewById(R.id.surnameTeacher2_ongoingTicketTeacher);

        informationTeacher1Text = findViewById(R.id.informationTeacher1Text_ongoingTicketTeacher2);
        informationTeacher2Text = findViewById(R.id.informationTeacher2Text_ongoingTicketTeacher);
        informationTeacherOne = findViewById(R.id.informationTeacher1_ongoingTicketTeacher);
        informationTeacherTwo = findViewById(R.id.informationTeacher2_ongoingTicketTeacher);
        textView = findViewById(R.id.secondTeacherView_ongoingTicketTeacher);

        imageViewStudent = findViewById(R.id.imageViewStudent_OngoingTicketTeacher);
        imageViewTeacher2 = findViewById(R.id.imageViewTeacher2_OngoingTicketTeacher);

        selectFile = findViewById(R.id.selectFile_ongoingTicketTeacher);

        nameTeacherLogin = getIntent().getStringExtra("nameTeacher");
        surnameTeacherLogin = getIntent().getStringExtra("surnameTeacher");
        facultyTeacherLogin = getIntent().getStringExtra("facultyTeacher");
        fieldTeacherLogin = getIntent().getStringExtra("fieldTeacher");
        emailTeacherLogin = getIntent().getStringExtra("Email");

        storageReference = FirebaseStorage.getInstance().getReference();

        uploadFile.setEnabled(false);


        firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("Accepted Applications");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                        String nameTeacher = document.getString("nameTeacher");
                        String surnameTeacher = document.getString("surnameTeacher");
                        String facultyTeacher = document.getString("facultyTeacher");
                        String fieldTeacher = document.getString("fieldTeacher");

                        assert nameTeacher != null;
                        assert surnameTeacher != null;
                        assert facultyTeacher != null;
                        assert fieldTeacher != null;

                        if (nameTeacher.equals(nameTeacherLogin) && surnameTeacher.equals(surnameTeacherLogin)
                                && facultyTeacher.equals(facultyTeacherLogin) && fieldTeacher.equals(fieldTeacherLogin)) {
                            CloudFireOngoingTicket cloudFireOngoingTicket = new CloudFireOngoingTicket(
                                    document.getId(),

                                    document.getString("nameTeacher"),
                                    document.getString("surnameTeacher"),
                                    document.getString("facultyTeacher"),
                                    document.getString("fieldTeacher"),
                                    document.getString("emailTeacher1"),

                                    document.getString("nameTeacher2"),
                                    document.getString("surnameTeacher2"),
                                    document.getString("facultyTeacher2"),
                                    document.getString("fieldTeacher2"),
                                    document.getString("emailTeacher2"),

                                    document.getString("nameStudent"),
                                    document.getString("surnameStudent"),
                                    document.getString("facultyStudent"),
                                    document.getString("fieldStudent"),
                                    document.getString("semesterStudent"),
                                    document.getString("degreeStudent"),
                                    document.getString("indexNumberStudent"),
                                    document.getString("emailStudent"),

                                    document.getString("minuteTicket"),
                                    document.getString("hourTicket"),

                                    document.getString("dayTicket"),
                                    document.getString("monthTicket"),
                                    document.getString("yearTicket"),

                                    document.getString("reasonType"),
                                    document.getString("typeMeet"),

                                    document.getString("informationTeacher1"),
                                    document.getString("informationTeacher2"));
                            objectArrayList.add(cloudFireOngoingTicket);
                        }

                        String nameTeacher2 = document.getString("nameTeacher2");
                        String surnameTeacher2 = document.getString("surnameTeacher2");
                        String facultyTeacher2 = document.getString("facultyTeacher2");
                        String fieldTeacher2 = document.getString("fieldTeacher2");

                        if (nameTeacher2.equals(nameTeacherLogin) && surnameTeacher2.equals(surnameTeacherLogin)
                                && facultyTeacher2.equals(facultyTeacherLogin) && fieldTeacher2.equals(fieldTeacherLogin)) {
                            CloudFireOngoingTicket cloudFireOngoingTicket = new CloudFireOngoingTicket(
                                    document.getId(),

                                    document.getString("nameTeacher"),
                                    document.getString("surnameTeacher"),
                                    document.getString("facultyTeacher"),
                                    document.getString("fieldTeacher"),
                                    document.getString("emailTeacher1"),

                                    document.getString("nameTeacher2"),
                                    document.getString("surnameTeacher2"),
                                    document.getString("facultyTeacher2"),
                                    document.getString("fieldTeacher2"),
                                    document.getString("emailTeacher2"),

                                    document.getString("nameStudent"),
                                    document.getString("surnameStudent"),
                                    document.getString("facultyStudent"),
                                    document.getString("fieldStudent"),
                                    document.getString("semesterStudent"),
                                    document.getString("degreeStudent"),
                                    document.getString("indexNumberStudent"),
                                    document.getString("emailStudent"),

                                    document.getString("minuteTicket"),
                                    document.getString("hourTicket"),

                                    document.getString("dayTicket"),
                                    document.getString("monthTicket"),
                                    document.getString("yearTicket"),

                                    document.getString("reasonType"),
                                    document.getString("typeMeet"),

                                    document.getString("informationTeacher1"),
                                    document.getString("informationTeacher2"));
                            objectArrayList.add(cloudFireOngoingTicket);
                        }
                    }
                }

                if (objectArrayList.size() == 0) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OngoingTicketTeacher.this);
                    dialogBuilder.setTitle("Ongoing applications");
                    dialogBuilder.setMessage("No tickets to display!");
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setNeutralButton("Back to Panel", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(OngoingTicketTeacher.this, PanelTeacher.class);
                            intent.putExtra("Email", getIntent().getStringExtra("Email"));
                            startActivity(intent);
                        }
                    });
                    dialogBuilder.create();
                    dialogBuilder.show();
                }


                if (objectArrayList.size() == 1) {
                    //////
                    nextButton.setVisibility(GONE);
                    previousButton.setVisibility(GONE);
                    final CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(0);
                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {

                        textView.setText("Second Teacher:");
                        textView.setVisibility(VISIBLE);

                        informationTeacherOne.setVisibility(VISIBLE);
                        save1.setVisibility(VISIBLE);
                        informationTeacher1Text.setVisibility(GONE);

                        informationTeacher2Text.setVisibility(VISIBLE);


                        if (cloudFireOngoingTicket.nameTeacher2.equals(" ") && cloudFireOngoingTicket.surnameTeacher2.equals(" ")) {
                            OngoingTicketTeacher.this.nameTeacher2.setText("No data!");
                            OngoingTicketTeacher.this.surnameTeacher2.setText("No data!");
                            informationTeacher2Text.setText("No data!");
                        } else {
                            OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                            OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);
                            informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);

                            storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher2 + ".jpg");

                            try {
                                final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher2, "jpg");
                                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        imageViewTeacher2.setImageBitmap(bitmap);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);
                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                        CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        if (document.getId().equals(cloudFireOngoingTicket.id)) {
                                            nameTeacher2Canceled = document.getString("nameTeacher2");
                                            surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                            facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                            fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                            if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                    && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                                if (document.getString("secondTeacher").equals("no")) {
                                                    save2.setVisibility(GONE);
                                                    informationTeacherTwo.setVisibility(GONE);
                                                    informationTeacher2Text.setVisibility(VISIBLE);
                                                    informationTeacher2Text.setText("No data!");
                                                    nameTeacher2.setText("No data!");
                                                    surnameTeacher2.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(GONE);
                                                informationTeacher2Text.setVisibility(VISIBLE);
                                                informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }


                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher2) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher2) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher2) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher2)) {

                        textView.setText("First Teacher:");
                        textView.setVisibility(VISIBLE);

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher, "jpg");
                            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    imageViewTeacher2.setImageBitmap(bitmap);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                        informationTeacherOne.setVisibility(GONE);
                        save1.setVisibility(GONE);
                        informationTeacher1Text.setVisibility(VISIBLE);
                        informationTeacher1Text.setText(cloudFireOngoingTicket.informationTeacher1);

                        informationTeacherTwo.setVisibility(VISIBLE);
                        save2.setVisibility(VISIBLE);
                        informationTeacher2Text.setVisibility(GONE);
                    }
                    //////
                }

                if (objectArrayList.size() > 1) {
                    nextButton.setVisibility(VISIBLE);
                    final CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(0);
//////
                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {

                        textView.setText("Second Teacher:");
                        textView.setVisibility(VISIBLE);

                        informationTeacherOne.setVisibility(VISIBLE);
                        save1.setVisibility(VISIBLE);
                        informationTeacher1Text.setVisibility(GONE);

                        informationTeacher2Text.setVisibility(VISIBLE);
                        if (cloudFireOngoingTicket.nameTeacher2.equals(" ") && cloudFireOngoingTicket.surnameTeacher2.equals(" ")) {
                            OngoingTicketTeacher.this.nameTeacher2.setText("No data!");
                            OngoingTicketTeacher.this.surnameTeacher2.setText("No data!");
                            informationTeacher2Text.setText("No data!");
                        } else {
                            OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                            OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);
                            informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);

                            storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher2 + ".jpg");

                            try {
                                final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher2, "jpg");
                                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        imageViewTeacher2.setImageBitmap(bitmap);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                        CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        if (document.getId().equals(cloudFireOngoingTicket.id)) {
                                            nameTeacher2Canceled = document.getString("nameTeacher2");
                                            surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                            facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                            fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                            if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                    && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                                if (document.getString("secondTeacher").equals("no")) {
                                                    save2.setVisibility(GONE);
                                                    informationTeacherTwo.setVisibility(GONE);
                                                    informationTeacher2Text.setVisibility(VISIBLE);
                                                    informationTeacher2Text.setText("No data!");
                                                    nameTeacher2.setText("No data!");
                                                    surnameTeacher2.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(GONE);
                                                informationTeacher2Text.setVisibility(VISIBLE);
                                                informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }


                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher2) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher2) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher2) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher2)) {

                        textView.setText("First Teacher:");
                        textView.setVisibility(VISIBLE);

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher, "jpg");
                            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    imageViewTeacher2.setImageBitmap(bitmap);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                        informationTeacherOne.setVisibility(GONE);
                        save1.setVisibility(GONE);
                        informationTeacher1Text.setVisibility(VISIBLE);
                        informationTeacher1Text.setText(cloudFireOngoingTicket.informationTeacher1);

                        informationTeacherTwo.setVisibility(VISIBLE);
                        save2.setVisibility(VISIBLE);
                        informationTeacher2Text.setVisibility(GONE);
                    }
                    //////

                }
            }
        });


        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OngoingTicketTeacher.this);
                dialogBuilder.setTitle("Ongoing Tickets ");
                dialogBuilder.setMessage("Select the type of file to send:");
                dialogBuilder.setCancelable(false);
                dialogBuilder.setNeutralButton("PDF", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        selectPDF();
                    }
                });

                dialogBuilder.setNegativeButton("Image", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        selectImage();
                    }
                });

                dialogBuilder.setPositiveButton("DOC", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        selectDOC();
                    }
                });

                dialogBuilder.create();
                dialogBuilder.show();
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousButton.setVisibility(View.VISIBLE);
                int sizeList = objectArrayList.size();
                indexTicket++;

                if (indexTicket == (sizeList - 1)) {
                    nextButton.setVisibility(GONE);
                    final CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(indexTicket);

                    //////
                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {

                        textView.setText("Second Teacher:");
                        textView.setVisibility(VISIBLE);


                        informationTeacherOne.setVisibility(VISIBLE);
                        save1.setVisibility(VISIBLE);
                        informationTeacher1Text.setVisibility(GONE);

                        informationTeacher2Text.setVisibility(VISIBLE);
                        if (cloudFireOngoingTicket.nameTeacher2.equals(" ") && cloudFireOngoingTicket.surnameTeacher2.equals(" ")) {
                            OngoingTicketTeacher.this.nameTeacher2.setText("No data!");
                            OngoingTicketTeacher.this.surnameTeacher2.setText("No data!");
                            informationTeacher2Text.setText("No data!");
                        } else {
                            OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                            OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);
                            informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);

                            storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher2 + ".jpg");

                            try {
                                final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher2, "jpg");
                                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        imageViewTeacher2.setImageBitmap(bitmap);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                        CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        if (document.getId().equals(cloudFireOngoingTicket.id)) {
                                            nameTeacher2Canceled = document.getString("nameTeacher2");
                                            surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                            facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                            fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                            if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                    && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                                if (document.getString("secondTeacher").equals("no")) {
                                                    save2.setVisibility(GONE);
                                                    informationTeacherTwo.setVisibility(GONE);
                                                    informationTeacher2Text.setVisibility(VISIBLE);
                                                    informationTeacher2Text.setText("No data!");
                                                    nameTeacher2.setText("No data!");
                                                    surnameTeacher2.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(GONE);
                                                informationTeacher2Text.setVisibility(VISIBLE);
                                                informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }


                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher2) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher2) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher2) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher2)) {

                        textView.setText("First Teacher:");
                        textView.setVisibility(VISIBLE);

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher, "jpg");
                            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    imageViewTeacher2.setImageBitmap(bitmap);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                        informationTeacherOne.setVisibility(GONE);
                        save1.setVisibility(GONE);
                        informationTeacher1Text.setVisibility(VISIBLE);
                        informationTeacher1Text.setText(cloudFireOngoingTicket.informationTeacher1);

                        informationTeacherTwo.setVisibility(VISIBLE);
                        save2.setVisibility(VISIBLE);
                        informationTeacher2Text.setVisibility(GONE);
                    }
                    //////

                }


                if (indexTicket < sizeList) {
                    final CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(indexTicket);


                    //////
                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {

                        textView.setText("Second Teacher:");
                        textView.setVisibility(VISIBLE);

                        informationTeacherOne.setVisibility(VISIBLE);
                        save1.setVisibility(VISIBLE);
                        informationTeacher1Text.setVisibility(GONE);

                        informationTeacher2Text.setVisibility(VISIBLE);
                        if (cloudFireOngoingTicket.nameTeacher2.equals(" ") && cloudFireOngoingTicket.surnameTeacher2.equals(" ")) {
                            OngoingTicketTeacher.this.nameTeacher2.setText("No data!");
                            OngoingTicketTeacher.this.surnameTeacher2.setText("No data!");
                            informationTeacher2Text.setText("No data!");
                        } else {
                            OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                            OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);
                            informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);
                            storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher2 + ".jpg");

                            try {
                                final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher2, "jpg");
                                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        imageViewTeacher2.setImageBitmap(bitmap);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                        CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        if (document.getId().equals(cloudFireOngoingTicket.id)) {
                                            nameTeacher2Canceled = document.getString("nameTeacher2");
                                            surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                            facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                            fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                            if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                    && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                                if (document.getString("secondTeacher").equals("no")) {
                                                    save2.setVisibility(GONE);
                                                    informationTeacherTwo.setVisibility(GONE);
                                                    informationTeacher2Text.setVisibility(VISIBLE);
                                                    informationTeacher2Text.setText("No data!");
                                                    nameTeacher2.setText("No data!");
                                                    surnameTeacher2.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(GONE);
                                                informationTeacher2Text.setVisibility(VISIBLE);
                                                informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }


                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher2) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher2) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher2) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher2)) {

                        textView.setText("First Teacher:");
                        textView.setVisibility(VISIBLE);

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher, "jpg");
                            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    imageViewTeacher2.setImageBitmap(bitmap);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                        informationTeacherOne.setVisibility(GONE);
                        save1.setVisibility(GONE);
                        informationTeacher1Text.setVisibility(VISIBLE);
                        informationTeacher1Text.setText(cloudFireOngoingTicket.informationTeacher1);

                        informationTeacherTwo.setVisibility(VISIBLE);
                        save2.setVisibility(VISIBLE);
                        informationTeacher2Text.setVisibility(GONE);
                    }
                    //////

                } else {
                    nextButton.setVisibility(GONE);
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexTicket--;

                if (indexTicket == 0) {
                    previousButton.setVisibility(GONE);
                    final CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(indexTicket);

//////
                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {

                        textView.setText("Second Teacher:");
                        textView.setVisibility(VISIBLE);

                        informationTeacherOne.setVisibility(VISIBLE);
                        save1.setVisibility(VISIBLE);
                        informationTeacher1Text.setVisibility(GONE);
                        informationTeacher2Text.setVisibility(VISIBLE);
                        if (cloudFireOngoingTicket.nameTeacher2.equals(" ") && cloudFireOngoingTicket.surnameTeacher2.equals(" ")) {
                            OngoingTicketTeacher.this.nameTeacher2.setText("No data!");
                            OngoingTicketTeacher.this.surnameTeacher2.setText("No data!");
                            informationTeacher2Text.setText("No data!");
                        } else {
                            OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                            OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);
                            informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);

                            storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher2 + ".jpg");

                            try {
                                final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher2, "jpg");
                                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        imageViewTeacher2.setImageBitmap(bitmap);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                        CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        if (document.getId().equals(cloudFireOngoingTicket.id)) {
                                            nameTeacher2Canceled = document.getString("nameTeacher2");
                                            surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                            facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                            fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                            if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                    && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                                if (document.getString("secondTeacher").equals("no")) {
                                                    save2.setVisibility(GONE);
                                                    informationTeacherTwo.setVisibility(GONE);
                                                    informationTeacher2Text.setVisibility(VISIBLE);
                                                    informationTeacher2Text.setText("No data!");
                                                    nameTeacher2.setText("No data!");
                                                    surnameTeacher2.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(GONE);
                                                informationTeacher2Text.setVisibility(VISIBLE);
                                                informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }


                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher2) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher2) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher2) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher2)) {

                        textView.setText("First Teacher:");
                        textView.setVisibility(VISIBLE);

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher, "jpg");
                            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    imageViewTeacher2.setImageBitmap(bitmap);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                        informationTeacherOne.setVisibility(GONE);
                        save1.setVisibility(GONE);
                        informationTeacher1Text.setVisibility(VISIBLE);
                        informationTeacher1Text.setText(cloudFireOngoingTicket.informationTeacher1);

                        informationTeacherTwo.setVisibility(VISIBLE);
                        save2.setVisibility(VISIBLE);
                        informationTeacher2Text.setVisibility(GONE);
                    }
                    //////

                    nextButton.setVisibility(VISIBLE);
                }

                if (indexTicket > 0) {
                    final CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(indexTicket);
                    //////
                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {

                        textView.setText("Second Teacher:");
                        textView.setVisibility(VISIBLE);

                        informationTeacherOne.setVisibility(VISIBLE);
                        save1.setVisibility(VISIBLE);
                        informationTeacher1Text.setVisibility(GONE);

                        informationTeacher2Text.setVisibility(VISIBLE);
                        if (cloudFireOngoingTicket.nameTeacher2.equals(" ") && cloudFireOngoingTicket.surnameTeacher2.equals(" ")) {
                            OngoingTicketTeacher.this.nameTeacher2.setText("No data!");
                            OngoingTicketTeacher.this.surnameTeacher2.setText("No data!");
                            informationTeacher2Text.setText("No data!");
                        } else {
                            OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                            OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);
                            informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);

                            storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher2 + ".jpg");

                            try {
                                final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher2, "jpg");
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
                        }

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                        CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        if (document.getId().equals(cloudFireOngoingTicket.id)) {
                                            nameTeacher2Canceled = document.getString("nameTeacher2");
                                            surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                            facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                            fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                            if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                    && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                                if (document.getString("secondTeacher").equals("no")) {
                                                    save2.setVisibility(GONE);
                                                    informationTeacherTwo.setVisibility(GONE);
                                                    informationTeacher2Text.setVisibility(VISIBLE);
                                                    informationTeacher2Text.setText("No data!");
                                                    nameTeacher2.setText("No data!");
                                                    surnameTeacher2.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(GONE);
                                                informationTeacher2Text.setVisibility(VISIBLE);
                                                informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }


                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher2) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher2) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher2) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher2)) {

                        textView.setText("First Teacher:");
                        textView.setVisibility(VISIBLE);

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher, "jpg");
                            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    imageViewTeacher2.setImageBitmap(bitmap);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                        storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                        try {
                            final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                        informationTeacherOne.setVisibility(GONE);
                        save1.setVisibility(GONE);
                        informationTeacher1Text.setVisibility(VISIBLE);
                        informationTeacher1Text.setText(cloudFireOngoingTicket.informationTeacher1);

                        informationTeacherTwo.setVisibility(VISIBLE);
                        save2.setVisibility(VISIBLE);
                        informationTeacher2Text.setVisibility(GONE);
                    }
                    //////

                    nextButton.setVisibility(VISIBLE);
                } else {
                    previousButton.setVisibility(GONE);
                }
            }
        });


        save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String informationTeacher1 = informationTeacherOne.getText().toString();

                if (informationTeacher1.isEmpty()) {
                    informationTeacherOne.setError("Complete the data for the teacher before saving!");
                } else {
                    final CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(indexTicket);

                    final DocumentReference documentReference = firebaseFirestore.collection("Accepted Applications")
                            .document(cloudFireOngoingTicket.id);
                    Map<String, Object> user = new HashMap<>();

                    user.put("nameTeacher", cloudFireOngoingTicket.nameTeacher);
                    user.put("surnameTeacher", cloudFireOngoingTicket.surnameTeacher);
                    user.put("facultyTeacher", cloudFireOngoingTicket.facultyTeacher);
                    user.put("fieldTeacher", cloudFireOngoingTicket.fieldTeacher);
                    user.put("emailTeacher1", cloudFireOngoingTicket.emailTeacher);

                    user.put("nameTeacher2", cloudFireOngoingTicket.nameTeacher2);
                    user.put("surnameTeacher2", cloudFireOngoingTicket.surnameTeacher2);
                    user.put("facultyTeacher2", cloudFireOngoingTicket.facultyTeacher2);
                    user.put("fieldTeacher2", cloudFireOngoingTicket.fieldTeacher2);
                    user.put("emailTeacher2", cloudFireOngoingTicket.emailTeacher2);

                    user.put("nameStudent", cloudFireOngoingTicket.nameStudent);
                    user.put("surnameStudent", cloudFireOngoingTicket.surnameStudent);
                    user.put("facultyStudent", cloudFireOngoingTicket.facultyStudent);
                    user.put("fieldStudent", cloudFireOngoingTicket.fieldStudent);
                    user.put("degreeStudent", cloudFireOngoingTicket.degreeStudent);
                    user.put("semesterStudent", cloudFireOngoingTicket.semesterStudent);
                    user.put("indexNumberStudent", cloudFireOngoingTicket.indexNumberStudent);
                    user.put("emailStudent", cloudFireOngoingTicket.emailStudent);

                    user.put("minuteTicket", cloudFireOngoingTicket.minuteTicket);
                    user.put("hourTicket", cloudFireOngoingTicket.hourTicket);

                    user.put("dayTicket", cloudFireOngoingTicket.dayTicket);
                    user.put("monthTicket", cloudFireOngoingTicket.monthTicket);
                    user.put("yearTicket", cloudFireOngoingTicket.yearTicket);

                    user.put("reasonType", cloudFireOngoingTicket.reasonType);
                    user.put("typeMeet", cloudFireOngoingTicket.typeMeet);

                    user.put("informationTeacher1", informationTeacher1);
                    user.put("informationTeacher2", cloudFireOngoingTicket.informationTeacher2);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onSuccess(Void aVoid) {

                            NotificationChannel channel = new NotificationChannel("channel01", "name",
                                    NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                            channel.setDescription("description");

                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                            notificationManager.createNotificationChannel(channel);

                            Notification notification = new NotificationCompat.Builder(OngoingTicketTeacher.this, "channel01")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("eContact")
                                    .setContentText("A message has been saved for the student!")
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

                            Notification notification = new NotificationCompat.Builder(OngoingTicketTeacher.this, "channel01")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("Error!: ")
                                    .setContentText(e.toString())
                                    .setDefaults(Notification.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                    .build();
                            notificationManager.notify(0, notification);

                        }
                    });
                }
            }
        });

        save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String informationTeacher2 = informationTeacherTwo.getText().toString();

                if (informationTeacher2.isEmpty()) {
                    informationTeacherTwo.setError("Complete the data for the teacher before saving!");
                } else {
                    final CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(indexTicket);

                    final DocumentReference documentReference = firebaseFirestore.collection("Accepted Applications")
                            .document(cloudFireOngoingTicket.id);
                    Map<String, Object> user = new HashMap<>();

                    user.put("nameTeacher", cloudFireOngoingTicket.nameTeacher);
                    user.put("surnameTeacher", cloudFireOngoingTicket.surnameTeacher);
                    user.put("facultyTeacher", cloudFireOngoingTicket.facultyTeacher);
                    user.put("fieldTeacher", cloudFireOngoingTicket.fieldTeacher);
                    user.put("emailTeacher1", cloudFireOngoingTicket.emailTeacher);

                    user.put("nameTeacher2", cloudFireOngoingTicket.nameTeacher2);
                    user.put("surnameTeacher2", cloudFireOngoingTicket.surnameTeacher2);
                    user.put("facultyTeacher2", cloudFireOngoingTicket.facultyTeacher2);
                    user.put("fieldTeacher2", cloudFireOngoingTicket.fieldTeacher2);
                    user.put("emailTeacher2", cloudFireOngoingTicket.emailTeacher2);

                    user.put("nameStudent", cloudFireOngoingTicket.nameStudent);
                    user.put("surnameStudent", cloudFireOngoingTicket.surnameStudent);
                    user.put("facultyStudent", cloudFireOngoingTicket.facultyStudent);
                    user.put("fieldStudent", cloudFireOngoingTicket.fieldStudent);
                    user.put("indexNumberStudent", cloudFireOngoingTicket.indexNumberStudent);
                    user.put("emailStudent", cloudFireOngoingTicket.emailStudent);

                    user.put("minuteTicket", cloudFireOngoingTicket.minuteTicket);
                    user.put("hourTicket", cloudFireOngoingTicket.hourTicket);

                    user.put("dayTicket", cloudFireOngoingTicket.dayTicket);
                    user.put("monthTicket", cloudFireOngoingTicket.monthTicket);
                    user.put("yearTicket", cloudFireOngoingTicket.yearTicket);

                    user.put("reasonType", cloudFireOngoingTicket.reasonType);
                    user.put("typeMeet", cloudFireOngoingTicket.typeMeet);

                    user.put("informationTeacher1", cloudFireOngoingTicket.informationTeacher1);
                    user.put("informationTeacher2", informationTeacher2);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onSuccess(Void aVoid) {

                            NotificationChannel channel = new NotificationChannel("channel01", "name",
                                    NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                            channel.setDescription("description");

                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                            notificationManager.createNotificationChannel(channel);

                            Notification notification = new NotificationCompat.Builder(OngoingTicketTeacher.this, "channel01")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("eContact")
                                    .setContentText("A message has been saved for the student!")
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

                            Notification notification = new NotificationCompat.Builder(OngoingTicketTeacher.this, "channel01")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("Error!: " )
                                    .setContentText(e.toString())
                                    .setDefaults(Notification.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                    .build();
                            notificationManager.notify(0, notification);
                        }
                    });
                }
            }
        });


        deleteTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder10 = new AlertDialog.Builder(OngoingTicketTeacher.this);
                dialogBuilder10.setTitle("Are you sure to delete the ticket?");
                dialogBuilder10.setMessage("Remember that the ticket will be deleted from other people participating in the event");
                dialogBuilder10.setCancelable(false);
                dialogBuilder10.setPositiveButton("Yes", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {

                        final CollectionReference collectionReference = firebaseFirestore.collection("Accepted Applications");
                        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(indexTicket);
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        if (document.getId().equals(cloudFireOngoingTicket.id)) {
                                            collectionReference.document(cloudFireOngoingTicket.id).delete();
                                            objectArrayList.remove(indexTicket);
                                            //databaseReference.child("uploadFile").removeValue();
                                        }
                                    }
                                    ///
                                    NotificationChannel channel = new NotificationChannel("channel01", "name",
                                            NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                                    channel.setDescription("description");

                                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                    notificationManager.createNotificationChannel(channel);

                                    Notification notification = new NotificationCompat.Builder(OngoingTicketTeacher.this, "channel01")
                                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                                            .setContentTitle("eContact")
                                            .setContentText("Delete ticket success!")
                                            .setDefaults(Notification.DEFAULT_ALL)
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                            .build();
                                    notificationManager.notify(0, notification);
                                    /////

                                    if (objectArrayList.size() == 0) {
                                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OngoingTicketTeacher.this);
                                        dialogBuilder.setTitle("Ongoing Ticket");
                                        dialogBuilder.setMessage("No tickets to display!");
                                        dialogBuilder.setCancelable(false);
                                        dialogBuilder.setNeutralButton("Back to Panel", new Dialog.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                Intent intent = new Intent(OngoingTicketTeacher.this, PanelTeacher.class);
                                                intent.putExtra("Email", emailTeacherLogin);
                                                startActivity(intent);
                                            }
                                        });
                                        dialogBuilder.create();
                                        dialogBuilder.show();
                                    }

                                    if ((objectArrayList.size() > 0) && (indexTicket > objectArrayList.size() - 1)) {
                                        indexTicket--;

                                        if (indexTicket == 0) {
                                            previousButton.setVisibility(GONE);
                                            cloudFireOngoingTicket = objectArrayList.get(indexTicket);

//////
                                            if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                                                    surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                                                    facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                                                    fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {

                                                textView.setText("Second Teacher:");

                                                informationTeacherOne.setVisibility(VISIBLE);
                                                save1.setVisibility(VISIBLE);
                                                informationTeacher1Text.setVisibility(GONE);

                                                informationTeacher2Text.setVisibility(VISIBLE);
                                                informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);

                                                OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                                                OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher2 + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher2, "jpg");
                                                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                            imageViewTeacher2.setImageBitmap(bitmap);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                                                OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                                                CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                                                final CloudFireOngoingTicket finalCloudFireOngoingTicket4 = cloudFireOngoingTicket;
                                                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                                if (document.getId().equals(finalCloudFireOngoingTicket4.id)) {
                                                                    nameTeacher2Canceled = document.getString("nameTeacher2");
                                                                    surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                                                    facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                                                    fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                                                    if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                                            && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                                                        if (document.getString("secondTeacher").equals("no")) {
                                                                            save2.setVisibility(GONE);
                                                                            informationTeacherTwo.setVisibility(GONE);
                                                                            informationTeacher2Text.setVisibility(VISIBLE);
                                                                            informationTeacher2Text.setText("No data!");
                                                                            nameTeacher2.setText("No data!");
                                                                            surnameTeacher2.setText("No data!");
                                                                        }
                                                                    } else {
                                                                        save2.setVisibility(GONE);
                                                                        informationTeacherTwo.setVisibility(GONE);
                                                                        informationTeacher2Text.setVisibility(VISIBLE);
                                                                        informationTeacher2Text.setText(finalCloudFireOngoingTicket4.informationTeacher2);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                            }


                                            if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher2) &&
                                                    surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher2) &&
                                                    facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher2) &&
                                                    fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher2)) {

                                                textView.setText("First Teacher:");

                                                OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                                                OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher, "jpg");
                                                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                            imageViewTeacher2.setImageBitmap(bitmap);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                                                OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                                                informationTeacherOne.setVisibility(GONE);
                                                save1.setVisibility(GONE);
                                                informationTeacher1Text.setVisibility(VISIBLE);
                                                informationTeacher1Text.setText(cloudFireOngoingTicket.informationTeacher1);

                                                informationTeacherTwo.setVisibility(VISIBLE);
                                                save2.setVisibility(VISIBLE);
                                                informationTeacher2Text.setVisibility(GONE);
                                            }
                                            //////

                                            nextButton.setVisibility(VISIBLE);
                                        }

                                        if (indexTicket > 0) {
                                            cloudFireOngoingTicket = objectArrayList.get(indexTicket);
                                            //////
                                            if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                                                    surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                                                    facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                                                    fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {

                                                textView.setText("Second Teacher:");

                                                informationTeacherOne.setVisibility(VISIBLE);
                                                save1.setVisibility(VISIBLE);
                                                informationTeacher1Text.setVisibility(GONE);

                                                informationTeacher2Text.setVisibility(VISIBLE);
                                                informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);

                                                OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                                                OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher2 + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher2, "jpg");
                                                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                            imageViewTeacher2.setImageBitmap(bitmap);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                                                OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                                                CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                                                final CloudFireOngoingTicket finalCloudFireOngoingTicket5 = cloudFireOngoingTicket;
                                                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                                if (document.getId().equals(finalCloudFireOngoingTicket5.id)) {
                                                                    nameTeacher2Canceled = document.getString("nameTeacher2");
                                                                    surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                                                    facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                                                    fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                                                    if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                                            && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                                                        if (document.getString("secondTeacher").equals("no")) {
                                                                            save2.setVisibility(GONE);
                                                                            informationTeacherTwo.setVisibility(GONE);
                                                                            informationTeacher2Text.setVisibility(VISIBLE);
                                                                            informationTeacher2Text.setText("No data!");
                                                                            nameTeacher2.setText("No data!");
                                                                            surnameTeacher2.setText("No data!");
                                                                        }
                                                                    } else {
                                                                        save2.setVisibility(GONE);
                                                                        informationTeacherTwo.setVisibility(GONE);
                                                                        informationTeacher2Text.setVisibility(VISIBLE);
                                                                        informationTeacher2Text.setText(finalCloudFireOngoingTicket5.informationTeacher2);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                            }


                                            if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher2) &&
                                                    surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher2) &&
                                                    facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher2) &&
                                                    fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher2)) {

                                                textView.setText("First Teacher:");

                                                OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                                                OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);
                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher, "jpg");
                                                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                            imageViewTeacher2.setImageBitmap(bitmap);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                                                OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                                                informationTeacherOne.setVisibility(GONE);
                                                save1.setVisibility(GONE);
                                                informationTeacher1Text.setVisibility(VISIBLE);
                                                informationTeacher1Text.setText(cloudFireOngoingTicket.informationTeacher1);

                                                informationTeacherTwo.setVisibility(VISIBLE);
                                                save2.setVisibility(VISIBLE);
                                                informationTeacher2Text.setVisibility(GONE);
                                            }
                                            //////

                                            nextButton.setVisibility(VISIBLE);
                                        } else {
                                            previousButton.setVisibility(GONE);
                                        }
                                    }

                                    if ((objectArrayList.size() > 0) && (indexTicket == 0)) {
                                        previousButton.setVisibility(View.VISIBLE);
                                        int sizeList = objectArrayList.size();
                                        indexTicket++;

                                        if (indexTicket == (sizeList - 1)) {
                                            nextButton.setVisibility(GONE);
                                            cloudFireOngoingTicket = objectArrayList.get(indexTicket);

                                            //////
                                            if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                                                    surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                                                    facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                                                    fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {

                                                textView.setText("Second Teacher:");


                                                informationTeacherOne.setVisibility(VISIBLE);
                                                save1.setVisibility(VISIBLE);
                                                informationTeacher1Text.setVisibility(GONE);

                                                informationTeacher2Text.setVisibility(VISIBLE);
                                                informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);

                                                OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                                                OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher2 + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher2, "jpg");
                                                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                            imageViewTeacher2.setImageBitmap(bitmap);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                                                OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                                                CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                                                final CloudFireOngoingTicket finalCloudFireOngoingTicket2 = cloudFireOngoingTicket;
                                                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                                if (document.getId().equals(finalCloudFireOngoingTicket2.id)) {
                                                                    nameTeacher2Canceled = document.getString("nameTeacher2");
                                                                    surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                                                    facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                                                    fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                                                    if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                                            && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                                                        if (document.getString("secondTeacher").equals("no")) {
                                                                            save2.setVisibility(GONE);
                                                                            informationTeacherTwo.setVisibility(GONE);
                                                                            informationTeacher2Text.setVisibility(VISIBLE);
                                                                            informationTeacher2Text.setText("No data!");
                                                                            nameTeacher2.setText("No data!");
                                                                            surnameTeacher2.setText("No data!");
                                                                        }
                                                                    } else {
                                                                        save2.setVisibility(GONE);
                                                                        informationTeacherTwo.setVisibility(GONE);
                                                                        informationTeacher2Text.setVisibility(VISIBLE);
                                                                        informationTeacher2Text.setText(finalCloudFireOngoingTicket2.informationTeacher2);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                            }


                                            if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher2) &&
                                                    surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher2) &&
                                                    facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher2) &&
                                                    fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher2)) {

                                                textView.setText("First Teacher:");

                                                OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                                                OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher, "jpg");
                                                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                            imageViewTeacher2.setImageBitmap(bitmap);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                                                OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
                                                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                            imageViewTeacher2.setImageBitmap(bitmap);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                informationTeacherOne.setVisibility(GONE);
                                                save1.setVisibility(GONE);
                                                informationTeacher1Text.setVisibility(VISIBLE);
                                                informationTeacher1Text.setText(cloudFireOngoingTicket.informationTeacher1);

                                                informationTeacherTwo.setVisibility(VISIBLE);
                                                save2.setVisibility(VISIBLE);
                                                informationTeacher2Text.setVisibility(GONE);
                                            }
                                            //////

                                        }


                                        if (indexTicket < sizeList) {
                                            cloudFireOngoingTicket = objectArrayList.get(indexTicket);
                                            if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                                                    surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                                                    facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                                                    fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {

                                                textView.setText("Second Teacher:");

                                                informationTeacherOne.setVisibility(VISIBLE);
                                                save1.setVisibility(VISIBLE);
                                                informationTeacher1Text.setVisibility(GONE);

                                                informationTeacher2Text.setVisibility(VISIBLE);
                                                informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);

                                                OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                                                OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher2 + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher2, "jpg");
                                                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                            imageViewTeacher2.setImageBitmap(bitmap);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                                                OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent+ ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                                                CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                                                final CloudFireOngoingTicket finalCloudFireOngoingTicket3 = cloudFireOngoingTicket;
                                                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                                if (document.getId().equals(finalCloudFireOngoingTicket3.id)) {
                                                                    nameTeacher2Canceled = document.getString("nameTeacher2");
                                                                    surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                                                    facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                                                    fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                                                    if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                                            && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                                                        if (document.getString("secondTeacher").equals("no")) {
                                                                            save2.setVisibility(GONE);
                                                                            informationTeacherTwo.setVisibility(GONE);
                                                                            informationTeacher2Text.setVisibility(VISIBLE);
                                                                            informationTeacher2Text.setText("No data!");
                                                                            nameTeacher2.setText("No data!");
                                                                            surnameTeacher2.setText("No data!");
                                                                        }
                                                                    } else {
                                                                        save2.setVisibility(GONE);
                                                                        informationTeacherTwo.setVisibility(GONE);
                                                                        informationTeacher2Text.setVisibility(VISIBLE);
                                                                        informationTeacher2Text.setText(finalCloudFireOngoingTicket3.informationTeacher2);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                            }


                                            if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher2) &&
                                                    surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher2) &&
                                                    facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher2) &&
                                                    fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher2)) {

                                                textView.setText("First Teacher:");

                                                OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                                                OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher, "jpg");
                                                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                            imageViewTeacher2.setImageBitmap(bitmap);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                                                OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                                                informationTeacherOne.setVisibility(GONE);
                                                save1.setVisibility(GONE);
                                                informationTeacher1Text.setVisibility(VISIBLE);
                                                informationTeacher1Text.setText(cloudFireOngoingTicket.informationTeacher1);

                                                informationTeacherTwo.setVisibility(VISIBLE);
                                                save2.setVisibility(VISIBLE);
                                                informationTeacher2Text.setVisibility(GONE);
                                            }

                                        } else {
                                            nextButton.setVisibility(GONE);
                                        }
                                    }

                                    if ((objectArrayList.size() > 0) && (indexTicket < objectArrayList.size() - 1) && (indexTicket > 0)) {
                                        previousButton.setVisibility(View.VISIBLE);
                                        int sizeList = objectArrayList.size();
                                        indexTicket++;

                                        if (indexTicket == (sizeList - 1)) {
                                            nextButton.setVisibility(GONE);
                                            cloudFireOngoingTicket = objectArrayList.get(indexTicket);

                                            //////
                                            if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                                                    surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                                                    facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                                                    fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {

                                                textView.setText("Second Teacher:");


                                                informationTeacherOne.setVisibility(VISIBLE);
                                                save1.setVisibility(VISIBLE);
                                                informationTeacher1Text.setVisibility(GONE);

                                                informationTeacher2Text.setVisibility(VISIBLE);
                                                informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);

                                                OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                                                OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher2 + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher2, "jpg");
                                                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                            imageViewTeacher2.setImageBitmap(bitmap);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                                                OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                                                CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                                                final CloudFireOngoingTicket finalCloudFireOngoingTicket1 = cloudFireOngoingTicket;
                                                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                                if (document.getId().equals(finalCloudFireOngoingTicket1.id)) {
                                                                    nameTeacher2Canceled = document.getString("nameTeacher2");
                                                                    surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                                                    facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                                                    fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                                                    if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                                            && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                                                        if (document.getString("secondTeacher").equals("no")) {
                                                                            save2.setVisibility(GONE);
                                                                            informationTeacherTwo.setVisibility(GONE);
                                                                            informationTeacher2Text.setVisibility(VISIBLE);
                                                                            informationTeacher2Text.setText("No data!");
                                                                            nameTeacher2.setText("No data!");
                                                                            surnameTeacher2.setText("No data!");
                                                                        }
                                                                    } else {
                                                                        save2.setVisibility(GONE);
                                                                        informationTeacherTwo.setVisibility(GONE);
                                                                        informationTeacher2Text.setVisibility(VISIBLE);
                                                                        informationTeacher2Text.setText(finalCloudFireOngoingTicket1.informationTeacher2);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                            }


                                            if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher2) &&
                                                    surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher2) &&
                                                    facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher2) &&
                                                    fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher2)) {

                                                textView.setText("First Teacher:");

                                                OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                                                OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher, "jpg");
                                                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                            imageViewTeacher2.setImageBitmap(bitmap);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                                                OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                                                informationTeacherOne.setVisibility(GONE);
                                                save1.setVisibility(GONE);
                                                informationTeacher1Text.setVisibility(VISIBLE);
                                                informationTeacher1Text.setText(cloudFireOngoingTicket.informationTeacher1);

                                                informationTeacherTwo.setVisibility(VISIBLE);
                                                save2.setVisibility(VISIBLE);
                                                informationTeacher2Text.setVisibility(GONE);
                                            }
                                            //////

                                        }


                                        if (indexTicket < sizeList) {
                                            cloudFireOngoingTicket = objectArrayList.get(indexTicket);
                                            //////
                                            if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                                                    surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                                                    facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                                                    fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {

                                                textView.setText("Second Teacher:");

                                                informationTeacherOne.setVisibility(VISIBLE);
                                                save1.setVisibility(VISIBLE);
                                                informationTeacher1Text.setVisibility(GONE);

                                                informationTeacher2Text.setVisibility(VISIBLE);
                                                informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);

                                                OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                                                OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher2 + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher2, "jpg");
                                                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                            imageViewTeacher2.setImageBitmap(bitmap);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                                                OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                                                CollectionReference collectionReference = firebaseFirestore.collection("Canceled Applications");
                                                final CloudFireOngoingTicket finalCloudFireOngoingTicket = cloudFireOngoingTicket;
                                                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                                if (document.getId().equals(finalCloudFireOngoingTicket.id)) {
                                                                    nameTeacher2Canceled = document.getString("nameTeacher2");
                                                                    surnameTeacher2Canceled = document.getString("surnameTeacher2");
                                                                    facultyTeacher2Canceled = document.getString("facultyTeacher2");
                                                                    fieldTeacher2Canceled = document.getString("fieldTeacher2");

                                                                    if (!(nameTeacher2Canceled.isEmpty() && surnameTeacher2Canceled.isEmpty()
                                                                            && facultyTeacher2Canceled.isEmpty() && fieldTeacher2Canceled.isEmpty())) {

                                                                        if (document.getString("secondTeacher").equals("no")) {
                                                                            save2.setVisibility(GONE);
                                                                            informationTeacherTwo.setVisibility(GONE);
                                                                            informationTeacher2Text.setVisibility(VISIBLE);
                                                                            informationTeacher2Text.setText("No data!");
                                                                            nameTeacher2.setText("No data!");
                                                                            surnameTeacher2.setText("No data!");
                                                                        }
                                                                    } else {
                                                                        save2.setVisibility(GONE);
                                                                        informationTeacherTwo.setVisibility(GONE);
                                                                        informationTeacher2Text.setVisibility(VISIBLE);
                                                                        informationTeacher2Text.setText(finalCloudFireOngoingTicket.informationTeacher2);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                            }


                                            if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher2) &&
                                                    surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher2) &&
                                                    facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher2) &&
                                                    fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher2)) {

                                                textView.setText("First Teacher:");

                                                OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                                                OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailTeacher + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailTeacher, "jpg");
                                                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                            imageViewTeacher2.setImageBitmap(bitmap);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                                                OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                                                storageReference = FirebaseStorage.getInstance().getReference().child(cloudFireOngoingTicket.emailStudent + ".jpg");

                                                try {
                                                    final File localFile = File.createTempFile(cloudFireOngoingTicket.emailStudent, "jpg");
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

                                                informationTeacherOne.setVisibility(GONE);
                                                save1.setVisibility(GONE);
                                                informationTeacher1Text.setVisibility(VISIBLE);
                                                informationTeacher1Text.setText(cloudFireOngoingTicket.informationTeacher1);

                                                informationTeacherTwo.setVisibility(VISIBLE);
                                                save2.setVisibility(VISIBLE);
                                                informationTeacher2Text.setVisibility(GONE);
                                            }
                                            //////

                                        } else {
                                            nextButton.setVisibility(GONE);
                                        }
                                    }

                                }
                            }
                        });
                    }
                });

                dialogBuilder10.setNeutralButton("No", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                dialogBuilder10.create();
                dialogBuilder10.show();
            }
        });
    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        valueType = 1;
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "PDF FILE SELECT"), 12);
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/jpeg");
        valueType = 2;
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "IMAGE FILE SELECT"), 12);
    }

    private void selectDOC() {
        Intent intent = new Intent();
        intent.setType("application/msword");
        valueType = 3;
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "DOCUMENT FILE SELECT"), 12);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 12) && (resultCode == RESULT_OK) && (data != null) && (data.getData() != null)) {
            uploadFile.setEnabled(true);

            //selectFile.setText(data.getDataString()
            //        .substring(data.getDataString().lastIndexOf("/") + 1));
            //


            if (valueType == 1) {

                final CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(indexTicket);

                ///
                valueFile = "File " + System.currentTimeMillis();
                nameFile = "File " + System.currentTimeMillis() + ".pdf";
                selectFile.setText(nameFile);
                ///
                namePathFileView = "Preview " + nameFile;


                /*
                StorageReference reference = storageReference.child(namePathFileView);
                reference.putFile(data.getData())
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isComplete()) ;
                                Uri uri = uriTask.getResult();
                                putPDF putPDF = new putPDF(nameFile, uri.toString(), "PDF");

                                databaseReference = FirebaseDatabase.getInstance().getReference(cloudFireOngoingTicket.nameStudent + cloudFireOngoingTicket.surnameStudent + "to" +
                                        cloudFireOngoingTicket.nameTeacher + cloudFireOngoingTicket.surnameTeacher
                                        + cloudFireOngoingTicket.dayTicket + cloudFireOngoingTicket.monthTicket + cloudFireOngoingTicket.yearTicket
                                        + cloudFireOngoingTicket.minuteTicket + cloudFireOngoingTicket.hourTicket);

                                databaseReference
                                        .child("Path - " + valueFile).setValue(putPDF);
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    }
                });

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OngoingTicketTeacher.this);
                dialogBuilder.setTitle("Ongoing Ticket");
                dialogBuilder.setMessage("Do you want to check the photo before uploading to the ticket?");
                dialogBuilder.setCancelable(false);

                dialogBuilder.setNegativeButton("No", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                dialogBuilder.setPositiveButton("Yes", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        databaseReference = FirebaseDatabase.getInstance().getReference("Preview " + databaseReferenceNamePath);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    if (ds.toString().equals("Path - " + valueFile)) {
                                        uploadedPDF = ds.getValue(com.example.econtact.putPDF.class);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(uploadedPDF.getUrl()), "application/pdf");
                        startActivity(intent);
                    }
                });

                dialogBuilder.create();
                dialogBuilder.show();








                 */


                /*Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(data.getData(),"application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                Intent intent = Intent.createChooser(target, "Open File");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Instruct the user to install a PDF reader here, or something
                }

                 */
            }

            if (valueType == 2) {

                valueFile = "File " + System.currentTimeMillis();
                nameFile = "File " + System.currentTimeMillis() + ".jpg";
                selectFile.setText(nameFile);


                /*Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(data.getData(), "image/jpeg");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                Intent intent = Intent.createChooser(target, "Open File");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Instruct the user to install a PDF reader here, or something
                }

                 */
            }

            if (valueType == 3) {

                valueFile = "File " + System.currentTimeMillis();
                nameFile = "File " + System.currentTimeMillis() + ".doc";
                selectFile.setText(nameFile);

                /*Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(data.getData(), "application/msword");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                Intent intent = Intent.createChooser(target, "Open File");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Instruct the user to install a PDF reader here, or something
                }

                 */
            }

            uploadFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadPDFFileFirebase(data.getData());
                }
            });

        }
    }


            private void uploadPDFFileFirebase(Uri data) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("File is loading...");
                progressDialog.show();

                final CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(indexTicket);

                if (valueType == 1) {
                    StorageReference reference = storageReference.child(nameFile);
                    reference.putFile(data)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!uriTask.isComplete()) ;
                                    Uri uri = uriTask.getResult();
                                    putPDF putPDF = new putPDF(nameFile, uri.toString(), "PDF");

                                    databaseReference = FirebaseDatabase.getInstance().getReference(cloudFireOngoingTicket.nameStudent + cloudFireOngoingTicket.surnameStudent + "to" +
                                            cloudFireOngoingTicket.nameTeacher + cloudFireOngoingTicket.surnameTeacher
                                            + cloudFireOngoingTicket.dayTicket + cloudFireOngoingTicket.monthTicket + cloudFireOngoingTicket.yearTicket
                                            + cloudFireOngoingTicket.minuteTicket + cloudFireOngoingTicket.hourTicket);

                                    databaseReference
                                            .child("Path - " + valueFile).setValue(putPDF);

                                    //// Notification!
                                    NotificationChannel channel = new NotificationChannel("channel01", "name",
                                            NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                                    channel.setDescription("description");

                                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                    notificationManager.createNotificationChannel(channel);

                                    Notification notification = new NotificationCompat.Builder(OngoingTicketTeacher.this, "channel01")
                                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                                            .setContentTitle("eContact")
                                            .setContentText("File Upload complete!")
                                            .setDefaults(Notification.DEFAULT_ALL)
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                            .build();
                                    notificationManager.notify(0, notification);
                                    progressDialog.dismiss();
                                    ///
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            progressDialog.setMessage("File Uploaded.." + (int) progress + "%");
                        }
                    });
                }

                if(valueType == 2){
                    StorageReference reference = storageReference.child(nameFile);
                    reference.putFile(data)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!uriTask.isComplete()) ;
                                    Uri uri = uriTask.getResult();
                                    putPDF putPDF = new putPDF(nameFile, uri.toString(), "JPG");

                                    databaseReference = FirebaseDatabase.getInstance().getReference(cloudFireOngoingTicket.nameStudent + cloudFireOngoingTicket.surnameStudent + "to" +
                                            cloudFireOngoingTicket.nameTeacher + cloudFireOngoingTicket.surnameTeacher
                                            + cloudFireOngoingTicket.dayTicket + cloudFireOngoingTicket.monthTicket + cloudFireOngoingTicket.yearTicket
                                            + cloudFireOngoingTicket.minuteTicket + cloudFireOngoingTicket.hourTicket);

                                    databaseReference
                                            .child("Path - " + valueFile).setValue(putPDF);

                                    //// Notification!
                                    NotificationChannel channel = new NotificationChannel("channel01", "name",
                                            NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                                    channel.setDescription("description");

                                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                    notificationManager.createNotificationChannel(channel);

                                    Notification notification = new NotificationCompat.Builder(OngoingTicketTeacher.this, "channel01")
                                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                                            .setContentTitle("eContact")
                                            .setContentText("File Upload complete!")
                                            .setDefaults(Notification.DEFAULT_ALL)
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                            .build();
                                    notificationManager.notify(0, notification);
                                    progressDialog.dismiss();
                                    ///
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            progressDialog.setMessage("File Uploaded.." + (int) progress + "%");
                        }
                    });
                }

                if(valueType == 3){
                    StorageReference reference = storageReference.child(nameFile);
                    reference.putFile(data)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!uriTask.isComplete()) ;
                                    Uri uri = uriTask.getResult();
                                    putPDF putPDF = new putPDF(nameFile, uri.toString(), "DOC");

                                    databaseReference = FirebaseDatabase.getInstance().getReference(cloudFireOngoingTicket.nameStudent + cloudFireOngoingTicket.surnameStudent + "to" +
                                            cloudFireOngoingTicket.nameTeacher + cloudFireOngoingTicket.surnameTeacher
                                            + cloudFireOngoingTicket.dayTicket + cloudFireOngoingTicket.monthTicket + cloudFireOngoingTicket.yearTicket
                                            + cloudFireOngoingTicket.minuteTicket + cloudFireOngoingTicket.hourTicket);

                                    databaseReference
                                            .child("Path - " + valueFile).setValue(putPDF);

                                    //// Notification!
                                    NotificationChannel channel = new NotificationChannel("channel01", "name",
                                            NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                                    channel.setDescription("description");

                                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                    notificationManager.createNotificationChannel(channel);

                                    Notification notification = new NotificationCompat.Builder(OngoingTicketTeacher.this, "channel01")
                                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                                            .setContentTitle("eContact")
                                            .setContentText("File Upload complete!")
                                            .setDefaults(Notification.DEFAULT_ALL)
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                            .build();
                                    notificationManager.notify(0, notification);
                                    progressDialog.dismiss();
                                    ///
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            progressDialog.setMessage("File Uploaded.." + (int) progress + "%");
                        }
                    });
                }

            }
}
class CloudFireOngoingTicket {

    String id = " ";

    String nameTeacher = " ";
    String surnameTeacher = " ";
    String facultyTeacher = " ";
    String fieldTeacher = " ";
    String emailTeacher = " ";


    String nameTeacher2 = " ";
    String surnameTeacher2 = " ";
    String facultyTeacher2 = " ";
    String fieldTeacher2 = " ";
    String emailTeacher2 = " ";

    String nameStudent = " ";
    String surnameStudent = " ";
    String facultyStudent = " ";
    String fieldStudent = " ";
    String semesterStudent = " ";
    String degreeStudent = " ";
    String indexNumberStudent = " ";
    String emailStudent = " ";

    String minuteTicket = " ";
    String hourTicket = " ";
    String dayTicket = " ";

    String monthTicket = " ";
    String yearTicket = " ";

    String reasonType = " ";
    String typeMeet = " ";

    String informationTeacher1 = " ";
    String informationTeacher2 = " ";

    public CloudFireOngoingTicket(String id, String nameTeacher, String surnameTeacher, String facultyTeacher, String fieldTeacher, String emailTeacher,
                                  String nameTeacher2, String surnameTeacher2, String facultyTeacher2, String fieldTeacher2, String emailTeacher2,
                                  String nameStudent, String surnameStudent, String facultyStudent, String fieldStudent,
                                  String semesterStudent, String degreeStudent, String indexNumberStudent, String emailStudent,
                                  String minuteTicket, String hourTicket, String dayTicket, String monthTicket, String yearTicket,
                                  String reasonType, String typeMeet, String informationTeacher1, String informationTeacher2) {

        this.id = id;

        this.nameTeacher = nameTeacher;
        this.surnameTeacher = surnameTeacher;
        this.facultyTeacher = facultyTeacher;
        this.fieldTeacher = fieldTeacher;
        this.emailTeacher = emailTeacher;

        this.nameTeacher2 = nameTeacher2;
        this.surnameTeacher2 = surnameTeacher2;
        this.facultyTeacher2 = facultyTeacher2;
        this.fieldTeacher2 = fieldTeacher2;
        this.emailTeacher2 = emailTeacher2;

        this.nameStudent = nameStudent;
        this.surnameStudent = surnameStudent;
        this.facultyStudent = facultyStudent;
        this.fieldStudent = fieldStudent;
        this.semesterStudent = semesterStudent;
        this.degreeStudent = degreeStudent;
        this.indexNumberStudent = indexNumberStudent;
        this.emailStudent = emailStudent;

        this.minuteTicket = minuteTicket;
        this.hourTicket = hourTicket;
        this.dayTicket = dayTicket;

        this.monthTicket = monthTicket;
        this.yearTicket = yearTicket;

        this.reasonType = reasonType;
        this.typeMeet = typeMeet;

        this.informationTeacher1 = informationTeacher1;
        this.informationTeacher2 = informationTeacher2;
    }
}