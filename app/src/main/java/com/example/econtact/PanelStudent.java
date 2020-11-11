package com.example.econtact;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class PanelStudent extends AppCompatActivity {

    TextView welcomeText, verifyMessage;
    Button addNewTicketButton, confirmTicketsButton, logoutButton, pendingTicketsButton, rejectTicketsButton, resendCodeButton;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String studentID, nameStudent, surnameStudent, facultyStudent, fieldStudent, degreeStudent, semesterStudent, indexNumberStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_student);

        //Get ID for objects in layout: Buttons, TextViews
        addNewTicketButton = findViewById(R.id.addNewTicket_panelStudent);
        confirmTicketsButton = findViewById(R.id.confirmTicket_panelStudent);
        logoutButton = findViewById(R.id.logoutButton_panelStudent);
        resendCodeButton = findViewById(R.id.verifyButton_panelStudent);
        welcomeText = findViewById(R.id.textview_panelStudent);
        verifyMessage = findViewById(R.id.verifyText_panelStudent);
        pendingTicketsButton = findViewById(R.id.pendingTickets_panelStudent);
        rejectTicketsButton = findViewById(R.id.rejectTickets_panelStudent);

        //Get Instance for Firebase Authentication and Firebase Firestore
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Get of student user data regading his name and surname
        //Set his data to top banner
        studentID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        //Searching data in collection "User Accounts" in document name UID student user
        DocumentReference documentStudent = firebaseFirestore.collection("Users Accounts").document(studentID);
        documentStudent.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                //Protection if UID document haven't data
                assert documentSnapshot != null;

                //Ascription data user to local variable
                nameStudent = documentSnapshot.getString("Name");
                surnameStudent = documentSnapshot.getString("Surname");
                facultyStudent = documentSnapshot.getString("Faculty");
                fieldStudent = documentSnapshot.getString("Field");
                degreeStudent = documentSnapshot.getString("Degree");
                semesterStudent = documentSnapshot.getString("Semester");
                indexNumberStudent = documentSnapshot.getString("IndexNumber");

                //Save name and surname to top textview
                welcomeText.setText("Hello " + nameStudent + " " + surnameStudent);
            }
        });

        //Action about user not verify a email
        //Checking, if user verify a email
        //If user not verify a your email:
        //1.Set visible TextView verifyText and Button verifyButton
        //2.If user click a Button, new verify email will send to his mail
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        //Protection - if user doest not exist
        assert user != null;
        //If user not verify your email
        if (!user.isEmailVerified()) {
            //Show button and textview
            resendCodeButton.setVisibility(View.VISIBLE);
            verifyMessage.setVisibility(View.VISIBLE);
            resendCodeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(PanelStudent.this, "A new verification e-mail has been sent - check your e-mail", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "Error: " + e.getMessage());
                        }
                    });
                }
            });
        }

        //Activity - user logout
        //Pass to activity - MainActivity
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PanelStudent.this, "Sign Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PanelStudent.this, MainActivity.class));
            }
        });

        //Activity - Add new ticket from student user to specific teacher user
        //Subsequent handover data user to next Activity
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
                startActivity(intent);
            }
        });

        //Go to Confirm Tickets Activity
        //Transmission student data to new activity
        confirmTicketsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PanelStudent.this, ConfirmTickets.class);
                intent.putExtra("nameStudent", nameStudent);
                intent.putExtra("surnameStudent", surnameStudent);
                intent.putExtra("facultyStudent", facultyStudent);
                intent.putExtra("fieldStudent", fieldStudent);
                startActivity(intent);
            }
        });

        //Go to Pending Tickets Activity
        //Transmission student data to new activity
        pendingTicketsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PanelStudent.this, PendingTickets.class);
                intent.putExtra("nameStudent", nameStudent);
                intent.putExtra("surnameStudent", surnameStudent);
                intent.putExtra("facultyStudent", facultyStudent);
                intent.putExtra("fieldStudent", fieldStudent);
                startActivity(intent);
            }
        });

        //Go to Reject Tickets Activity
        //Transmission student data to new activity
        rejectTicketsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PanelStudent.this, RejectTickets.class);
                intent.putExtra("nameStudent", nameStudent);
                intent.putExtra("surnameStudent", surnameStudent);
                intent.putExtra("facultyStudent", facultyStudent);
                intent.putExtra("fieldStudent", fieldStudent);
                startActivity(intent);
            }
        });
    }
}