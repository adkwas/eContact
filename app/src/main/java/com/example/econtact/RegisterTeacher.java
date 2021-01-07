package com.example.econtact;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterTeacher extends AppCompatActivity {

    EditText nameTeacher, surnameTeacher, emailTeacher, passwordTeacher, repeatPasswordTeacher;
    Button registerButton, loginButton, setPicture;
    Spinner facultyUniversity, fieldUniversity;
    String teacherFaculty, teacherField;
    StorageReference storageReference;
    ImageView pictureTeacher;

    Boolean isPick = false;

    Uri imageUri;


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

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher);

        nameTeacher = findViewById(R.id.nameTeacher_RegisterTeacher);
        surnameTeacher = findViewById(R.id.surnameTeacher_RegisterTeacher);
        emailTeacher = findViewById(R.id.emailTeacher_RegisterTeacher);
        passwordTeacher = findViewById(R.id.passwordTeacher_RegisterTeacher);
        repeatPasswordTeacher = findViewById(R.id.copypasswordTeacher_RegisterTeacher);
        registerButton = findViewById(R.id.registerButton_RegisterTeacher);
        loginButton = findViewById(R.id.loginButton_RegisterTeacher);
        facultyUniversity = findViewById(R.id.spinnerFaculty_RegisterTeacher);
        fieldUniversity = findViewById(R.id.spinnerField_RegisterTeacher);

        setPicture = findViewById(R.id.setPicture_RegisterTeacher);
        pictureTeacher = findViewById(R.id.imageView1_ConfirmTickets);

        storageReference = FirebaseStorage.getInstance().getReference();

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

        //Start Login Activity when user click a Login Button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterTeacher.this, Login.class));
            }
        });

        //When teacher click a Register Button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String emailString = emailTeacher.getText().toString().trim();
                final String passwordString = passwordTeacher.getText().toString().trim();
                final String repeatString = repeatPasswordTeacher.getText().toString().trim();
                final String nameString = nameTeacher.getText().toString();
                final String surnameString = surnameTeacher.getText().toString();
                final String facultyString = teacherFaculty;
                final String fieldString = teacherField;

                //Save in Database type registered user - in this time this is teacher
                final String userType = "Teacher";


                //Check if the email you entered is in the correct form
                boolean emailValue = isEmailValid(emailString);

                //Checking variables
                final String checkFaculty = "Faculty of Study";
                final String checkField = "Field of Study";

                if (nameString.isEmpty()) {
                    nameTeacher.setError("Name is empty!");
                    return;
                }
                if (surnameString.isEmpty()) {
                    surnameTeacher.setError("Surname is empty!");
                    return;
                }

                if (facultyString.equals(checkFaculty)) {
                    Toast.makeText(getApplicationContext(), "Please check a faculty pole!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (fieldString.equals(checkField)) {
                    Toast.makeText(getApplicationContext(), "Please check a field pole!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (emailString.isEmpty()) {
                    emailTeacher.setError("Email is empty!");
                    return;
                }

                if (!emailValue) {
                    emailTeacher.setError("Email must have a email type!");
                    return;
                }

                if (passwordString.isEmpty()) {
                    emailTeacher.setError("Password is empty!");
                    return;
                }

                if (repeatString.isEmpty()) {
                    repeatPasswordTeacher.setError("Repeat password is empty!");
                    return;
                }

                if(!isPick){
                    Toast.makeText(RegisterTeacher.this, "Choose a photo!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isPick){
                    uploadImageToFirebase(imageUri, emailString);
                }

                //Register new teacher user
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users Accounts").document(emailString);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Name", nameString);
                            user.put("Surname", surnameString);
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

                                    Notification notification = new NotificationCompat.Builder(RegisterTeacher.this, "channel01")
                                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                                            .setContentTitle("Register success!")
                                            .setContentText("You can login to your account")
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

                                    Notification notification = new NotificationCompat.Builder(RegisterTeacher.this, "channel01")
                                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                                            .setContentTitle("Error: " )
                                            .setContentText(e.toString())
                                            .setDefaults(Notification.DEFAULT_ALL)
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                            .build();
                                    notificationManager.notify(0, notification);

                                }
                            });

                            FirebaseUser newUser = FirebaseAuth.getInstance().getCurrentUser();
                            assert newUser != null;

                            newUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onSuccess(Void aVoid) {
                                    NotificationChannel channel = new NotificationChannel("channel01", "name",
                                            NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                                    channel.setDescription("description");

                                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                    notificationManager.createNotificationChannel(channel);

                                    Notification notification = new NotificationCompat.Builder(RegisterTeacher.this, "channel01")
                                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                                            .setContentTitle("eContact")
                                            .setContentText("Verification Email has been sent!")
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

                                    Notification notification = new NotificationCompat.Builder(RegisterTeacher.this, "channel01")
                                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                                            .setContentTitle("Error: " )
                                            .setContentText(e.getMessage())
                                            .setDefaults(Notification.DEFAULT_ALL)
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                            .build();
                                    notificationManager.notify(0, notification);

                                }
                            });
                        } else {
                            NotificationChannel channel = new NotificationChannel("channel01", "name",
                                    NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
                            channel.setDescription("description");

                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                            notificationManager.createNotificationChannel(channel);

                            Notification notification = new NotificationCompat.Builder(RegisterTeacher.this, "channel01")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("Error: ")
                                    .setContentText(Objects.requireNonNull(task.getException()).getMessage())
                                    .setDefaults(Notification.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                    .build();
                            notificationManager.notify(0, notification);
                        }
                    }
                });
            }
        });

        setPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                imageUri = data.getData();
                pictureTeacher.setImageURI(imageUri);
                isPick = true;
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri, String emailString) {
        StorageReference fileRef = storageReference.child(emailString + ".jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
}