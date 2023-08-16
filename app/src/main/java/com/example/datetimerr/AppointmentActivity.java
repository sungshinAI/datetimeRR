package com.example.datetimerr;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button addDateTimeButton;
    private Button createAppointmentButton;
    private FirebaseAuth mAuth;

    private List<Date> selectedDateTimes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        mAuth = FirebaseAuth.getInstance();

        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        addDateTimeButton = findViewById(R.id.buttonAddDateTime);
        createAppointmentButton = findViewById(R.id.buttonCreateAppointment);

        addDateTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSelectedDateTime();
            }
        });

        createAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAppointments();
            }
        });
    }

    private void addSelectedDateTime() {
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int dayOfMonth = datePicker.getDayOfMonth();

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, hour, minute);

        selectedDateTimes.add(calendar.getTime());
    }

    private void createAppointments() {
        String creator = getCurrentUserDisplayName(); // 로그인한 사용자 이름 가져오기

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (Date dateTime : selectedDateTimes) {
            Map<String, Object> appointmentData = new HashMap<>();
            appointmentData.put("creator", creator);
            appointmentData.put("dateTime", dateTime);

            db.collection("appointments")
                    .add(appointmentData)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                // 약속 정보 저장 성공
                            } else {
                                // 약속 정보 저장 실패
                            }
                        }
                    });
        }
    }

    // 현재 로그인한 사용자의 표시 이름 가져오기
    private String getCurrentUserDisplayName() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getDisplayName();
        }
        return "Unknown";
    }

    // 다른 메소드들...
}
