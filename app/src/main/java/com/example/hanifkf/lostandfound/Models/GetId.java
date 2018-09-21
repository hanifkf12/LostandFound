package com.example.hanifkf.lostandfound.Models;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

/**
 * Created by hanifkf on 04/04/2018.
 */

public class GetId {

    @Exclude
    public String HomeId;

    public <T extends GetId> T withId(@NonNull final String id){
        this.HomeId=id;
        return (T) this;
    }
}
