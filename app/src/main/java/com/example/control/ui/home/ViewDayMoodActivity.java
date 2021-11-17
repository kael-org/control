package com.example.control.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.control.databinding.ActivityViewDayMoodBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class ViewDayMoodActivity extends AppCompatActivity {

    private ActivityViewDayMoodBinding binding;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String uid;
    private DocumentReference documentReference;

    private FloatingActionButton backButton;
    private TextView titleView;
    private TextView journalEntry;
    private String title;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // creates the activity view
        binding = ActivityViewDayMoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the Intent that started this activity and extract them
        Intent intent = getIntent();
        title = intent.getStringExtra(HomeFragment.VIEW_MOOD_TITLE);

        // instantiate database
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        documentReference = db.collection("Users").document(uid).
                collection("Moods").document(title);

        // set the title
        titleView = binding.ViewDayMoodTitle;
        titleView.setText(title);

        // back button to go back to previous fragment
        backButton = binding.viewDayMoodBack;
        backButton.setOnClickListener(this::backButtonOnClick);

        // set the journal entry
        journalEntry = binding.ViewJournalEntry;
        setJournalEntry();
    }

    private boolean backButtonOnClick(View view) {
        finish();
        return true;
    }

    private void setJournalEntry() {
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String random_quote = (String) document.getData().get("entry");
                        journalEntry.setText(random_quote);
                    }
                }
            }
        });
    }
}
