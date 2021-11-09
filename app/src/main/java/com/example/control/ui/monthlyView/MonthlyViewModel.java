package com.example.control.ui.monthlyView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonthlyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MonthlyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is monthly view fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}