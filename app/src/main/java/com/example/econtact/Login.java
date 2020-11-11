package com.example.econtact;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class Login extends AppCompatActivity {
    EditText loginUser, passwordUser;
    TextView forgotTextLink;
    Button loginButton;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID, userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get ID for EditTexts, TextView and Button
        loginUser = findViewById(R.id.email_Login);
        passwordUser = findViewById(R.id.password_Login);
        forgotTextLink = findViewById(R.id.forgotPassword_Login);
        loginButton = findViewById(R.id.loginButton_Login);
        progressBar = findViewById(R.id.bar_Login);

        //Save user ID and check in collection "Users Accounts" data user
        //Save type user in variable: userStatus
        /*userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentStudent = firebaseFirestore.collection("Users Accounts").document(userID);
        documentStudent.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshot != null;
                userStatus = documentSnapshot.getString("User");
            }
        });
         */

        //When Button is clicked
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save email and password from user
                String emailUserString = loginUser.getText().toString();
                String passwordUserString = passwordUser.getText().toString();

                //Check pole email
                if (emailUserString.isEmpty()) {
                    loginUser.setError("Email is empty!");
                    return;
                }

                //Check pole password
                if (passwordUserString.isEmpty()) {
                    passwordUser.setError("Password is empty!");
                    return;
                }

                //Get Instance for Firebase Authentication and Firebase Firestore
                firebaseAuth = FirebaseAuth.getInstance();

                //Sign in Firebase user
                firebaseAuth.signInWithEmailAndPassword(emailUserString, passwordUserString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DocumentReference documentStudent = FirebaseFirestore.getInstance()
                                    .collection("Users Accounts").document(firebaseAuth.getCurrentUser().getUid());
                            documentStudent.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                    assert documentSnapshot != null;
                                    userStatus = documentSnapshot.getString("User");
                                }
                            });

                            final String studentVal = "Student";
                            final String teacherVal = "Teacher";
                            //progressBar.setVisibility(View.INVISIBLE);

                            String val = String.valueOf(userStatus);
                            //If loggin in is student - go to Panel Student activity
                            if (val.equals(studentVal)) {
                                Toast.makeText(Login.this, "Login success!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, PanelStudent.class));
                            }
                            //If loggin in is teacher - go to Panel Student activity
                            if (val.equals(teacherVal)) {
                                Toast.makeText(Login.this, "Login success!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, PanelTeacher.class));
                            }

                        }
                        //If user isn't in Database - show information about it
                        else {
                            Toast.makeText(Login.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            //progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });

        //If user forget your password, click a TextView "Forgot password?"
        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create Edit Text to writing your email
                //Create AlertDialog to send password reset to your email register
                final EditText resetMail = new EditText(view.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your mail to received reset link: ");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail = resetMail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset link send to your email!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error! Reset link is not send" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                passwordResetDialog.create().show();
            }
        });
    }
}