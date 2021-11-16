package com.example.control.ui.add;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.control.databinding.FragmentAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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

        addMood.setOnClickListener(this :: addMoodButtonClick);
        return root;
    }

    public void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        // get the complete date
        String month_str = (String) DateFormat.format("MMMM", calendar.getTime());
        String date_str = month_str + " " + day + ", " + year;

        date.setText(date_str);
    }

    private boolean addMoodButtonClick(View view) {
        ArrayList<String> moods = new ArrayList<>();

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
        Toast.makeText(getContext(), "Mood Added", Toast.LENGTH_LONG).show();
        clear();
        return true;
    }

    private void clear(){
        //sets all input fields to their original values
        happyButton.setChecked(false);
        sadButton.setChecked(false);
        angryButton.setChecked(false);
        anxiousButton.setChecked(false);
        tiredButton.setChecked(false);
        okayButton.setChecked(false);
        journalEntry.setText("");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}