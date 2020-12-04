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
import android.widget.EditText;
import android.widget.TextView;

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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class OngoingTicketTeacher extends AppCompatActivity {

    Button previousButton, nextButton, save1, save2, delete;
    EditText informationTeacherOne, informationTeacherTwo;

    TextView nameStudent, surnameStudent, nameTeacher2, surnameTeacher2, informationTeacher1Text, informationTeacher2Text;

    String nameTeacher2Canceled = " ", surnameTeacher2Canceled = " ", facultyTeacher2Canceled = " ",
            fieldTeacher2Canceled = " ";

    String nameTeacherLogin, surnameTeacherLogin, facultyTeacherLogin, fieldTeacherLogin;
    FirebaseFirestore firebaseFirestore;
    int indexTicket = 0;
    List<CloudFireOngoingTicket> objectArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_ticket_teacher);

        previousButton = findViewById(R.id.previous_ongoingTicket);
        nextButton = findViewById(R.id.next_ongoingTicket);
        save1 = findViewById(R.id.save1_ongoingTicketTeacher);
        save2 = findViewById(R.id.save2_ongoingTicketTeacher);
        delete = findViewById(R.id.delete_ongoingTicketTeacher);

        nameStudent = findViewById(R.id.nameStudent_ongoingTicketTeacher);
        surnameStudent = findViewById(R.id.surnameStudent_ongoingTicketTeacher);

        nameTeacher2 = findViewById(R.id.nameTeacher2_ongoingTicketTeacher);
        surnameTeacher2 = findViewById(R.id.surnameTeacher2_ongoingTicketTeacher);

        informationTeacher1Text = findViewById(R.id.informationTeacher1Text_ongoingTicketTeacher2);
        informationTeacher2Text = findViewById(R.id.informationTeacher2Text_ongoingTicketTeacher);
        informationTeacherOne = findViewById(R.id.informationTeacher1_ongoingTicketTeacher);
        informationTeacherTwo = findViewById(R.id.informationTeacher2_ongoingTicketTeacher);

        nameTeacherLogin = getIntent().getStringExtra("nameTeacher");
        surnameTeacherLogin = getIntent().getStringExtra("surnameTeacher");
        facultyTeacherLogin = getIntent().getStringExtra("facultyTeacher");
        fieldTeacherLogin = getIntent().getStringExtra("fieldTeacher");

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

                        if (nameTeacher.equals(nameTeacherLogin) && surnameTeacher.equals(surnameTeacherLogin)
                                && facultyTeacher.equals(facultyTeacherLogin) && fieldTeacher.equals(fieldTeacherLogin)) {
                            CloudFireOngoingTicket cloudFireOngoingTicket = new CloudFireOngoingTicket(
                                    document.getId(),

                                    document.getString("nameTeacher"),
                                    document.getString("surnameTeacher"),
                                    document.getString("facultyTeacher"),
                                    document.getString("fieldTeacher"),

                                    document.getString("nameTeacher2"),
                                    document.getString("surnameTeacher2"),
                                    document.getString("facultyTeacher2"),
                                    document.getString("fieldTeacher2"),

                                    document.getString("nameStudent"),
                                    document.getString("surnameStudent"),
                                    document.getString("facultyStudent"),
                                    document.getString("fieldStudent"),
                                    document.getString("semesterStudent"),
                                    document.getString("degreeStudent"),
                                    document.getString("indexNumberStudent"),

                                    document.getString("minuteTicket"),
                                    document.getString("hourTicket"),

                                    document.getString("dayTicket"),
                                    document.getString("monthTicket"),
                                    document.getString("yearTicket"),

                                    document.getString("reasonType"),
                                    document.getString("typeMeet"),

                                    document.getString("informationTeacher"),
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

                                    document.getString("nameTeacher2"),
                                    document.getString("surnameTeacher2"),
                                    document.getString("facultyTeacher2"),
                                    document.getString("fieldTeacher2"),

                                    document.getString("nameStudent"),
                                    document.getString("surnameStudent"),
                                    document.getString("facultyStudent"),
                                    document.getString("fieldStudent"),
                                    document.getString("semesterStudent"),
                                    document.getString("degreeStudent"),
                                    document.getString("indexNumberStudent"),

                                    document.getString("minuteTicket"),
                                    document.getString("hourTicket"),

                                    document.getString("dayTicket"),
                                    document.getString("monthTicket"),
                                    document.getString("yearTicket"),

                                    document.getString("reasonType"),
                                    document.getString("typeMeet"),

                                    document.getString("informationTeacher"),
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
                    final CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(0);


                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {
                        informationTeacherOne.setVisibility(VISIBLE);

                        informationTeacher1Text.setVisibility(GONE);

                        save1.setVisibility(VISIBLE);

                        informationTeacher2Text.setVisibility(VISIBLE);

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

                        informationTeacher2Text.setText(cloudFireOngoingTicket.informationTeacher2);

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
                                                    informationTeacher2Text.setText("No data!");
                                                    nameTeacher2.setText("No data!");
                                                    surnameTeacher2.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(GONE);
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

                        informationTeacherTwo.setVisibility(VISIBLE);
                        save2.setVisibility(VISIBLE);

                        informationTeacher1Text.setVisibility(VISIBLE);
                        save1.setVisibility(GONE);


                        informationTeacher1Text.setText(cloudFireOngoingTicket.informationTeacher1);

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

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
                                                    informationTeacherTwo.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(VISIBLE);
                                                informationTeacherTwo.setText(cloudFireOngoingTicket.informationTeacher2);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }


                }

                if (objectArrayList.size() > 1) {
                    nextButton.setVisibility(VISIBLE);
                    final CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(0);


                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {
                        informationTeacherOne.setVisibility(VISIBLE);

                        informationTeacher1Text.setVisibility(GONE);


                        save1.setVisibility(VISIBLE);

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

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
                                                    informationTeacherTwo.setText("No data!");
                                                    nameTeacher2.setText("No data!");
                                                    surnameTeacher2.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(VISIBLE);
                                                informationTeacherTwo.setText(cloudFireOngoingTicket.informationTeacher2);
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
                        informationTeacherTwo.setVisibility(VISIBLE);
                        save2.setVisibility(VISIBLE);

                        save1.setVisibility(GONE);

                        if (cloudFireOngoingTicket.informationTeacher1.equals(" ")) {
                            informationTeacherOne.setText("No data!");
                        } else {
                            informationTeacherOne.setText(cloudFireOngoingTicket.informationTeacher1);
                        }

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameTeacher2);

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
                                                    informationTeacherTwo.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(VISIBLE);
                                                informationTeacherTwo.setText(cloudFireOngoingTicket.informationTeacher2);
                                            }
                                        }
                                    }
                                }
                            }
                        });
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
                    nextButton.setVisibility(GONE);
                    final CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(indexTicket);

                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {
                        informationTeacherOne.setVisibility(VISIBLE);

                        informationTeacher1Text.setVisibility(GONE);


                        save1.setVisibility(VISIBLE);

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

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
                                                    informationTeacherTwo.setText("No data!");
                                                    nameTeacher2.setText("No data!");
                                                    surnameTeacher2.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(VISIBLE);
                                                informationTeacherTwo.setText(cloudFireOngoingTicket.informationTeacher2);
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
                        informationTeacherTwo.setVisibility(VISIBLE);
                        save2.setVisibility(VISIBLE);

                        informationTeacher1Text.setVisibility(GONE);
                        save1.setVisibility(GONE);

                        if (cloudFireOngoingTicket.informationTeacher1.isEmpty()) {
                            informationTeacher1Text.setText("No data!");
                        } else {
                            informationTeacherOne.setText(cloudFireOngoingTicket.informationTeacher1);
                        }

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameTeacher2);

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
                                                    informationTeacherTwo.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(VISIBLE);
                                                informationTeacherTwo.setText(cloudFireOngoingTicket.informationTeacher2);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }

                }


                if (indexTicket < sizeList) {
                    final CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(indexTicket);


                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {
                        informationTeacherOne.setVisibility(VISIBLE);

                        informationTeacher1Text.setVisibility(GONE);


                        save1.setVisibility(VISIBLE);

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

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
                                                    informationTeacherTwo.setText("No data!");
                                                    nameTeacher2.setText("No data!");
                                                    surnameTeacher2.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(VISIBLE);
                                                informationTeacherTwo.setText(cloudFireOngoingTicket.informationTeacher2);
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
                        informationTeacherTwo.setVisibility(VISIBLE);
                        save2.setVisibility(VISIBLE);

                        save1.setVisibility(GONE);

                        if (cloudFireOngoingTicket.informationTeacher1.equals(" ")) {
                            informationTeacherOne.setText("No data!");
                        } else {
                            informationTeacherOne.setText(cloudFireOngoingTicket.informationTeacher1);
                        }

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameTeacher2);

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
                                                    informationTeacherTwo.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(VISIBLE);
                                                informationTeacherTwo.setText(cloudFireOngoingTicket.informationTeacher2);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }

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


                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {
                        informationTeacherOne.setVisibility(VISIBLE);

                        informationTeacher1Text.setVisibility(GONE);


                        save1.setVisibility(VISIBLE);

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

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
                                                    informationTeacherTwo.setText("No data!");
                                                    nameTeacher2.setText("No data!");
                                                    surnameTeacher2.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(VISIBLE);
                                                informationTeacherTwo.setText(cloudFireOngoingTicket.informationTeacher2);
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
                        informationTeacherTwo.setVisibility(VISIBLE);
                        save2.setVisibility(VISIBLE);


                        save1.setVisibility(GONE);

                        if (cloudFireOngoingTicket.informationTeacher1.equals(" ")) {
                            informationTeacherOne.setText("No data!");
                        } else {
                            informationTeacherOne.setText(cloudFireOngoingTicket.informationTeacher1);
                        }

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameTeacher2);

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
                                                    informationTeacherTwo.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(VISIBLE);
                                                informationTeacherTwo.setText(cloudFireOngoingTicket.informationTeacher2);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }

                    nextButton.setVisibility(VISIBLE);
                }

                if (indexTicket > 0) {
                    final CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(indexTicket);


                    if (nameTeacherLogin.equals(cloudFireOngoingTicket.nameTeacher) &&
                            surnameTeacherLogin.equals(cloudFireOngoingTicket.surnameTeacher) &&
                            facultyTeacherLogin.equals(cloudFireOngoingTicket.facultyTeacher) &&
                            fieldTeacherLogin.equals(cloudFireOngoingTicket.fieldTeacher)) {
                        informationTeacherOne.setVisibility(VISIBLE);

                        informationTeacher1Text.setVisibility(GONE);

                        save1.setVisibility(VISIBLE);

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher2);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher2);

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameStudent);

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
                                                    informationTeacherTwo.setText("No data!");
                                                    nameTeacher2.setText("No data!");
                                                    surnameTeacher2.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(VISIBLE);
                                                informationTeacherTwo.setText(cloudFireOngoingTicket.informationTeacher2);
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
                        informationTeacherTwo.setVisibility(VISIBLE);
                        save2.setVisibility(VISIBLE);

                        save1.setVisibility(GONE);

                        if (cloudFireOngoingTicket.informationTeacher1.equals(" ")) {
                            informationTeacherOne.setText("No data!");
                        } else {
                            informationTeacherOne.setText(cloudFireOngoingTicket.informationTeacher1);
                        }

                        OngoingTicketTeacher.this.nameTeacher2.setText(cloudFireOngoingTicket.nameTeacher);
                        OngoingTicketTeacher.this.surnameTeacher2.setText(cloudFireOngoingTicket.surnameTeacher);

                        OngoingTicketTeacher.this.nameStudent.setText(cloudFireOngoingTicket.nameStudent);
                        OngoingTicketTeacher.this.surnameStudent.setText(cloudFireOngoingTicket.surnameTeacher2);

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
                                                    informationTeacherTwo.setText("No data!");
                                                }
                                            } else {
                                                save2.setVisibility(GONE);
                                                informationTeacherTwo.setVisibility(VISIBLE);
                                                informationTeacherTwo.setText(cloudFireOngoingTicket.informationTeacher2);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }

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

                    user.put("nameTeacher2", cloudFireOngoingTicket.nameTeacher2);
                    user.put("surnameTeacher2", cloudFireOngoingTicket.surnameTeacher2);
                    user.put("facultyTeacher2", cloudFireOngoingTicket.facultyTeacher2);
                    user.put("fieldTeacher2", cloudFireOngoingTicket.fieldTeacher2);

                    user.put("nameStudent", cloudFireOngoingTicket.nameStudent);
                    user.put("surnameStudent", cloudFireOngoingTicket.surnameStudent);
                    user.put("facultyStudent", cloudFireOngoingTicket.facultyStudent);
                    user.put("fieldStudent", cloudFireOngoingTicket.fieldStudent);
                    user.put("indexNumberStudent", cloudFireOngoingTicket.indexNumberStudent);

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
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "Error!: " + e.toString());
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

                    user.put("nameTeacher2", cloudFireOngoingTicket.nameTeacher2);
                    user.put("surnameTeacher2", cloudFireOngoingTicket.surnameTeacher2);
                    user.put("facultyTeacher2", cloudFireOngoingTicket.facultyTeacher2);
                    user.put("fieldTeacher2", cloudFireOngoingTicket.fieldTeacher2);

                    user.put("nameStudent", cloudFireOngoingTicket.nameStudent);
                    user.put("surnameStudent", cloudFireOngoingTicket.surnameStudent);
                    user.put("facultyStudent", cloudFireOngoingTicket.facultyStudent);
                    user.put("fieldStudent", cloudFireOngoingTicket.fieldStudent);
                    user.put("indexNumberStudent", cloudFireOngoingTicket.indexNumberStudent);

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
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "Error!: " + e.toString());
                        }
                    });
                }
            }
        });
    }
}

