package com.example.control.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.control.R;
import com.example.control.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Random;

public class HomeFragment extends Fragment {
    public static final String VIEW_MOOD_TITLE = "VIEW MOOD TITLE";

    private FragmentHomeBinding binding;
    private static final int numQuotes = 5;

    private FirebaseFirestore db;
    private FirebaseUser user;
    private String uid;

    private CalendarView calendar;
    private TextView quote;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        // bind the values
        calendar = binding.calendarView;
        quote = binding.quoteHome;

        // set the random quote
        setRandomQuote();

        // calendar listener
        calendar.setOnDateChangeListener(this::onDateClick);

        return root;
    }

    public void setRandomQuote() {
        Integer random_num = (Integer) new Random().nextInt(numQuotes+1);

        DocumentReference documentReference = db.collection("Quotes").document(random_num.toString());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String random_quote = (String) document.getData().get("quote");
                        quote.setText(random_quote);
                    }
                }
            }
        });
    }

    private boolean onDateClick(CalendarView calendarView, int year, int month, int day) {
        // initialize Calendar format
        Calendar calendar_click = Calendar.getInstance();
        calendar_click.set(year, month, day);

        // get the complete date for database query
        String month_str = (String) DateFormat.format("MMMM", calendar_click.getTime());
        String date = month_str + " " + day + ", " + year;

        Intent intent = new Intent(getContext(), ViewDayMoodActivity.class);
        intent.putExtra(HomeFragment.VIEW_MOOD_TITLE, date);
        startActivity(intent);
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}