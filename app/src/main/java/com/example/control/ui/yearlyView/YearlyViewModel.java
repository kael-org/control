package com.example.control.ui.yearlyView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class YearlyViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public YearlyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is yearly view fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
