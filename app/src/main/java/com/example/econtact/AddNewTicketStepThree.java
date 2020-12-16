package com.example.econtact;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddNewTicketStepThree extends AppCompatActivity {

    Spinner typeMeetSpinner;
    EditText datePicker, timePicker, reasonType;
    int minuteVariable, hourVariable, dayVariable, monthVariable, yearVariable;
    Button nextStep, previousStep;
    String nameStudent, surnameStudent, facultyStudent, fieldStudent, degreeStudent, semesterStudent, indexNumberStudent, nameTeacher,
            surnameTeacher, facultyTeacher, fieldTeacher, dayVariableString, monthVariableString, yearVariableString, selectedTypeMeet, reason;
    String[] typeMeetArray = {"Consultation", "Thesis", "Teleconference - meeting", "Do Overdue classes"};
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_ticket_step_three);

        //Get ID objects about TimePicker, DatePicker and Buttons
        datePicker = findViewById(R.id.datepicker_step3);
        timePicker = findViewById(R.id.timepicker_step3);
        typeMeetSpinner = findViewById(R.id.spinnerTypeMeet_step3);
        reasonType = findViewById(R.id.reasonType_step3);
        previousStep = findViewById(R.id.previous_step3);
        nextStep = findViewById(R.id.nextStep3);

        //Get Instance for FirebaseFirestore
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Set typeMeet adapter typeMeetArray
        final ArrayAdapter<String> adapterElements = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeMeetArray);
        typeMeetSpinner.setAdapter(adapterElements);

        //The user selects one type of match with the spinner
        typeMeetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch ((int) l) {
                    case 0:
                        selectedTypeMeet = "Consultation";
                        break;
                    case 1:
                        selectedTypeMeet = "Thesis";
                        break;
                    case 2:
                        selectedTypeMeet = "Teleconference - meeting";
                        break;
                    case 3:
                        selectedTypeMeet = "Do Overdue classes";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedTypeMeet = "Consultation";
            }
        });


        //Set a instance to calendar and set day,month,year of calendar
        final Calendar calendar = Calendar.getInstance();

        //Set the variables as day month year
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        //If click a textView DatePicker (Calendar) and action
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNewTicketStepThree.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    //If user click a calendar and pick some date
                    public void onDateSet(DatePicker datePicker, int yearPick, int monthPick, int dayPick) {
                        //Month must be increased by one because first month has o 0 value
                        monthPick = monthPick + 1;
                        String date = dayPick + "." + monthPick + "." + yearPick;
                        dayVariable = dayPick;
                        monthVariable = monthPick;
                        yearVariable = yearPick;
                        AddNewTicketStepThree.this.datePicker.setText(date);
                        dayVariableString = String.valueOf(dayVariable);
                        monthVariableString = String.valueOf(monthVariableString);
                        yearVariableString = String.valueOf(yearVariableString);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        //If click a textView TimePicker and action
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddNewTicketStepThree.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    //If user click a TimePicker and pick some time
                    public void onTimeSet(TimePicker timePicker, int hourPick, int minutePick) {
                        minuteVariable = minutePick;
                        hourVariable = hourPick;

                        //Get instance for calendar and set time in calendar and timePick
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, hourVariable, minuteVariable);
                        AddNewTicketStepThree.this.timePicker.setText(DateFormat.format("k:mm ", calendar));
                    }
                }, 24, 0, true
                );
                timePickerDialog.updateTime(hourVariable, minuteVariable);
                timePickerDialog.show();
            }
        });

        //If user want send a ticket to teacher user
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Save reason meet whick user write in application
                reason = reasonType.getText().toString();

                //Download data lector from previous activity
                nameTeacher = getIntent().getStringExtra("nameTeacher");
                surnameTeacher = getIntent().getStringExtra("surnameTeacher");
                facultyTeacher = getIntent().getStringExtra("facultyTeacher");
                fieldTeacher = getIntent().getStringExtra("fieldTeacher");

                //Download data student from previous activity
                nameStudent = getIntent().getStringExtra("nameStudent");
                surnameStudent = getIntent().getStringExtra("surnameStudent");
                facultyStudent = getIntent().getStringExtra("facultyStudent");
                fieldStudent = getIntent().getStringExtra("fieldStudent");
                degreeStudent = getIntent().getStringExtra("degreeStudent");
                semesterStudent = getIntent().getStringExtra("semesterStudent");
                indexNumberStudent = getIntent().getStringExtra("indexNumberStudent");

                //Save ticket in Cloud Fire as: nameStudent + surnameStudent + to + nameTeacher + surnameTeacher - Date: dayMeet . monthMeet . yearMeet
                //Such a provision is to remove the problem of obscuring the same reports or similar to the same teacher
                DocumentReference documentReference = firebaseFirestore.collection("Pending applications")
                        .document(nameStudent + " " + surnameStudent + " to " + nameTeacher + " " + surnameTeacher +
                                " date: " + dayVariable + "." + monthVariable + "." + yearVariable + " time: "+ hourVariable + ":" + minuteVariable);
                Map<String, Object> user = new HashMap<>();
                //Data teacher
                user.put("nameTeacher", nameTeacher);
                user.put("surnameTeacher", surnameTeacher);
                user.put("facultyTeacher", facultyTeacher);
                user.put("fieldTeacher", fieldTeacher);

                user.put("nameTeacher2", " ");
                user.put("surnameTeacher2", " ");
                user.put("facultyTeacher2", " ");
                user.put("fieldTeacher2", " ");

                //Data student
                user.put("nameStudent", nameStudent);
                user.put("surnameStudent", surnameStudent);
                user.put("facultyStudent", facultyStudent);
                user.put("fieldStudent", fieldStudent);
                user.put("degreeStudent", degreeStudent);
                user.put("semesterStudent", semesterStudent);
                user.put("indexNumberStudent", indexNumberStudent);

                //Data ticket///
                //Time meet
                //The minutes and hours of the meeting are saved as a string
                //type to eliminate problems with reading data from Cloud Fire
                String minuteString = String.valueOf(minuteVariable);
                String hourString = String.valueOf(hourVariable);
                user.put("minuteTicket", minuteString);
                user.put("hourTicket", hourString);

                //Date meet
                //The day, month and year of the meeting are saved as a string
                //type to eliminate problems with reading data from Cloud Fire
                String dayString = String.valueOf(dayVariable);
                String monthString = String.valueOf(monthVariable);
                String yearString = String.valueOf(yearVariable);
                user.put("dayTicket", dayString);
                user.put("monthTicket", monthString);
                user.put("yearTicket", yearString);

                //Reason meet
                user.put("typeMeet", selectedTypeMeet);
                user.put("reasonType", reason);

                //Check if ticket is saved in Cloud Fire
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onSuccess(Void aVoid) {

                        NotificationChannel channel = null;   // for heads-up notifications
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            channel = new NotificationChannel("channel01", "name",
                                    NotificationManager.IMPORTANCE_HIGH);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            channel.setDescription("description");
                        }

                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            notificationManager.createNotificationChannel(channel);
                        }

                        Notification notification = new NotificationCompat.Builder(AddNewTicketStepThree.this, "channel01")
                                .setSmallIcon(android.R.drawable.ic_dialog_info)
                                .setContentTitle("Application sent to the teacher!")
                                .setContentText("Wait for his decision")
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                .build();
                        notificationManager.notify(0, notification);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        NotificationChannel channel = null;   // for heads-up notifications
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            channel = new NotificationChannel("channel01", "name",
                                    NotificationManager.IMPORTANCE_HIGH);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            channel.setDescription("description");
                        }

                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            notificationManager.createNotificationChannel(channel);
                        }

                        Notification notification = new NotificationCompat.Builder(AddNewTicketStepThree.this, "channel01")
                                .setSmallIcon(android.R.drawable.ic_dialog_info)
                                .setContentTitle("Error!")
                                .setContentText(e.toString())
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                                .build();
                        notificationManager.notify(0, notification);

                    }
                });
                Intent intent = new Intent(AddNewTicketStepThree.this, PanelStudent.class);
                intent.putExtra("Email", getIntent().getStringExtra("Email"));
                startActivity(intent);
            }
        });

        //Back to double previous activity - AddNewTicketStepOne
        previousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewTicketStepThree.this, AddNewTicketStepOne.class));
            }
        });
    }
}