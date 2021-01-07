package com.example.econtact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class AddNewTicketStepOne extends AppCompatActivity {

    Spinner facultySpinner;
    EditText nameTeacher, surnameTeacher;
    Button nextStep, backStep;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String emailStudent;
    String selectedFaculty = "", nameSelected = "", surnameSelected = "", fieldSelected = "", facultySelected = "", emailSelected = "";
    String[] arrayFaculty = new String[]{"Faculty of Architecture", "Faculty of Chemical Technology", "Faculty of Civil and Transport Engineering", "Faculty of Computing and Telecomunications",
            "Faculty of Control, Robotics and Electrical Engineering", "Faculty of Engineering Management", "Faculty of Environmental Engineering and Energy",
            "Faculty of Materials Engineering and Technical Physics", "Faculty of Mechanical Engineering"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_ticket_step_one);

        //Set ID for Strings, Buttons, Spinner and Firebase elements
        facultySpinner = findViewById(R.id.spinnerFaculty_addNewTicketStepOne);
        nameTeacher = findViewById(R.id.nameTeacher_addNewTicketStepOne);
        surnameTeacher = findViewById(R.id.surnameTeacher_addNewTicketStepOne);
        nextStep = findViewById(R.id.nextStep_addNewTicketStepOne);
        backStep = findViewById(R.id.backStep_addNewTicketStepOne);

        emailStudent = getIntent().getStringExtra("Email");

        //Set Adapter for Spinner
        //Use a arrayFaculty
        final ArrayAdapter<String> adapterElements = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayFaculty);
        facultySpinner.setAdapter(adapterElements);
        facultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch ((int) l) {
                    case 0:
                        selectedFaculty = "Faculty of Architecture";
                        break;
                    case 1:
                        selectedFaculty = "Faculty of Chemical Technology";
                        break;
                    case 2:
                        selectedFaculty = "Faculty of Civil and Transport Engineering";
                        break;
                    case 3:
                        selectedFaculty = "Faculty of Computing and Telecomunications";
                        break;
                    case 4:
                        selectedFaculty = "Faculty of Control, Robotics and Electrical Engineering";
                        break;
                    case 5:
                        selectedFaculty = "Faculty of Engineering Management";
                        break;
                    case 6:
                        selectedFaculty = "Faculty of Environmental Engineering and Energy";
                        break;
                    case 7:
                        selectedFaculty = "Faculty of Materials Engineering and Technical Physics";
                        break;
                    case 8:
                        selectedFaculty = "Faculty of Mechanical Engineering";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedFaculty = "Faculty of Architecture";
            }
        });

        //If student want go to next step - click a NextStep Button
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save student data from application
                final String nameLecturerUser = nameTeacher.getText().toString();
                final String surnameLecturerUser = surnameTeacher.getText().toString();

                //Protection variable from empty value
                if (nameLecturerUser.isEmpty()) {
                    nameTeacher.setError("Name teacher is empty!");
                    return;
                }

                if (surnameLecturerUser.isEmpty()) {
                    surnameTeacher.setError("Surname teacher is empty!");
                    return;
                }

                //Get Instance for Firebase Firestore and Authentication
                firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseAuth = FirebaseAuth.getInstance();

                //Download collection of Cloud Firestore
                CollectionReference collectionReference = firebaseFirestore.collection("Users Accounts");

                //Download data from Firebase Cloud Collection "Users Accounts" of onefold document
                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                //Download data user from single document
                                String userCloud = document.getString("User");
                                String nameLecturerCloud = document.getString("Name");
                                String surnameLecturerCloud = document.getString("Surname");
                                String facultyLecturerCloud = document.getString("Faculty");

                                //Protection
                                assert userCloud != null;
                                assert facultyLecturerCloud != null;
                                assert nameLecturerCloud != null;
                                assert surnameLecturerCloud != null;

                                //Check if name, surname, faculty and user type are the same value as value given from user in specific document in collection
                                //If data are same type, save value in local variable:
                                //user, nameLecturerCloud, surnameLecturerCloud and facultyLecturerType
                                if (userCloud.equals("Teacher") && facultyLecturerCloud.equals(selectedFaculty) &&
                                        nameLecturerCloud.equals(nameLecturerUser) && surnameLecturerCloud.equals(surnameLecturerUser)) {
                                    nameSelected = document.getString("Name");
                                    surnameSelected = document.getString("Surname");
                                    facultySelected = document.getString("Faculty");
                                    fieldSelected = document.getString("Field");
                                    emailSelected = document.getString("Email");
                                }
                            }

                            //Check if all variable have a value
                            //If at least one variable no has a value, show toast bar with message
                            if (nameSelected.isEmpty() && surnameSelected.isEmpty() && facultySelected.isEmpty() && fieldSelected.isEmpty() && emailSelected.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "This lecturer is not avaiable in eContact", Toast.LENGTH_SHORT).show();
                            }
                            //If all variable have a value, start next activity -> AddNewTicketStepTwo and transer data to new Activity
                            else {
                                Intent stepTwoActivity = new Intent(AddNewTicketStepOne.this, AddNewTicketStepTwo.class);
                                //Send to next Activity data about Lector
                                stepTwoActivity.putExtra("nameTeacher", nameSelected);
                                stepTwoActivity.putExtra("surnameTeacher", surnameSelected);
                                stepTwoActivity.putExtra("facultyTeacher", facultySelected);
                                stepTwoActivity.putExtra("fieldTeacher", fieldSelected);
                                stepTwoActivity.putExtra("emailTeacher", emailSelected);
                                //Send to next Activity data about student user
                                stepTwoActivity.putExtra("nameStudent", getIntent().getStringExtra("nameStudent"));
                                stepTwoActivity.putExtra("surnameStudent", getIntent().getStringExtra("surnameStudent"));
                                stepTwoActivity.putExtra("facultyStudent", getIntent().getStringExtra("facultyStudent"));
                                stepTwoActivity.putExtra("fieldStudent", getIntent().getStringExtra("fieldStudent"));
                                stepTwoActivity.putExtra("degreeStudent", getIntent().getStringExtra("degreeStudent"));
                                stepTwoActivity.putExtra("semesterStudent", getIntent().getStringExtra("semesterStudent"));
                                stepTwoActivity.putExtra("indexNumberStudent", getIntent().getStringExtra("indexNumberStudent"));
                                stepTwoActivity.putExtra("Email", getIntent().getStringExtra("Email"));
                                //Start next Activity -> AddNewTicketStepTwo
                                startActivity(stepTwoActivity);
                            }
                        }
                    }
                });
            }
        });

        //Back to previous activity - Student Panel Activity
        backStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = (new Intent(AddNewTicketStepOne.this, PanelStudent.class));
                intent.putExtra("Email", emailStudent);
                startActivity(intent);
            }
        });
    }
}