package com.example.control.ui.add;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.control.databinding.FragmentAddBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddFragment extends Fragment {
    public static String TAG = "Add Database";

    private FragmentAddBinding binding;

    private FirebaseFirestore db;
    private FirebaseUser user;
    private String uid;
    private CollectionReference collectionReference;

    private ToggleButton happyButton;
    private ToggleButton sadButton;
    private ToggleButton okayButton;
    private ToggleButton anxiousButton;
    private ToggleButton tiredButton;
    private ToggleButton angryButton;
    private EditText journalEntry;
    private TextView date;
    private Button addMood;

    private boolean moodAlreadyAdded;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // bind the views
        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // initialize database
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        collectionReference = db.collection("Users").document(uid).collection("Moods");

        // bind all the values
        happyButton = binding.happy;
        sadButton = binding.sad;
        okayButton = binding.okay;
        anxiousButton = binding.anxious;
        tiredButton = binding.tired;
        angryButton = binding.angry;
        journalEntry = binding.journalEntryAdd;
        date = binding.currentDateAdd;
        addMood = binding.moodAddButton;

        // set the date
        setCurrentDate();

        // listener for when the add mood is clicked
        addMood.setOnClickListener(this :: addMoodButtonClick);
        return root;
    }

    /**
     * Set the current date
     */
    public void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        // get the complete date
        String month_str = (String) DateFormat.format("MMMM", calendar.getTime());
        String date_str = month_str + " " + day + ", " + year;

        date.setText(date_str);
    }

    /**
     * When the add mood button is clicked, check if the mood already exists
     * If it exists, call addMood method to perform more checks and add
     * @param view
     * @return
     */
    private boolean addMoodButtonClick(View view) {
        DocumentReference documentReference = db.collection("Users").document(uid).
                collection("Moods").document(date.getText().toString());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // show a warning to the user
                        Toast.makeText(getContext(), "Mood already added for today!", Toast.LENGTH_SHORT).show();
                        clear();
                    } else {
                        addMood();
                    }
                }
            }
        });
        return true;
    }

    /**
     * Helper method to perform checks and add mood
     */
    private void addMood() {
        ArrayList<String> moods = new ArrayList<>();

        // perform checks and add
        if (happyButton.isChecked()) {
            moods.add("Happy");
        }
        if (sadButton.isChecked()) {
            moods.add("Sad");
        }
        if (anxiousButton.isChecked()) {
            moods.add("Anxious");
        }
        if (okayButton.isChecked()) {
            moods.add("Okay");
        }
        if (tiredButton.isChecked()) {
            moods.add("Tired");
        }
        if (angryButton.isChecked()) {
            moods.add("Angry");
        }
        // checks if the user has clicked at least one mood
        if (moods.isEmpty()) {
            Toast.makeText(getContext(), "Please choose at least one mood for today!",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            // Add to database
            Calendar calendar = Calendar.getInstance();
            String dateAdd = new SimpleDateFormat("MM/dd/yyyy").format(calendar.getTime());
            Date dateAddDB = null;
            try {
                dateAddDB = new SimpleDateFormat("MM/dd/yyyy").parse(dateAdd);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // get the journal entry
            String entry = journalEntry.getText().toString();

            // data
            HashMap<String, Object> data = new HashMap<>();
            data.put("date", dateAddDB);
            data.put("entry", entry);
            data.put("moods", moods);

            // add to firebase
            collectionReference
                    .document(date.getText().toString())
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // These are a method which gets executed when the task is succeeded
                            Log.d(TAG, "Data has been added successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // These are a method which gets executed if there’s any problem
                            Log.d(TAG, "Data could not be added!" + e.toString());
                        }
                    });
            Toast.makeText(getContext(), "Mood Added", Toast.LENGTH_SHORT).show();
            clear();
        }
    }

    /**
     * clears all the fields
     */
    private void clear(){
        happyButton.setChecked(false);
        sadButton.setChecked(false);
        angryButton.setChecked(false);
        anxiousButton.setChecked(false);
        tiredButton.setChecked(false);
        okayButton.setChecked(false);
        journalEntry.setText("");

    }

    /**
     * When the view is destroyed, set binding to null
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}