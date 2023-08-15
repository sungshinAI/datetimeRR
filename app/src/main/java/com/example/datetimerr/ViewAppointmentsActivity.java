package com.example.datetimerr;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewAppointmentsActivity extends AppCompatActivity {

    private ListView listViewAppointments;
    private ArrayAdapter<String> adapter;
    private List<String> appointmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);

        listViewAppointments = findViewById(R.id.listViewAppointments);
        appointmentList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appointmentList);
        listViewAppointments.setAdapter(adapter);

        fetchAppointments();
    }

    private void fetchAppointments() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("appointments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String creator = document.getString("creator");
                            Date dateTime = document.getDate("dateTime");

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                            String formattedDateTime = dateFormat.format(dateTime);

                            appointmentList.add("Created by: " + creator + "\nDate and Time: " + formattedDateTime);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
