package com.example.control.ui.add;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is add mood fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}