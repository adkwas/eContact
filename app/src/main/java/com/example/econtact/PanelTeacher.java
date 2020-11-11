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

public class PanelTeacher extends AppCompatActivity {
    TextView welcomeText, verifyMessageText;
    Button checkNewTicketButton, logoutButton, acceptedTicketsButton, resendCodeButton;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String teacherID, nameTeacher, surnameTeacher, facultyTeacher, fieldTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_teacher);

        //Get ID for TextViews, Buttons
        checkNewTicketButton = findViewById(R.id.checkNewTicket_panelTeacher);
        welcomeText = findViewById(R.id.textview_panelTeacher);
        logoutButton = findViewById(R.id.logoutButton_panelTeacher);
        resendCodeButton = findViewById(R.id.verifyButton_panelTeacher);
        verifyMessageText = findViewById(R.id.verifyText_panelTeacher);
        acceptedTicketsButton = findViewById(R.id.acceptedTickets_panelTeacher);

        //Get Instance for FirebaseAuthentication and FirebaseFirestore
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Get data teacher to set welcome TextView
        teacherID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        final DocumentReference documentTeacher = firebaseFirestore.collection("Users Accounts").document(teacherID);
        documentTeacher.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshot != null;
                nameTeacher = documentSnapshot.getString("Name");
                surnameTeacher = documentSnapshot.getString("Surname");
                facultyTeacher = documentSnapshot.getString("Faculty");
                fieldTeacher = documentSnapshot.getString("Field");
                welcomeText.setText("Hello" + " " + nameTeacher + " " + surnameTeacher);
            }
        });

        //Check if user verify your email
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        if (!user.isEmailVerified()) {
            //Show button and textview
            resendCodeButton.setVisibility(View.VISIBLE);
            verifyMessageText.setVisibility(View.VISIBLE);

            resendCodeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(PanelTeacher.this, "Verification Email has been sent - Check an email!", Toast.LENGTH_SHORT).show();
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

        //Sing out user of aplication
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PanelTeacher.this, "Sign Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PanelTeacher.this, MainActivity.class));
            }
        });

        //Go to Accepted Tickets Activity
        //Transmission teacher data
        acceptedTicketsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PanelTeacher.this, AcceptedTickets.class);
                intent.putExtra("nameTeacher", nameTeacher);
                intent.putExtra("surnameTeacher", surnameTeacher);
                intent.putExtra("facultyTeacher", facultyTeacher);
                intent.putExtra("fieldTeacher", fieldTeacher);
                startActivity(intent);
            }
        });

        //Go to Check New Tickets Activity
        //Transmission teacher data
        checkNewTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PanelTeacher.this, CheckNewTicket.class);
                intent.putExtra("nameTeacher", nameTeacher);
                intent.putExtra("surnameTeacher", surnameTeacher);
                intent.putExtra("facultyTeacher", facultyTeacher);
                intent.putExtra("fieldTeacher", fieldTeacher);
                startActivity(intent);
            }
        });
    }
}
