package com.example.control.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.control.R;

import java.util.ArrayList;

/**
 * Adapter that extends to class Mood to dynamically add moods for ViewDayMoodActivity
 */
public class MoodAdapter extends ArrayAdapter<Mood> {

    private ArrayList<Mood> moods;
    private Context context;

    // constructor
    public MoodAdapter(Context context, ArrayList<Mood> moods){
        super(context, 0, moods);
        this.moods = moods;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content_card_view, parent,false);
        }
        Mood mood = moods.get(position);

        // change the color of the card view based on chosen colors
        changeCardViewColor(mood.getMood(), view);

        // sets the mood title
        TextView moodTitle = view.findViewById(R.id.card_view_text);
        moodTitle.setText(mood.getMood());

        return view;
    }

    /**
     * Based on the given move, change the background color
     * @param mood_title
     * @param view
     */
    private void changeCardViewColor(String mood_title, View view) {
        CardView cardView = view.findViewById(R.id.card_view);
        TextView habitTitle = view.findViewById(R.id.card_view_text);

        switch (mood_title) {
            case "Happy": {
                int currColor = ContextCompat.getColor(context, R.color.happy);
                cardView.setCardBackgroundColor(currColor);
                break;
            }
            case "Sad": {
                int currColor = ContextCompat.getColor(context, R.color.sad);
                cardView.setCardBackgroundColor(currColor);
                break;
            }
            case "Okay": {
                int currColor = ContextCompat.getColor(context, R.color.okay);
                cardView.setCardBackgroundColor(currColor);
                break;
            }
            case "Tired": {
                int currColor = ContextCompat.getColor(context, R.color.tired);
                cardView.setCardBackgroundColor(currColor);
                break;
            }
            case "Anxious": {
                int currColor = ContextCompat.getColor(context, R.color.anxious);
                cardView.setCardBackgroundColor(currColor);
                break;
            }
            case "Angry": {
                int currColor = ContextCompat.getColor(context, R.color.angry);
                cardView.setCardBackgroundColor(currColor);
                break;
            }
        }
    }

}
