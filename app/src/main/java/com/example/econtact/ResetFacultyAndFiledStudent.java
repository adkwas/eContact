package com.example.econtact;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class ResetFacultyAndFiledStudent extends AppCompatActivity {

    Spinner resetFaculty, resetField;

    FirebaseFirestore firebaseFirestore;
    Button reset, back;

    String emailStudent, nameStudent, surnameStudent, degreeStudent, semesterStudent, indexNumberStudent;

    String studentFaculty, studentField;

    String[] faculty = {"Faculty of Study", "Faculty of Architecture", "Faculty of Chemical Technology", "Faculty of Civil and Transport Engineering", "Faculty of Computing and Telecomunications", "Faculty of Control, Robotics and Electrical Engineering", "Faculty of Engineering Management", "Faculty of Environmental Engineering and Energy", "Faculty of Materials Engineering and Technical Physics", "Faculty of Mechanical Engineering"};
    String[] architectureField = {"Field of Study", "Architecture", "Interior Design"};
    String[] automaticsField = {"Field of Study", "Automatic Control and Robotics", "Electrical Engineering", "Mathematics in Technology"};
    String[] telecomsField = {"Field of Study", "Computing", "Bioinformatics", "Artifical Inteligence", "Electronics and Telecommunications", "Information and Communication Technologies"};
    String[] transportField = {"Field of Study", "Civil Engineering", "Sustainable Building Engineering", "Mechanical Engineering", "Aerospace engineering", "Transport"};
    String[] physicsField = {"Field of Study", "Education in Technology and Informatics", "Technical Physics", "Materials Engineering"};
    String[] mechanicsField = {"Field of Study", "Biomedical Engineering", "Mechanical Engineering", "Mechatronics", "Manangement and Production Engineering"};
    String[] energyField = {"Field of Study", "Environmental Engineering", "Power Engineering", "Industrial and Renewable Energy", "Aviation Engineering"};
    String[] managmentField = {"Field of Study", "Logistics", "Engineering", "Management", "Safety Engineering"};
    String[] chemicsField = {"Field of Study", "Chemical and Process Engineering", "Chemical Technology", "Environmental Protection Technology", "Pharmaceutical Engineering"};
    String[] defaultField = {"Field of Study"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_faculty_and_filed_student);

        resetFaculty = findViewById(R.id.spinnerResetFacultyStudent_ResetFacultyAndFieldStudent);
        resetField = findViewById(R.id.resetField_ResetFacultyAndFieldStudent);
        reset = findViewById(R.id.reset_ResetFacultyAndFieldStudent);
        back = findViewById(R.id.back_ResetFacultyAndFieldStudent);

        emailStudent = getIntent().getStringExtra("Email");

        firebaseFirestore = FirebaseFirestore.getInstance();

        final DocumentReference documentStudent = firebaseFirestore.collection("Users Accounts").document(emailStudent);
        documentStudent.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshot != null;

                nameStudent = documentSnapshot.getString("Name");
                surnameStudent = documentSnapshot.getString("Surname");
                degreeStudent = documentSnapshot.getString("Degree");
                semesterStudent = documentSnapshot.getString("Semester");
                indexNumberStudent = documentSnapshot.getString("IndexNumber");
            }
        });

        final ArrayAdapter<String> adapterArchitecture = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, architectureField);
        final ArrayAdapter<String> adapterAutomatics = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, automaticsField);
        final ArrayAdapter<String> adapterTelecom = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, telecomsField);
        final ArrayAdapter<String> adapterTransport = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, transportField);
        final ArrayAdapter<String> adapterPhysics = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, physicsField);
        final ArrayAdapter<String> adapterMechanics = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mechanicsField);
        final ArrayAdapter<String> adapterEnergy = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, energyField);
        final ArrayAdapter<String> adapterManagment = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, managmentField);
        final ArrayAdapter<String> adapterChemics = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, chemicsField);
        final ArrayAdapter<String> adapterDefault = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, defaultField);
        final ArrayAdapter<String> adapterFaculty = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, faculty);

        //Set faculty whatever the student chooses
        resetFaculty.setAdapter(adapterFaculty);
        resetFaculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch ((int) l) {
                    case 0:
                        studentFaculty = "Faculty of Study";
                        resetField.setAdapter(adapterDefault);
                        break;
                    case 1:
                        studentFaculty = "Faculty of Architecture";
                        resetField.setAdapter(adapterArchitecture);
                        resetField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        studentField = "Field of Study";
                                        break;
                                    case 1:
                                        studentField = "Architecture";
                                        break;
                                    case 2:
                                        studentField = "Interior Design";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                studentField = "Field of Study";
                            }
                        });
                        break;
                    case 2:
                        studentFaculty = "Faculty of Chemical Technology";
                        resetField.setAdapter(adapterChemics);
                        resetField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        studentField = "Field of Study";
                                        break;
                                    case 1:
                                        studentField = "Chemical and Process Engineering";
                                        break;
                                    case 2:
                                        studentField = "Chemical Technology";
                                        break;
                                    case 3:
                                        studentField = "Environmental protection technology";
                                        break;
                                    case 4:
                                        studentField = "Pharmaceutical Engineering";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                studentField = "Field of Study";
                            }
                        });
                        break;
                    case 3:
                        studentFaculty = "Faculty of Civil and Transport Engineering";
                        resetField.setAdapter(adapterTransport);
                        resetField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        studentField = "Field of Study";
                                        break;
                                    case 1:
                                        studentField = "Civil Engineering";
                                        break;
                                    case 2:
                                        studentField = "Sustainable building engineering";
                                        break;
                                    case 3:
                                        studentField = "Mechanical Engineering";
                                        break;
                                    case 4:
                                        studentField = "Aerospace engineering";
                                        break;
                                    case 5:
                                        studentField = "Transport";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                studentField = "Field of Study";
                            }
                        });
                        break;
                    case 4:
                        studentFaculty = "Faculty of Computing and Telecomunications";
                        resetField.setAdapter(adapterTelecom);
                        resetField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        studentField = "Field of Study";
                                        break;
                                    case 1:
                                        studentField = "Computing";
                                        break;
                                    case 2:
                                        studentField = "Bioinformatics";
                                        break;
                                    case 3:
                                        studentField = "Artifical Inteligence";
                                        break;
                                    case 4:
                                        studentField = "Electronics and Telecommunications";
                                        break;
                                    case 5:
                                        studentField = "Information and Communication technologies";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                studentField = "Field of Study";
                            }
                        });
                        break;
                    case 5:
                        studentFaculty = "Faculty of Control, Robotics and Electrical Engineering";
                        resetField.setAdapter(adapterAutomatics);
                        resetField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        studentField = "Field of Study";
                                        break;
                                    case 1:
                                        studentField = "Automatic Control and Robotics";
                                        break;
                                    case 2:
                                        studentField = "Electrical Engineering";
                                        break;
                                    case 3:
                                        studentField = "Mathematics in Technology";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                studentField = "Field of Study";
                            }
                        });
                        break;
                    case 6:
                        studentFaculty = "Faculty of Engineering Management";
                        resetField.setAdapter(adapterManagment);
                        resetField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        studentField = "Field of Study";
                                        break;
                                    case 1:
                                        studentField = "Logistics";
                                        break;
                                    case 2:
                                        studentField = "Engineering";
                                        break;
                                    case 3:
                                        studentField = "Management";
                                        break;
                                    case 4:
                                        studentField = "Safety Engineering";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                studentField = "Field of Study";
                            }
                        });
                        break;
                    case 7:
                        studentFaculty = "Faculty of Environmental Engineering and Energy";
                        resetField.setAdapter(adapterEnergy);
                        resetField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        studentField = "Field of Study";
                                        break;
                                    case 1:
                                        studentField = "Environmental Engineering";
                                        break;
                                    case 2:
                                        studentField = "Power Engineering";
                                        break;
                                    case 3:
                                        studentField = "Industrial and renewable energy";
                                        break;
                                    case 4:
                                        studentField = "Aviation engineering";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                studentField = "Field of Study";
                            }
                        });
                        break;
                    case 8:
                        studentFaculty = "Faculty of Materials Engineering and Technical Physics";
                        resetField.setAdapter(adapterPhysics);
                        resetField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        studentField = "Field of Study";
                                        break;
                                    case 1:
                                        studentField = "Education in Technology nad informatics";
                                        break;
                                    case 2:
                                        studentField = "Technical physics";
                                        break;
                                    case 3:
                                        studentField = "Materials Engineering";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                studentField = "Field of Study";
                            }
                        });
                        break;
                    case 9:
                        studentFaculty = "Faculty of Mechanical Engineering";
                        resetField.setAdapter(adapterMechanics);
                        resetField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        studentField = "Field of Study";
                                        break;
                                    case 1:
                                        studentField = "Biomedical engineering";
                                        break;
                                    case 2:
                                        studentField = "Mechanical Engineering";
                                        break;
                                    case 3:
                                        studentField = "Mechatronics";
                                        break;
                                    case 4:
                                        studentField = "Management and Production Engineering";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                studentFaculty = "Faculty of Study";
                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                studentFaculty = "Faculty of Study";
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String facultyString = studentFaculty;
                final String fieldString = studentField;

                final String checkFaculty = "Faculty of Study";
                final String checkField = "Field of Study";

                if (facultyString.equals(checkFaculty)) {
                    Toast.makeText(getApplicationContext(), "Please check a faculty pole!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (fieldString.equals(checkField)) {
                    Toast.makeText(getApplicationContext(), "Please check a field pole!", Toast.LENGTH_SHORT).show();
                    return;
                }

                DocumentReference documentReference = firebaseFirestore.collection("Users Accounts").document(emailStudent);
                Map<String, Object> user = new HashMap<>();
                user.put("Name", nameStudent);
                user.put("Surname", surnameStudent);
                user.put("Faculty", facultyString);
                user.put("Field", fieldString);
                user.put("Semester", semesterStudent);
                user.put("Degree", degreeStudent);
                user.put("Email", emailStudent);
                user.put("IndexNumber", indexNumberStudent);
                user.put("User", "Student");
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(Void aVoid) {
                        NotificationChannel channel = new NotificationChannel("channel01", "name",
                                NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                        channel.setDescription("description");

                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);

                        Notification notification = new NotificationCompat.Builder(ResetFacultyAndFiledStudent.this, "channel01")
                                .setSmallIcon(android.R.drawable.ic_dialog_info)
                                .setContentTitle("eContact")
                                .setContentText("Faculty and Field changed!")
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

                        Notification notification = new NotificationCompat.Builder(ResetFacultyAndFiledStudent.this, "channel01")
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
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetFacultyAndFiledStudent.this, ResetDataStudent.class);
                intent.putExtra("Email", emailStudent);
                startActivity(intent);
            }
        });


    }
}