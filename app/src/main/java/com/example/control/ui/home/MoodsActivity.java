package com.example.control.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.control.databinding.ActivityMoodsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MoodsActivity extends AppCompatActivity {
    private ActivityMoodsBinding binding;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String uid;
    private DocumentReference documentReference;

    private FloatingActionButton backButton;
    private String title;
    private ListView moodListView;

    private ArrayList<Mood> moodList;
    private ArrayAdapter<Mood> moodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // creates the activity view
        binding = ActivityMoodsBinding.inflate(getLayoutInflater());
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


        // back button to go back to previous fragment
        backButton = binding.moodsBack;
        backButton.setOnClickListener(this::backButtonOnClick);

        // set the moods
        moodList = new ArrayList<>();
        moodListView = binding.moodListView;
        moodAdapter = new MoodAdapter(this, moodList);
        moodListView.setAdapter(moodAdapter);
        setMoodListView();
    }

    /**
     * Go back to previous activity
     * @param view
     * @return
     */
    private boolean backButtonOnClick(View view) {
        finish();
        return true;
    }

    /**
     * Getting all the moods to be listed to a listview
     */
    private void setMoodListView() {
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        moodList.clear();
                        ArrayList<?> moods = (ArrayList<?>) document.getData().get("moods");
                        for (Object mood : moods) {
                            moodList.add(new Mood((String) mood));
                        }
                        moodAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
