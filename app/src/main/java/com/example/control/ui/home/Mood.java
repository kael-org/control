package com.example.control.ui.home;

/**
 * Instance of a Mood mood with the name of the mood
 */
public class Mood {
    private String mood;

    public Mood(String curr_mood) {
        this.mood = curr_mood;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }
}
