package com.example.econtact;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.View.VISIBLE;

public class OngoingTicket extends AppCompatActivity {

    Button secondTeacherAddButton, previousButton, nextButton, saveButton;
    EditText informationTeacherOne, informationTeacherTwo;
    TextView textView2;

    ///
    String nameTeacher, surnameTeacher, facultyTeacher, fieldTeacher;
    FirebaseFirestore firebaseFirestore;
    int indexTicket = 0;
    List<CloudFireOngoingTicket> objectArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_ticket);
        secondTeacherAddButton = findViewById(R.id.secondTeacherButton_ongoingTicket);
        previousButton = findViewById(R.id.previous_ongoingTicket);
        nextButton = findViewById(R.id.next_ongoingTicket);
        saveButton = findViewById(R.id.save_ongoingTicket);
        informationTeacherOne = findViewById(R.id.information_teacher_ongoingTicket_1);
        informationTeacherTwo = findViewById(R.id.information_teacher_ongoingTicket_2);
        textView2 = findViewById(R.id.ongoing_ticket_texview_2);

        //Get data teacher with Panel Teacher
        nameTeacher = getIntent().getStringExtra("nameTeacher");
        surnameTeacher = getIntent().getStringExtra("surnameTeacher");
        facultyTeacher = getIntent().getStringExtra("facultyTeacher");
        fieldTeacher = getIntent().getStringExtra("fieldTeacher");

        //Search for tickets that are specific to a given teacher.
        firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("Accepted Applications");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        CloudFireOngoingTicket cloudFireOngoingTicket = new CloudFireOngoingTicket(
                                document.getString("nameTeacher"),
                                document.getString("surnameTeacher"),
                                document.getString("facultyTeacher"),
                                document.getString("fieldTeacher"),
                                document.getString("nameTeacher2"),
                                document.getString("surnameTeacher2"),
                                document.getString("facultyTeacher2"),
                                document.getString("fieldTeacher2"),
                                document.getString("informationTeacher"),
                                document.getString("informationTeacher2"));
                        objectArrayList.add(cloudFireOngoingTicket);
                    }
                }

                //If the list of applications is late -
                //display a banner about no applications with a button to return to Panel Teacher
                if (objectArrayList.size() == 0) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OngoingTicket.this);
                    dialogBuilder.setTitle("Ongoing applications");
                    dialogBuilder.setMessage("No tickets to display!");
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setNeutralButton("Back to Panel", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(OngoingTicket.this, PanelTeacher.class);
                            intent.putExtra("Email", getIntent().getStringExtra("Email"));
                            startActivity(intent);
                        }
                    });
                    dialogBuilder.create();
                    dialogBuilder.show();
                }
                //If the list of tickets has one ticket - display it on the application's desktop
                if (objectArrayList.size() == 1) {
                    CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(0);
                    String nameTeacher = cloudFireOngoingTicket.nameTeacher;
                    String surnameTeacher = cloudFireOngoingTicket.surnameTeacher;
                    String facultyTeacher = cloudFireOngoingTicket.facultyTeacher;
                    String fieldTeacher = cloudFireOngoingTicket.fieldTeacher;

                    String nameTeacher2 = cloudFireOngoingTicket.nameTeacher2;
                    String surnameTeacher2 = cloudFireOngoingTicket.surnameTeacher2;
                    String facultyTeacher2 = cloudFireOngoingTicket.facultyTeacher2;
                    String fieldTeacher2 = cloudFireOngoingTicket.fieldTeacher2;


                    if (nameTeacher.equals(OngoingTicket.this.nameTeacher) &&
                            surnameTeacher.equals(OngoingTicket.this.surnameTeacher) &&
                            facultyTeacher.equals(OngoingTicket.this.facultyTeacher) &&
                            fieldTeacher.equals(OngoingTicket.this.fieldTeacher)
                    ) {
                        secondTeacherAddButton.setVisibility(VISIBLE);
                        informationTeacherOne.setVisibility(VISIBLE);
                        textView2.setVisibility(VISIBLE);
                    }
                    //if (nameTeacher2.equals(OngoingTicket.this.nameTeacher) &&
                    //        surnameTeacher2.equals(OngoingTicket.this.surnameTeacher) &&
                    //        facultyTeacher2.equals(OngoingTicket.this.facultyTeacher) &&
                    //        fieldTeacher2.equals(OngoingTicket.this.fieldTeacher))


                    else {
                        informationTeacherTwo.setVisibility(VISIBLE);
                        textView2.setVisibility(VISIBLE);
                    }
                }

                if (objectArrayList.size() > 1) {
                    nextButton.setVisibility(VISIBLE);
                    CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(0);
                    String nameTeacher = cloudFireOngoingTicket.nameTeacher;
                    String surnameTeacher = cloudFireOngoingTicket.surnameTeacher;
                    String facultyTeacher = cloudFireOngoingTicket.facultyTeacher;
                    String fieldTeacher = cloudFireOngoingTicket.fieldTeacher;

                    String nameTeacher2 = cloudFireOngoingTicket.nameTeacher2;
                    String surnameTeacher2 = cloudFireOngoingTicket.surnameTeacher2;
                    String facultyTeacher2 = cloudFireOngoingTicket.facultyTeacher2;
                    String fieldTeacher2 = cloudFireOngoingTicket.fieldTeacher2;

                    if (nameTeacher.equals(OngoingTicket.this.nameTeacher) &&
                            surnameTeacher.equals(OngoingTicket.this.surnameTeacher) &&
                            facultyTeacher.equals(OngoingTicket.this.facultyTeacher) &&
                            fieldTeacher.equals(OngoingTicket.this.fieldTeacher)) {
                        secondTeacherAddButton.setVisibility(VISIBLE);
                        informationTeacherOne.setVisibility(VISIBLE);
                        textView2.setVisibility(VISIBLE);
                    }
                    if (nameTeacher2.equals(OngoingTicket.this.nameTeacher) &&
                            surnameTeacher2.equals(OngoingTicket.this.surnameTeacher) &&
                            facultyTeacher2.equals(OngoingTicket.this.facultyTeacher) &&
                            fieldTeacher2.equals(OngoingTicket.this.fieldTeacher)) {
                        informationTeacherTwo.setVisibility(VISIBLE);
                        textView2.setVisibility(VISIBLE);
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
                    CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(indexTicket);
                    String nameTeacher = cloudFireOngoingTicket.nameTeacher;
                    String surnameTeacher = cloudFireOngoingTicket.surnameTeacher;
                    String facultyTeacher = cloudFireOngoingTicket.facultyTeacher;
                    String fieldTeacher = cloudFireOngoingTicket.fieldTeacher;

                    String nameTeacher2 = cloudFireOngoingTicket.nameTeacher2;
                    String surnameTeacher2 = cloudFireOngoingTicket.surnameTeacher2;
                    String facultyTeacher2 = cloudFireOngoingTicket.facultyTeacher2;
                    String fieldTeacher2 = cloudFireOngoingTicket.fieldTeacher2;

                    if (nameTeacher.equals(OngoingTicket.this.nameTeacher) &&
                            surnameTeacher.equals(OngoingTicket.this.surnameTeacher) &&
                            facultyTeacher.equals(OngoingTicket.this.facultyTeacher) &&
                            fieldTeacher.equals(OngoingTicket.this.fieldTeacher)) {
                        secondTeacherAddButton.setVisibility(VISIBLE);
                        informationTeacherOne.setVisibility(VISIBLE);
                        textView2.setVisibility(VISIBLE);
                    }
                    if (nameTeacher2.equals(OngoingTicket.this.nameTeacher) &&
                            surnameTeacher2.equals(OngoingTicket.this.surnameTeacher) &&
                            facultyTeacher2.equals(OngoingTicket.this.facultyTeacher) &&
                            fieldTeacher2.equals(OngoingTicket.this.fieldTeacher)) {
                        informationTeacherTwo.setVisibility(VISIBLE);
                        textView2.setVisibility(VISIBLE);
                    }

                }


                if (indexTicket < sizeList) {
                    CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(indexTicket);
                    String nameTeacher = cloudFireOngoingTicket.nameTeacher;
                    String surnameTeacher = cloudFireOngoingTicket.surnameTeacher;
                    String facultyTeacher = cloudFireOngoingTicket.facultyTeacher;
                    String fieldTeacher = cloudFireOngoingTicket.fieldTeacher;

                    String nameTeacher2 = cloudFireOngoingTicket.nameTeacher2;
                    String surnameTeacher2 = cloudFireOngoingTicket.surnameTeacher2;
                    String facultyTeacher2 = cloudFireOngoingTicket.facultyTeacher2;
                    String fieldTeacher2 = cloudFireOngoingTicket.fieldTeacher2;

                    if (nameTeacher.equals(OngoingTicket.this.nameTeacher) &&
                            surnameTeacher.equals(OngoingTicket.this.surnameTeacher) &&
                            facultyTeacher.equals(OngoingTicket.this.facultyTeacher) &&
                            fieldTeacher.equals(OngoingTicket.this.fieldTeacher)) {
                        secondTeacherAddButton.setVisibility(VISIBLE);
                        informationTeacherOne.setVisibility(VISIBLE);
                        textView2.setVisibility(VISIBLE);
                    }
                    if (nameTeacher2.equals(OngoingTicket.this.nameTeacher) &&
                            surnameTeacher2.equals(OngoingTicket.this.surnameTeacher) &&
                            facultyTeacher2.equals(OngoingTicket.this.facultyTeacher) &&
                            fieldTeacher2.equals(OngoingTicket.this.fieldTeacher)) {
                        informationTeacherTwo.setVisibility(VISIBLE);
                        textView2.setVisibility(VISIBLE);
                    }

                } else {
                    nextButton.setVisibility(View.GONE);
                }
            }
        });

        //If click a previous button
        //Go to the previous request and set the parameters on the display.
        //If we get to the first notification - turn off the previous button
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexTicket--;

                if (indexTicket == 0) {
                    previousButton.setVisibility(View.GONE);
                    CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(indexTicket);
                    String nameTeacher = cloudFireOngoingTicket.nameTeacher;
                    String surnameTeacher = cloudFireOngoingTicket.surnameTeacher;
                    String facultyTeacher = cloudFireOngoingTicket.facultyTeacher;
                    String fieldTeacher = cloudFireOngoingTicket.fieldTeacher;

                    String nameTeacher2 = cloudFireOngoingTicket.nameTeacher2;
                    String surnameTeacher2 = cloudFireOngoingTicket.surnameTeacher2;
                    String facultyTeacher2 = cloudFireOngoingTicket.facultyTeacher2;
                    String fieldTeacher2 = cloudFireOngoingTicket.fieldTeacher2;

                    if (nameTeacher.equals(OngoingTicket.this.nameTeacher) &&
                            surnameTeacher.equals(OngoingTicket.this.surnameTeacher) &&
                            facultyTeacher.equals(OngoingTicket.this.facultyTeacher) &&
                            fieldTeacher.equals(OngoingTicket.this.fieldTeacher)) {
                        secondTeacherAddButton.setVisibility(VISIBLE);
                        informationTeacherOne.setVisibility(VISIBLE);
                        textView2.setVisibility(VISIBLE);
                    }
                    if (nameTeacher2.equals(OngoingTicket.this.nameTeacher) &&
                            surnameTeacher2.equals(OngoingTicket.this.surnameTeacher) &&
                            facultyTeacher2.equals(OngoingTicket.this.facultyTeacher) &&
                            fieldTeacher2.equals(OngoingTicket.this.fieldTeacher)) {
                        informationTeacherTwo.setVisibility(VISIBLE);
                        textView2.setVisibility(VISIBLE);
                    }

                    nextButton.setVisibility(VISIBLE);
                }

                if (indexTicket > 0) {
                    CloudFireOngoingTicket cloudFireOngoingTicket = objectArrayList.get(indexTicket);
                    String nameTeacher = cloudFireOngoingTicket.nameTeacher;
                    String surnameTeacher = cloudFireOngoingTicket.surnameTeacher;
                    String facultyTeacher = cloudFireOngoingTicket.facultyTeacher;
                    String fieldTeacher = cloudFireOngoingTicket.fieldTeacher;

                    String nameTeacher2 = cloudFireOngoingTicket.nameTeacher2;
                    String surnameTeacher2 = cloudFireOngoingTicket.surnameTeacher2;
                    String facultyTeacher2 = cloudFireOngoingTicket.facultyTeacher2;
                    String fieldTeacher2 = cloudFireOngoingTicket.fieldTeacher2;

                    if (nameTeacher.equals(OngoingTicket.this.nameTeacher) &&
                            surnameTeacher.equals(OngoingTicket.this.surnameTeacher) &&
                            facultyTeacher.equals(OngoingTicket.this.facultyTeacher) &&
                            fieldTeacher.equals(OngoingTicket.this.fieldTeacher)) {
                        secondTeacherAddButton.setVisibility(VISIBLE);
                        informationTeacherOne.setVisibility(VISIBLE);
                        textView2.setVisibility(VISIBLE);
                    }
                    if (nameTeacher2.equals(OngoingTicket.this.nameTeacher) &&
                            surnameTeacher2.equals(OngoingTicket.this.surnameTeacher) &&
                            facultyTeacher2.equals(OngoingTicket.this.facultyTeacher) &&
                            fieldTeacher2.equals(OngoingTicket.this.fieldTeacher)) {
                        informationTeacherTwo.setVisibility(VISIBLE);
                        textView2.setVisibility(VISIBLE);
                    }

                    nextButton.setVisibility(VISIBLE);
                } else {
                    previousButton.setVisibility(View.GONE);
                }
            }
        });


    }
}

class CloudFireOngoingTicket {
    String nameTeacher = " ";
    String surnameTeacher = " ";
    String facultyTeacher = " ";
    String fieldTeacher = " ";
    String nameTeacher2 = " ";
    String surnameTeacher2 = " ";
    String facultyTeacher2 = " ";
    String fieldTeacher2 = " ";
    String informationTeacher1 = " ";
    String informationTeacher2 = " ";

    public CloudFireOngoingTicket(String nameTeacher, String surnameTeacher, String facultyTeacher, String fieldTeacher, String nameTeacher2, String surnameTeacher2, String facultyTeacher2,
                                  String fieldTeacher2, String informationTeacher1, String informationTeacher2) {
        this.nameTeacher = nameTeacher;
        this.surnameTeacher = surnameTeacher;
        this.facultyTeacher = facultyTeacher;
        this.fieldTeacher = fieldTeacher;
        this.nameTeacher2 = nameTeacher2;
        this.surnameTeacher2 = surnameTeacher2;
        this.facultyTeacher2 = facultyTeacher2;
        this.fieldTeacher2 = fieldTeacher2;
        this.informationTeacher1 = informationTeacher1;
        this.informationTeacher2 = informationTeacher2;
    }
}