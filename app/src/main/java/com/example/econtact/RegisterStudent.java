package com.example.econtact;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterStudent extends AppCompatActivity {

    EditText nameStudent, surnameStudent, indexNumberStudent, emailStudent, passwordStudent, repeatPasswordStudent;
    Button registerButton, loginButton;
    Spinner facultyUniversity, fieldUniversity, degreeStudent, semesterStudent;
    String studentFaculty, studentField, studentDegree, studentSemester;

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
    String[] degreeTable = {"Degree of Study", "Engineering degree", "Master's degree"};
    String[] semesterEngineering = {"Semester of Study", "I", "II", "III", "IV", "V", "VI", "VII", "VIII"};
    String[] semesterMaster = {"Semester of Study", "I", "II", "III", "IV"};
    String[] default2 = {"Semester of Study"};

    //The method to check that the e-mail address has been saved correctly
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);
        nameStudent = findViewById(R.id.nameStudent__RegisterStudent);
        surnameStudent = findViewById(R.id.surnameStudent__RegisterStudent);
        indexNumberStudent = findViewById(R.id.indexNumber__RegisterStudent);
        emailStudent = findViewById(R.id.emailStudent__RegisterStudent);
        passwordStudent = findViewById(R.id.passwordStudent__RegisterStudent);
        repeatPasswordStudent = findViewById(R.id.copypasswordStudent__RegisterStudent);
        registerButton = findViewById(R.id.registerButton__RegisterStudent);
        loginButton = findViewById(R.id.loginButton__RegisterStudent);
        facultyUniversity = findViewById(R.id.spinnerFaculty__RegisterStudent);
        fieldUniversity = findViewById(R.id.spinnerField__RegisterStudent);
        degreeStudent = findViewById(R.id.spinnerDegree_RegisterStudent);
        semesterStudent = findViewById(R.id.semesterStudent__RegisterStudent);

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
        final ArrayAdapter<String> adapterDegree = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, degreeTable);
        final ArrayAdapter<String> adapterEngineering = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, semesterEngineering);
        final ArrayAdapter<String> adapterMaster = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, semesterMaster);
        final ArrayAdapter<String> adapterDefault2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, default2);

        //Set degree and semester whatever the student chooses
        degreeStudent.setAdapter(adapterDegree);
        degreeStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch ((int) l) {
                    case 0:
                        studentDegree = "Degree of Study";
                        semesterStudent.setAdapter(adapterDefault2);
                        break;
                    case 1:
                        studentDegree = "Engineering degree";
                        semesterStudent.setAdapter(adapterEngineering);
                        semesterStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        studentSemester = "Semester of Study";
                                        break;
                                    case 1:
                                        studentSemester = "I";
                                        break;
                                    case 2:
                                        studentSemester = "II";
                                        break;
                                    case 3:
                                        studentSemester = "III";
                                        break;
                                    case 4:
                                        studentSemester = "IV";
                                        break;
                                    case 5:
                                        studentSemester = "V";
                                        break;
                                    case 6:
                                        studentSemester = "VI";
                                        break;
                                    case 7:
                                        studentSemester = "VII";
                                        break;
                                    case 8:
                                        studentSemester = "VIII";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                    case 2:
                        studentDegree = "Master's degree";
                        semesterStudent.setAdapter(adapterMaster);
                        semesterStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch ((int) l) {
                                    case 0:
                                        studentSemester = "Semester of Study";
                                        break;
                                    case 1:
                                        studentSemester = "I";
                                        break;
                                    case 2:
                                        studentSemester = "II";
                                        break;
                                    case 3:
                                        studentSemester = "III";
                                        break;
                                    case 4:
                                        studentSemester = "IV";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                studentSemester = "Semester of Study";
                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Set faculty whatever the student chooses
        facultyUniversity.setAdapter(adapterFaculty);
        facultyUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch ((int) l) {
                    case 0:
                        studentFaculty = "Faculty of Study";
                        fieldUniversity.setAdapter(adapterDefault);
                        break;
                    case 1:
                        studentFaculty = "Faculty of Architecture";
                        fieldUniversity.setAdapter(adapterArchitecture);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        fieldUniversity.setAdapter(adapterChemics);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        fieldUniversity.setAdapter(adapterTransport);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        fieldUniversity.setAdapter(adapterTelecom);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        fieldUniversity.setAdapter(adapterAutomatics);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        fieldUniversity.setAdapter(adapterManagment);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        fieldUniversity.setAdapter(adapterEnergy);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        fieldUniversity.setAdapter(adapterPhysics);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        fieldUniversity.setAdapter(adapterMechanics);
                        fieldUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        //Start Login Activity after click Login Button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterStudent.this, Login.class);
                startActivity(intent);
            }
        });

        //If click a Register Button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String emailString = emailStudent.getText().toString().trim();
                final String passwordString = passwordStudent.getText().toString().trim();
                final String repeatString = repeatPasswordStudent.getText().toString().trim();
                final String nameString = nameStudent.getText().toString();
                final String surnameString = surnameStudent.getText().toString();
                final String indexString = indexNumberStudent.getText().toString();
                final String facultyString = studentFaculty;
                final String fieldString = studentField;
                final String degreeString = studentDegree;
                final String semesterString = studentSemester;

                //Save in Database type registered user - in this time this is student
                final String userType = "Student";

                //Check if the email you entered is in the correct form
                boolean emailValue = isEmailValid(emailString);

                //Checking variables
                final String checkFaculty = "Faculty of Study";
                final String checkDegree = "Degree of Study";
                final String checkField = "Field of Study";
                final String checkYear = "Semester of Study";


                //Check if all pole are saved
                if (nameString.isEmpty()) {
                    nameStudent.setError("Name is empty!");
                    return;
                }

                if (surnameString.isEmpty()) {
                    surnameStudent.setError("Surname is empty!");
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

                if (degreeString.equals(checkDegree)) {
                    Toast.makeText(getApplicationContext(), "Please check a semester pole!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (semesterString.equals(checkYear)) {
                    Toast.makeText(getApplicationContext(), "Please check a semester pole!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (indexString.isEmpty()) {
                    indexNumberStudent.setError("Index Number is empty!");
                    return;
                }

                if (emailString.isEmpty()) {
                    emailStudent.setError("Email is empty!");
                    return;
                }

                if (!emailValue) {
                    emailStudent.setError("Email must have a email type!");
                    return;
                }

                if (passwordString.isEmpty()) {
                    emailStudent.setError("Password is empty!");
                    return;
                }

                if (passwordString.length() < 8) {
                    emailStudent.setError("Password is too short!");
                }

                if (repeatString.isEmpty()) {
                    repeatPasswordStudent.setError("Repeat password is empty!");
                    return;
                }

                //Index student number must be 6 to 7 digits long
                if (indexString.length() > 6) {
                    indexNumberStudent.setError("Index number is too long!");
                    return;
                }

                if (indexString.length() < 5) {
                    indexNumberStudent.setError("Index number is too short!");
                    return;
                }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //Save student data in Cloud Fire Database
                            //studentID = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users Accounts").document(emailString);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Name", nameString);
                            user.put("Surname", surnameString);
                            user.put("Faculty", facultyString);
                            user.put("Field", fieldString);
                            user.put("Semester", semesterString);
                            user.put("Degree", degreeString);
                            user.put("Email", emailString);
                            user.put("IndexNumber", indexString);
                            user.put("User", userType);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "Registered success!");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "Error: " + e.toString());
                                }
                            });


                            FirebaseUser newUser = FirebaseAuth.getInstance().getCurrentUser();
                            assert newUser != null;

                            //Send email verify to new user
                            newUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterStudent.this, "Verification Email has been sent!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "Error: " + e.getMessage());
                                }
                            });


                        } else {
                            Toast.makeText(RegisterStudent.this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}


