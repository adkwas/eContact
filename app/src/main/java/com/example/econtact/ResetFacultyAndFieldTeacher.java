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

public class ResetFacultyAndFieldTeacher extends AppCompatActivity {

    Spinner facultyUniversity, fieldUniversity;
    Button reset, back;

    String[] elementsFaculty = {"Faculty of Study", "Faculty of Architecture", "Faculty of Chemical Technology", "Faculty of Civil and Transport Engineering", "Faculty of Computing and Telecomunications", "Faculty of Control, Robotics and Electrical Engineering", "Faculty of Engineering Management", "Faculty of Environmental Engineering and Energy", "Faculty of Materials Engineering and Technical Physics", "Faculty of Mechanical Engineering"};
    String[] architectureField = {"Field of Study", "Architecture", "Interior Design"};
    String[] automaticsField = {"Field of Study", "Automatic Control and Robotics", "Electrical Engineering", "Mathematics in Technology"};
    String[] telecomsField = {"Field of Study", "Computing", "Bioinformatics", "Artifical Inteligence", "Electronics and Telecommunications", "Information and Communication Technologies"};
    String[] transportField = {"Field of Study", "Civil Engineering", "Sustainable Building Engineering", "Mechanical Engineering", "Aerospace engineering", "Transport"};
    String[] physicsField = {"Field of Study", "Education in Technology and Informatics", "Technical Physics", "Materials Engineering"};
    String[] mechanicsField = {"Field of Study", "Biomedical Engineering", "Mechanical Engineering", "Mechatronics", "Manangement and Production Engineering"};
    String[] energyField = {"Field of Study", "Environmental Engineering", "Power Engineering", "Industrial and Renewable Energy", "Aviation Engineering"};
    String[] managementField = {"Field of Study", "Logistics", "Engineering", "Management", "Safety Engineering"};
    String[] chemistField = {"Field of Study", "Chemical and Process Engineering", "Chemical Technology", "Environmental Protection Technology", "Pharmaceutical Engineering"};
    String[] defaultField = {"Field of Study"};

