package com.example.melocommunity.models;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Register model classes
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Account.class);
        ParseObject.registerSubclass(Artist.class);
        ParseObject.registerSubclass(Comment.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("b299ho8EQ6DduGDG2Dvag0EGTY53uCICv1ldeuDJ")
                .clientKey("WPaL4m4hEoRMte7eU6bVgDShms9VL3OisKhpbEI2")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