class CloudFireOngoingTicket {

    String id = " ";

    String nameTeacher = " ";
    String surnameTeacher = " ";
    String facultyTeacher = " ";
    String fieldTeacher = " ";

    String nameTeacher2 = " ";
    String surnameTeacher2 = " ";
    String facultyTeacher2 = " ";
    String fieldTeacher2 = " ";

    String nameStudent = " ";
    String surnameStudent = " ";
    String facultyStudent = " ";
    String fieldStudent = " ";
    String semesterStudent = " ";
    String degreeStudent = " ";
    String indexNumberStudent = " ";

    String minuteTicket = " ";
    String hourTicket = " ";
    String dayTicket = " ";

    String monthTicket = " ";
    String yearTicket = " ";

    String reasonType = " ";
    String typeMeet = " ";

    String informationTeacher1 = " ";
    String informationTeacher2 = " ";

    public CloudFireOngoingTicket(String id, String nameTeacher, String surnameTeacher, String facultyTeacher, String fieldTeacher,
                                  String nameTeacher2, String surnameTeacher2, String facultyTeacher2, String fieldTeacher2,
                                  String nameStudent, String surnameStudent, String facultyStudent, String fieldStudent,
                                  String semesterStudent, String degreeStudent, String indexNumberStudent,
                                  String minuteTicket, String hourTicket, String dayTicket, String monthTicket, String yearTicket,
                                  String reasonType, String typeMeet, String informationTeacher1, String informationTeacher2) {

        this.id = id;

        this.nameTeacher = nameTeacher;
        this.surnameTeacher = surnameTeacher;
        this.facultyTeacher = facultyTeacher;
        this.fieldTeacher = fieldTeacher;

        this.nameTeacher2 = nameTeacher2;
        this.surnameTeacher2 = surnameTeacher2;
        this.facultyTeacher2 = facultyTeacher2;
        this.fieldTeacher2 = fieldTeacher2;

        this.nameStudent = nameStudent;
        this.surnameStudent = surnameStudent;
        this.facultyStudent = facultyStudent;
        this.fieldStudent = fieldStudent;
        this.semesterStudent = semesterStudent;
        this.degreeStudent = degreeStudent;
        this.indexNumberStudent = indexNumberStudent;

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