    String teacherFaculty, teacherField, emailString;
    String nameTeacher, surnameTeacher, emailTeacher, userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_faculty_and_field_teacher);

        facultyUniversity = findViewById(R.id.spinnerResetFaculty_ResetFacultyAndFieldTeacher);
        fieldUniversity = findViewById(R.id.spinnerField_ResetFacultyAndFieldTeacher);
        reset = findViewById(R.id.reset_ResetFacultyAndFieldTeacher);
        back = findViewById(R.id.back_ResetFacultyAndFieldTeacher);

        emailString = getIntent().getStringExtra("Email");

        final DocumentReference documentTeacher = FirebaseFirestore.getInstance().collection("Users Accounts").document(emailString);
        documentTeacher.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshot != null;

                nameTeacher = documentSnapshot.getString("Name");
                surnameTeacher = documentSnapshot.getString("Surname");
                emailTeacher = documentSnapshot.getString("Email");
                userType = documentSnapshot.getString("User");
            }
        });

        final ArrayAdapter<String> adapterArchitecture = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, architectureField);
        final ArrayAdapter<String> adapterAutomatics = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, automaticsField);
        final ArrayAdapter<String> adapterTelecoms = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, telecomsField);
        final ArrayAdapter<String> adapterTransport = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, transportField);
        final ArrayAdapter<String> adapterPhysics = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, physicsField);
        final ArrayAdapter<String> adapterMechanics = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mechanicsField);
        final ArrayAdapter<String> adapterEnergy = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, energyField);
        final ArrayAdapter<String> adapterManagement = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, managementField);
        final ArrayAdapter<String> adapterChemist = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, chemistField);
        final ArrayAdapter<String> adapterDefault = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, defaultField);
        final ArrayAdapter<String> adapterFaculty = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, elementsFaculty);

        facultyUniversity.setAdapter(adapterFaculty);
        facultyUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch ((int) l) {
                    case 0:
                        teacherFaculty = "Faculty of Study";
                        fieldUniversity.setAdapter(adapterDefault);
                        break;
                    case 1:
                        teacherFaculty = "Faculty of Architecture";
                        fieldUniversity.setAdapter(adapterArchitecture);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        teacherField = "Field of Study";
                                        break;
                                    case 1:
                                        teacherField = "Architecture";
                                        break;
                                    case 2:
                                        teacherField = "Interior Design";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                teacherField = "Field of Study";
                            }
                        });
                        break;
                    case 2:
                        teacherFaculty = "Faculty of Chemical Technology";
                        fieldUniversity.setAdapter(adapterChemist);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        teacherField = "Field of Study";
                                        break;
                                    case 1:
                                        teacherField = "Chemical and Process Engineering";
                                        break;
                                    case 2:
                                        teacherField = "Chemical Technology";
                                        break;
                                    case 3:
                                        teacherField = "Environmental protection technology";
                                        break;
                                    case 4:
                                        teacherField = "Pharmaceutical Engineering";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                teacherField = "Field of Study";
                            }
                        });
                        break;
                    case 3:
                        teacherFaculty = "Faculty of Civil and Transport Engineering";
                        fieldUniversity.setAdapter(adapterTransport);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        teacherField = "Field of Study";
                                        break;
                                    case 1:
                                        teacherField = "Civil Engineering";
                                        break;
                                    case 2:
                                        teacherField = "Sustainable building engineering";
                                        break;
                                    case 3:
                                        teacherField = "Mechanical Engineering";
                                        break;
                                    case 4:
                                        teacherField = "Aerospace engineering";
                                        break;
                                    case 5:
                                        teacherField = "Transport";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                teacherField = "Field of Study";
                            }
                        });
                        break;
                    case 4:
                        teacherFaculty = "Faculty of Computing and Telecomunications";
                        fieldUniversity.setAdapter(adapterTelecoms);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        teacherField = "Field of Study";
                                        break;
                                    case 1:
                                        teacherField = "Computing";
                                        break;
                                    case 2:
                                        teacherField = "Bioinformatics";
                                        break;
                                    case 3:
                                        teacherField = "Artifical Inteligence";
                                        break;
                                    case 4:
                                        teacherField = "Electronics and Telecommunications";
                                        break;
                                    case 5:
                                        teacherField = "Information and Communication technologies";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                teacherField = "Field of Study";
                            }
                        });
                        break;
                    case 5:
                        teacherFaculty = "Faculty of Control, Robotics and Electrical Engineering";
                        fieldUniversity.setAdapter(adapterAutomatics);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        teacherField = "Field of Study";
                                        break;
                                    case 1:
                                        teacherField = "Automatic Control and Robotics";
                                        break;
                                    case 2:
                                        teacherField = "Electrical Engineering";
                                        break;
                                    case 3:
                                        teacherField = "Mathematics in Technology";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                teacherField = "Field of Study";
                            }
                        });
                        break;
                    case 6:
                        teacherFaculty = "Faculty of Engineering Management";
                        fieldUniversity.setAdapter(adapterManagement);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        teacherField = "Field of Study";
                                        break;
                                    case 1:
                                        teacherField = "Logistics";
                                        break;
                                    case 2:
                                        teacherField = "Engineering";
                                        break;
                                    case 3:
                                        teacherField = "Management";
                                        break;
                                    case 4:
                                        teacherField = "Safety Engineering";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                teacherField = "Field of Study";
                            }
                        });
                        break;
                    case 7:
                        teacherFaculty = "Faculty of Environmental Engineering and Energy";
                        fieldUniversity.setAdapter(adapterEnergy);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        teacherField = "Field of Study";
                                        break;
                                    case 1:
                                        teacherField = "Environmental Engineering";
                                        break;
                                    case 2:
                                        teacherField = "Power Engineering";
                                        break;
                                    case 3:
                                        teacherField = "Industrial and renewable energy";
                                        break;
                                    case 4:
                                        teacherField = "Aviation engineering";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                teacherField = "Field of Study";
                            }
                        });
                        break;
                    case 8:
                        teacherFaculty = "Faculty of Materials Engineering and Technical Physics";
                        fieldUniversity.setAdapter(adapterPhysics);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        teacherField = "Field of Study";
                                        break;
                                    case 1:
                                        teacherField = "Education in Technology nad informatics";
                                        break;
                                    case 2:
                                        teacherField = "Technical physics";
                                        break;
                                    case 3:
                                        teacherField = "Materials Engineering";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                teacherField = "Field of Study";
                            }
                        });
                        break;
                    case 9:
                        teacherFaculty = "Faculty of Mechanical Engineering";
                        fieldUniversity.setAdapter(adapterMechanics);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        teacherField = "Field of Study";
                                        break;
                                    case 1:
                                        teacherField = "Biomedical engineering";
                                        break;
                                    case 2:
                                        teacherField = "Mechanical Engineering";
                                        break;
                                    case 3:
                                        teacherField = "Mechatronics";
                                        break;
                                    case 4:
                                        teacherField = "Management and Production Engineering";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                teacherField = "Field of Study";
                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                teacherFaculty = "Faculty of Study";
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String facultyString = teacherFaculty;
                final String fieldString = teacherField;

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

                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users Accounts").document(emailString);
                Map<String, Object> user = new HashMap<>();
                user.put("Name", nameTeacher);
                user.put("Surname", surnameTeacher);
                user.put("Faculty", facultyString);
                user.put("Field", fieldString);
                user.put("Email", emailString);
                user.put("User", userType);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(Void aVoid) {
                        NotificationChannel channel = new NotificationChannel("channel01", "name",
                                NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                        channel.setDescription("description");

                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);

                        Notification notification = new NotificationCompat.Builder(ResetFacultyAndFieldTeacher.this, "channel01")
                                .setSmallIcon(android.R.drawable.ic_dialog_info)
                                .setContentTitle("Settings")
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

                        Notification notification = new NotificationCompat.Builder(ResetFacultyAndFieldTeacher.this, "channel01")
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
                Intent intent = new Intent(ResetFacultyAndFieldTeacher.this, SettingsPanelTeacher.class);
                intent.putExtra("Email", emailString);
                startActivity(intent);
            }
        });
    }
}