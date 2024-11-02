package com.example.drawertest3.ui.addbkordono;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddBkorDonoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddBkorDonoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}