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

public class PanelStudent extends AppCompatActivity {

    TextView welcomeText, verifyMessage;
    Button addNewTicketButton, confirmTicketsButton, logoutButton, pendingTicketsButton, rejectTicketsButton, resendCodeButton;
    String nameStudent, surnameStudent, facultyStudent, fieldStudent, degreeStudent, semesterStudent, indexNumberStudent, emailStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_student);
        addNewTicketButton = findViewById(R.id.addNewTicket_panelStudent);
        confirmTicketsButton = findViewById(R.id.confirmTicket_panelStudent);
        logoutButton = findViewById(R.id.logoutButton_panelStudent);
        resendCodeButton = findViewById(R.id.verifyButton_panelStudent);
        welcomeText = findViewById(R.id.textview_panelStudent);
        verifyMessage = findViewById(R.id.verifyText_panelStudent);
        pendingTicketsButton = findViewById(R.id.pendingTickets_panelStudent);
        rejectTicketsButton = findViewById(R.id.rejectTickets_panelStudent);

        emailStudent = getIntent().getStringExtra("Email");

        //Searching data in collection "User Accounts" in document name UID student user
        DocumentReference documentStudent = FirebaseFirestore.getInstance().collection("Users Accounts").document(emailStudent);
        documentStudent.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshot != null;

                nameStudent = documentSnapshot.getString("Name");
                surnameStudent = documentSnapshot.getString("Surname");
                facultyStudent = documentSnapshot.getString("Faculty");
                fieldStudent = documentSnapshot.getString("Field");
                degreeStudent = documentSnapshot.getString("Degree");
                semesterStudent = documentSnapshot.getString("Semester");
                indexNumberStudent = documentSnapshot.getString("IndexNumber");
                welcomeText.setText("Hello " + nameStudent + " " + surnameStudent);
            }
        });

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;

        if (!user.isEmailVerified()) {
            resendCodeButton.setVisibility(View.VISIBLE);
            verifyMessage.setVisibility(View.VISIBLE);
            resendCodeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(PanelStudent.this, "A new verification e-mail has been sent", Toast.LENGTH_SHORT).show();
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

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PanelStudent.this, "Sign Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PanelStudent.this, MainActivity.class));
            }
        });

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
                intent.putExtra("Email", emailStudent);
                startActivity(intent);
            }
        });

        confirmTicketsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PanelStudent.this, ConfirmTickets.class);
                intent.putExtra("nameStudent", nameStudent);
                intent.putExtra("surnameStudent", surnameStudent);
                intent.putExtra("facultyStudent", facultyStudent);
                intent.putExtra("fieldStudent", fieldStudent);
                intent.putExtra("Email", emailStudent);
                startActivity(intent);
            }
        });

        pendingTicketsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PanelStudent.this, PendingTickets.class);
                intent.putExtra("nameStudent", nameStudent);
                intent.putExtra("surnameStudent", surnameStudent);
                intent.putExtra("facultyStudent", facultyStudent);
                intent.putExtra("fieldStudent", fieldStudent);
                intent.putExtra("Email", emailStudent);
                startActivity(intent);
            }
        });

        rejectTicketsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PanelStudent.this, RejectTickets.class);
                intent.putExtra("nameStudent", nameStudent);
                intent.putExtra("surnameStudent", surnameStudent);
                intent.putExtra("facultyStudent", facultyStudent);
                intent.putExtra("fieldStudent", fieldStudent);
                intent.putExtra("Email", emailStudent);
                startActivity(intent);
            }
        });
    }
}